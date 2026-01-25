package iuh.fit.trannhatduy_22699741_tuan01;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(String message) throws InterruptedException {
        System.out.println("📩 Nhận job: " + message);

        // giả lập gửi email chậm
        Thread.sleep(5000);

        System.out.println("✅ Email đã gửi cho: " + message);
    }
}
