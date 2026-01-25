package iuh.fit.bai3;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(EmailMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.QUEUE_EMAIL,
                message
        );
    }
}
