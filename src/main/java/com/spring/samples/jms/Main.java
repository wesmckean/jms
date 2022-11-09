package com.spring.samples.jms;

import javax.jms.Destination;
import javax.jms.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-mq-context.xml");
        
        JmsTemplate jmsTemplate = appContext.getBean(JmsTemplate.class);
        jmsTemplate.send((Session session) -> session.createTextMessage("Hello !!! Welcome to the world of Spring/ActiveMQ"));        
        
        Destination destination = appContext.getBean(Destination.class);
        Object o = jmsTemplate.receiveAndConvert(destination);
        System.out.println(o);
    }
}
