package iuh.fit.register.service;

import iuh.fit.register.dto.RegisterDTO;
import iuh.fit.register.models.User;
import iuh.fit.register.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Map lưu object User sau khi register (để login-service getObject)
    private final Map<String, User> registeredUsers = new HashMap<>();

    // =============================================
    // REGISTER → lưu DB + lưu vào Map
    // =============================================
    public User register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();

        // 1. Lưu vào SQL Server
        User saved = userRepository.save(user);

        // 2. Lưu vào Map (để login-service lấy qua getObject)
        registeredUsers.put(saved.getUsername(), saved);

        return saved;
    }

    // =============================================
    // GET OBJECT → login-service gọi API này
    // =============================================
    public User getObject(String username) {
        User user = registeredUsers.get(username);
        if (user == null) {
            throw new RuntimeException("User '" + username + "' chưa đăng ký!");
        }
        return user;
    }
}