package iuh.fit.bai3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestParam String email) {
        return orderService.createOrder(email);
    }

    @PostMapping("/mq")
    public Order createOrderUseMQ(@RequestParam String email) {
        return orderService.createOrder(email);
    }
}
