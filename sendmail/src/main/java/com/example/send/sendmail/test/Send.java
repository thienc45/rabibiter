package com.example.send.sendmail.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // Create a new connection factory
        ConnectionFactory factory = new ConnectionFactory();
        // Set the host to localhost
        factory.setHost("localhost");

        // Try-with-resources block to ensure the connection and channel are properly closed
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // Declare a queue
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // Message to be sent
            String message = "Hello, Long!";

            // Publish the message to the queue
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }


}
