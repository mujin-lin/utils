package com.fa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @Description: 为订单号，快递员编号等一系列编号的发号操作
 * @author: 伍成林
 * @date: 2020年3月27日
 */
@Component
@Slf4j
public class RedisImpulseSenderManager {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	// 不同环境产生不同的订单号
	@Value("${order.create}")
	private String diffEnvOrder;

	
	/**
	 *
	 * @Title getCourierNumber
	 * @Description 获取快递员编号，默认为9位数   prefix+编号 ， 缺少的长度都补0
	 * @param prefix 头信息
	 * @param key 快递员编号的发号器
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getCourierNumber(String prefix,String key) {
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			return null;
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		String next = String.format("%08d", nextNo);
		return prefix+next;
	}
	/**
	 * 获取快递员编号的没传头的情况下默认以0作为头，缺少的长度都补0
	 * @Title getCourierNumber
	 * @Description 
	 * @param key
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getCourierNumber(String key) {
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			return null;
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		String next = String.format("%08d", nextNo);
		return next;
	}
	
	/**
	 * 获取快递员编号的有头的情况下默认以prefix + 编号，缺少的长度都补0
	 * @Title getCourierNumber
	 * @Description 
	 * @param key 快递员编号的发号器的key
	 * @param prefix 头信息
	 * @param length 快递员除了头之外的长度
	 * @return
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getCourierNumber(String prefix,String key,Integer length) {
		String format = "%0"+length+"d";
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			return null;
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		String next = String.format(format, nextNo);
		return prefix+next;
	}
	/**
	 * 获取快递员编号的没传头的情况下默认以0作为头，缺少的长度都补0
	 * @Title getCourierNumber
	 * @Description 
	 * @param key 快递员编号的发号器的key
	 * @param length 快递员除了头之外的长度
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getCourierNumber(String key,Integer length) {
		String format = "%0"+length+"d";
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			return null;
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		return String.format(format, nextNo);
	}
	
	/**
	 * 设置快递员编号有长度则使用自定义的长度
	 * @Title setCourierNumber
	 * @Description 
	 * @return Boolean
	 * @author 伍成林
	 * @date: 2020年4月22日
	 */
	public String setCourierNumber(String key,String value,String length) {
		String format = "%0"+length+"d";
		if (!checkValue(value)) {
			log.error("传入的快递员编号中有非数字字符,或value为null{}",value);
			return "-1";
		}
		Integer number = Integer.valueOf(value)+1;
		stringRedisTemplate.opsForValue().set(key, number+"");
		log.info("设置快递员的编号为{}",String.format("%08d", number));
		return  String.format(format, number);
	}
	
	/**
	 * 设置快递员编号没有长度则使用8的默认长度
	 * @Title setCourierNumber
	 * @Description 
	 * @return Boolean
	 * @author 伍成林
	 * @date: 2020年4月22日
	 */
	public String setCourierNumber(String key,String value) {
		if (!checkValue(value)) {
			log.error("传入的快递员编号中有非数字字符,或value为null{}",value);;
			return  "-1";
		}
		Integer number = Integer.valueOf(value)+1;
		stringRedisTemplate.opsForValue().set(key, number+"");
		log.info("设置快递员的编号为{}",String.format("%08d", number));
		return  String.format("%08d", number);
	}
	
	
	/**
	 * 获取订单号的发号器 长度为除去头外的8位数，缺少的补零处理
	 * @Title getOrderNumber
	 * @Description 
	 * @param prefix 支付业务的头信息
	 * @param key 为当天的日期
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getOrderNumber(String prefix,String key) {
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			stringRedisTemplate.opsForValue().set(key, "0",25L,TimeUnit.HOURS);
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		String next = String.format("%08d", nextNo);
		return prefix+next+diffEnvOrder;
	}
	
	/**
	 * 获取订单号的发号器,可自定义长度
	 * @Title getOrderNumber
	 * @Description 
	 * @param prefix 支付宝业务的头信息
	 * @param key 为当天的日期，也作为订单编号的一部分
	 * @param lenth 除去头的长度
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月27日
	 */
	public String getOrderNumber(String prefix,String key,Integer lenth) {
		String format = "%0"+lenth+"d";
		if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
			stringRedisTemplate.opsForValue().set(key, "0",25l,TimeUnit.HOURS);
		}
		Long nextNo = stringRedisTemplate.opsForValue().increment(key);
		String next = String.format(format, nextNo);
		return prefix+next+diffEnvOrder;
	}
	
	/**
	 * 检查传递过来的value是否是纯数字
	 * @Title checkValue
	 * @Description 
	 * @param value
	 * @return Boolean
	 * @author 伍成林
	 * @date: 2020年4月22日
	 */
	private Boolean checkValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return Boolean.FALSE;
		}
        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(value);
        return m.matches();
	}
}
