package com.example.viapidemo;

import java.util.regex.Pattern;

/**
 * Description:
 *
 * @Author: 伍成林
 * @Date： 2020年 12月18日
 */
public class RegUtils {
    /**
     * 身份证号的正则
     */
    public final static String ID_CARD_REG="^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     *
     * @Title isIdCard
     * @Description: 判断输入的字符串是否为身份证号
     * @param idCard 输入的身份证号
     * @return boolean 返回输入的字符串是否是身份证号
     * @author 伍成林
     * @date 2020年12月18日
     */
    public static boolean isIdCard(String idCard){
        return Pattern.matches(ID_CARD_REG,idCard);
    }

}
