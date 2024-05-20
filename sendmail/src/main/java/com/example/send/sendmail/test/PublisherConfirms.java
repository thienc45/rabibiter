package com.example.send.sendmail.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class PublisherConfirms {
    private static final String EXCHANGE_NAME = "test_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.confirmSelect();

            // Individual Confirm
            publishMessagesIndividually(channel);

            // Batch Confirm
            publishMessagesInBatch(channel);

            // Asynchronous Confirm
            publishMessagesAsynchronously(channel);
        }
    }

    private static void publishMessagesIndividually(Channel channel) throws IOException, InterruptedException, TimeoutException {
        int messageCount = 50000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            String message = "Message " + i;
            channel.basicPublish(EXCHANGE_NAME, "test", null, message.getBytes());
            channel.waitForConfirmsOrDie(5000);  // Wait for confirmation
        }
        long end = System.currentTimeMillis();
        System.out.println("Published " + messageCount + " messages individually in " + (end - start) + " ms");
    }

    private static void publishMessagesInBatch(Channel channel) throws IOException, InterruptedException, TimeoutException {
        int messageCount = 50000;
        int batchSize = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            String message = "Message " + i;
            channel.basicPublish(EXCHANGE_NAME, "test", null, message.getBytes());
            if (i % batchSize == 0) {
                channel.waitForConfirmsOrDie(5000);  // Wait for batch confirmation
            }
        }
        channel.waitForConfirmsOrDie(5000);
        long end = System.currentTimeMillis();
        System.out.println("Published " + messageCount + " messages in batch in " + (end - start) + " ms");
    }

    private static void publishMessagesAsynchronously(Channel channel) throws IOException, InterruptedException {
        int messageCount = 50000;
        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        channel.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
            String body = outstandingConfirms.get(sequenceNumber);
            System.err.format("Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",
                    body, sequenceNumber, multiple);
            cleanOutstandingConfirms.handle(sequenceNumber, multiple);
        });

        long start = System.currentTimeMillis();
        for (int i = 0; i < messageCount; i++) {
            String message = "Message " + i;
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish(EXCHANGE_NAME, "test", null, message.getBytes());
        }

        while (outstandingConfirms.size() > 0) {
            Thread.sleep(100);  // Wait for all confirmations
        }

        long end = System.currentTimeMillis();
        System.out.println("Published " + messageCount + " messages and handled confirms asynchronously in " + (end - start) + " ms");
    }
}
