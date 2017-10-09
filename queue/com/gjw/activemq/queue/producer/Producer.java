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
 * queue:��Ϣ������
 */
public class Producer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = "admin";
		String password = "admin";
		String url = "tcp://192.168.1.147:61616";
		//��Ե�queue����
		String queueName = "FirstQueue";
		ConnectionFactory contectionFactory = new ActiveMQConnectionFactory( user, password, url);
		try {
			//Connection:���𴴽� Session
			Connection connection = contectionFactory.createConnection();
			connection.start();
			//Session ���� MessageProducer����������Ϣ�� �� MessageConsumer������������Ϣ��
			Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			//Destination ����Ϣ��Ŀ�ĵ�
			Destination destination = session.createQueue(queueName);
			//������Ϣ������
			MessageProducer producer = session.createProducer(destination);
			//���� �־û� ���ã��˴�ѧϰ��ʵ�ʸ�����Ŀ����
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            
			for (int i = 0; i < 20; i++) {
				MapMessage message = session.createMapMessage();
				Date date = new Date(); 
				message.setString("count", "�����ߣ�"+i);
				Thread.sleep(100);
				producer.send(message);
				System.out.println("--������Ϣ��" + date);
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