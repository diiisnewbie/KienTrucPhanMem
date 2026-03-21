package iuh.fit.login.controllers;

import iuh.fit.login.dto.LoginDTO;
import iuh.fit.login.dto.UserObject;
import iuh.fit.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // POST /api/login → đăng nhập
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            UserObject user = loginService.login(dto);
            return ResponseEntity.ok(Map.of(
                "message",  "Đăng nhập thành công!",
                "username", user.getUsername(),
                "email",    user.getEmail(),
                "fullName", user.getFullName(),
                "role",     user.getRole()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}