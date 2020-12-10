package com.fa.ebs.config;

import com.fa.ebs.pay.utils.RabbitmqUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
public class RabbitmqConfig {
	private final String payExchange = "FAEB_pay_exchange";
	
	private final String delResExchange = "FAEB_deliveryReserve_exchange";

	private final String scanLoginExchange = "FAEB_scan_login_exchange";

	private final String dealWithFaultExchange = "FAEB_deal_fault_exchange";

	private final String sendSmsExchange = "FAEB_send_sms_exchange";

	private final String ENABLE_OR_DISABLE_DEV_BASIC_EXCHANGE = "FAEB_enable_or_disable";

	private final String sendTrackingOcrResultExchange="FAEB_ocr_tracking_result_exchange";

	// 死信交换机
	private final String deadLetterExchange = "FAEB_dead_letter_exchange";
	// 死信交换机的路由键
	private final String appointmentDeadLetterRoutingKey = "appointment_dead_letter";
	// OCR 死信交换机的路由键
	private final String ocrDeadLetterRoutingKey="ocr_dead_letter_routing_key";

	@Bean
	public RabbitAdmin createRabbitAdmin(CachingConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.setAutoStartup(Boolean.TRUE);
		createExchanges(rabbitAdmin);
		return rabbitAdmin;
	}
	/**
	 * 创建交换机和队列
	 * @Title test3
	 * @Description 
	 * @return
	 * @return DirectExchange
	 * @author 伍成林
	 * @date: 2020年4月24日
	 */
	public void createExchanges(RabbitAdmin rabbitAdmin) {
		rabbitAdmin.declareExchange(new DirectExchange(scanLoginExchange,Boolean.TRUE,Boolean.FALSE)); // 创建扫码登柜的交换机
		rabbitAdmin.declareExchange(new DirectExchange(delResExchange,Boolean.TRUE,Boolean.FALSE));// 创建预约的交换机
		rabbitAdmin.declareExchange(new DirectExchange(payExchange,Boolean.TRUE,Boolean.FALSE));// 创建支付的交换机
		rabbitAdmin.declareExchange(new DirectExchange(dealWithFaultExchange,Boolean.TRUE,Boolean.FALSE));// 创建支付的交换机
		rabbitAdmin.declareExchange(new DirectExchange(ENABLE_OR_DISABLE_DEV_BASIC_EXCHANGE,Boolean.TRUE,Boolean.FALSE)); // 创建后台管理禁用启用箱格或者规则的交换机
		rabbitAdmin.declareExchange(new DirectExchange(sendTrackingOcrResultExchange,Boolean.TRUE,Boolean.FALSE)); // 创建小程序上传OCR识别结果的交换机
		// 创建短信发送的队列
		rabbitAdmin.declareQueue(new Queue(RabbitmqUtil.SEND_SMS_QUEUE_NAME,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE));
		// 创建发送公众号消息的队列
		rabbitAdmin.declareQueue(new Queue(RabbitmqUtil.SEND_GZH_QUEUE_NAME,Boolean.TRUE,Boolean.FALSE,Boolean.FALSE));
//		// 创建存放OCR死信的队列
		Queue queue = QueueBuilder.durable(RabbitmqUtil.OCR_DEAD_LETTER_QUEUE).build();
		rabbitAdmin.declareQueue(queue);
//		// 创建死信交换机
		DirectExchange deadLetter = ExchangeBuilder.directExchange(deadLetterExchange).durable(true).build();
		rabbitAdmin.declareExchange(deadLetter);
//
		// 创建绑定的对象
		Binding binding = BindingBuilder.bind(queue).to(deadLetter).with(ocrDeadLetterRoutingKey);
		// 将死信队列绑定当死信交换机
		rabbitAdmin.declareBinding(binding);

//		Queue testDevQueue = QueueBuilder.durable("test_dev").deadLetterExchange(deadLetterExchange)
//				.deadLetterRoutingKey(appointmentDeadLetterRoutingKey).build();
//		// 创建测试消息队列
//		rabbitAdmin.declareQueue(testDevQueue);

	}
	/**
	 * 设置rabbitmq监听的listener
	 * @Title createContainer
	 * @Description:
	 * @param cachingConnectionFactory rabbit连接工厂对象
	 * @return RabbitListenerContainerFactory
	 * @throws        
	 * @author 伍成林 
	 * @date 2020/6/12
	 */
	@Bean("listenerContainer")
	public RabbitListenerContainerFactory createContainer(CachingConnectionFactory cachingConnectionFactory){
		SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
		containerFactory.setPrefetchCount(1);
		containerFactory.setConnectionFactory(cachingConnectionFactory);
		return containerFactory;
	}

}
