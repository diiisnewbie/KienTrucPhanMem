package iuh.fit.register.controllers;

import iuh.fit.register.dto.RegisterDTO;
import iuh.fit.register.models.User;
import iuh.fit.register.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    // POST /api/register → đăng ký user mới
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO dto) {
        try {
            User user = registerService.register(dto);
            return ResponseEntity.ok(Map.of(
                "message", "Đăng ký thành công!",
                "username", user.getUsername(),
                "email", user.getEmail(),
                "fullName", user.getFullName()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/register/getObject/{username} → login-service gọi để lấy user object
    @GetMapping("/getObject/{username}")
    public ResponseEntity<?> getObject(@PathVariable String username) {
        try {
            User user = registerService.getObject(username);
            return ResponseEntity.ok(Map.of(
                "id",       user.getId(),
                "username", user.getUsername(),
                "email",    user.getEmail(),
                "fullName", user.getFullName(),
                "password", user.getPassword(),
                "role",     user.getRole()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}