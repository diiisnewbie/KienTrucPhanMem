package iuh.fit.login.service;

import iuh.fit.login.dto.LoginDTO;
import iuh.fit.login.dto.UserObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${register.service.url}")
    private String registerServiceUrl;

    // =============================================
    // LOGIN → gọi register-service để getObject
    // =============================================
    public UserObject login(LoginDTO dto) {
        String url = registerServiceUrl + "/api/register/getObject/" + dto.getUsername();

        UserObject userObject;

        try {
            // Gọi sang register-service để lấy object User
            userObject = restTemplate.getForObject(url, UserObject.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("User chưa đăng ký hoặc không tồn tại!");
        }

        if (userObject == null) {
            throw new RuntimeException("Không lấy được thông tin user!");
        }

        // Kiểm tra password với BCrypt
        if (!passwordEncoder.matches(dto.getPassword(), userObject.getPassword())) {
            throw new RuntimeException("Password không đúng!");
        }

        return userObject;
    }
}