package iuh.fit.bai2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "Chào ADMIN 👑";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "Chào USER 👤";
    }
}
