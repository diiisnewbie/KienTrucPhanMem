package iuh.fit.springbootdocker.controller;

import iuh.fit.springbootdocker.model.User;
import iuh.fit.springbootdocker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET tất cả user
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    // GET user theo id
    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userService.getById(id);
    }

    // POST thêm user mới
    @PostMapping
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    // DELETE xóa user
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        boolean removed = userService.delete(id);
        return removed ? "Xóa thành công!" : "Không tìm thấy user!";
    }
}