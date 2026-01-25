package iuh.fit.bai2.auth;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(
            @RequestParam String username,
            @RequestParam String password) {

        if (username.equals("admin") && password.equals("123")) {
            return Map.of(
                    "token", jwtUtil.generateToken(username, "ADMIN"),
                    "refreshToken", jwtUtil.generateRefreshToken("admin")
            );
        }

        if (username.equals("user") && password.equals("123")) {
            return Map.of(
                    "token", jwtUtil.generateToken(username, "USER")
            );
        }

        throw new RuntimeException("Sai tài khoản");
    }


}
