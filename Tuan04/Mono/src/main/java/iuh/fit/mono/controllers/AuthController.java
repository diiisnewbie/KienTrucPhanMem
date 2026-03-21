package iuh.fit.mono.controllers;

import iuh.fit.mono.dto.RegisterDTO;
import iuh.fit.mono.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // =============================================
    // TRANG CHỦ
    // =============================================
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // =============================================
    // LOGIN
    // =============================================
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Username hoặc password không đúng!");
        }
        if (logout != null) {
            model.addAttribute("successMsg", "Đăng xuất thành công!");
        }
        return "login";
    }

    // =============================================
    // REGISTER
    // =============================================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO dto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(dto);
            redirectAttributes.addFlashAttribute("successMsg", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }

    // =============================================
    // DASHBOARD (sau khi login)
    // =============================================
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}