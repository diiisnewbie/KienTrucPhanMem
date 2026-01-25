package iuh.fit;

import com.rabbitmq.client.*;


public class EmailProducer {

    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(
                    RabbitMQConfig.QUEUE_NAME,
                    true,
                    false,
                    false,
                    null
            );

            String message = "abc@gmail.com|Test RabbitMQ|Email gửi từ Java Core";

            channel.basicPublish(
                    "",
                    RabbitMQConfig.QUEUE_NAME,
                    null,
                    message.getBytes()
            );

            System.out.println("📤 Đã gửi message vào queue");
        }
    }
}
