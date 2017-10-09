package com.gjw.activemq.queue.comsumer;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * queue:消息消费者
 */
public class Consumer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = "admin";
		String password = "admin";
		String url = "tcp://192.168.1.147:61616";
		//点对点queue名称
		String queueName = "FirstQueue";
		
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);
			MessageConsumer message = session.createConsumer(destination);
			message.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					System.out.println("-->>>>"+msg.toString());
					BytesMessage message = (BytesMessage) msg;
					try {
						System.out.println("--收到消息：" + message.getByteProperty("utf-8"));
						session.commit();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			Thread.sleep(30000);
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}