package iuh.fit.springbootdocker.service;

import iuh.fit.springbootdocker.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    // Dùng List giả lập database
    private List<User> users = new ArrayList<>(List.of(
        new User(1, "Nguyen Van A", "a@gmail.com"),
        new User(2, "Tran Thi B",  "b@gmail.com")
    ));

    // Lấy tất cả user
    public List<User> getAll() {
        return users;
    }

    // Lấy user theo id
    public User getById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Thêm user
    public User add(User user) {
        users.add(user);
        return user;
    }

    // Xóa user
    public boolean delete(int id) {
        return users.removeIf(u -> u.getId() == id);
    }
}