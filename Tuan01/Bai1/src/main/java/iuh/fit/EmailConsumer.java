package iuh.fit;

import com.rabbitmq.client.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

public class EmailConsumer {

    private static final String HOST = "localhost";

    // biến email (load từ .env)
    private static String FROM_EMAIL;
    private static String APP_PASSWORD;

    public static void main(String[] args) throws Exception {

        // 1️⃣ Load .env
        Dotenv dotenv = Dotenv.load();

        FROM_EMAIL = dotenv.get("MAIL_NAME");
        APP_PASSWORD = dotenv.get("MAIL_PASSWORD");

        // 2️⃣ RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(
                RabbitMQConfig.QUEUE_NAME,
                true,
                false,
                false,
                null
        );

        System.out.println("⏳ Đang chờ email...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody());
            System.out.println("📩 Nhận: " + msg);

            String[] parts = msg.split("\\|");
            sendEmail(parts[0], parts[1], parts[2]);
        };

        channel.basicConsume(
                RabbitMQConfig.QUEUE_NAME,
                true,
                callback,
                consumerTag -> {}
        );
    }

    private static void sendEmail(String to, String subject, String content) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    FROM_EMAIL,
                                    APP_PASSWORD
                            );
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("✅ Email gửi thành công: " + to);

        } catch (Exception e) {
            System.err.println("❌ Gửi email thất bại");
            e.printStackTrace();
        }
    }
}
