package com.gjw.activemq.queue.producer;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * queue:消息消费者
 */
public class Producer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = "admin";
		String password = "admin";
		String url = "tcp://192.168.1.147:61616";
		//点对点queue名称
		String queueName = "FirstQueue";
		ConnectionFactory contectionFactory = new ActiveMQConnectionFactory( user, password, url);
		try {
			//Connection:负责创建 Session
			Connection connection = contectionFactory.createConnection();
			connection.start();
			//Session 创建 MessageProducer（用来发消息） 和 MessageConsumer（用来接收消息）
			Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			//Destination 是消息的目的地
			Destination destination = session.createQueue(queueName);
			//创建消息生产者
			MessageProducer producer = session.createProducer(destination);
			//设置 持久化 配置，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
			for (int i = 0; i < 20; i++) {
				MapMessage message = session.createMapMessage();
				Date date = new Date(); 
				message.setString("count", "生产者："+i);
				Thread.sleep(100);
				producer.send(message);
				System.out.println("--发送消息：" + date);
			}
			session.commit();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}