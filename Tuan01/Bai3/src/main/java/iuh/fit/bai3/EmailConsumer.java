package iuh.fit.bai3;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
    public void receiveEmail(EmailMessage message) throws InterruptedException {

        // giả lập xử lý chậm
        Thread.sleep(5000);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(message.getTo());
        mail.setSubject("Xác nhận đơn hàng #" + message.getOrderId());
        mail.setText("Đơn hàng của bạn đã được xử lý thành công!");

        mailSender.send(mail);

        System.out.println("📧 Email đã gửi cho order " + message.getOrderId());
    }
}
