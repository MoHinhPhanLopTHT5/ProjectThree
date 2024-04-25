/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.mohinhphanlop.projectthree.Controllers;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThietBiService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Admin
 */
@Controller
public class MainController {

    @Autowired
    private ThanhVienService tvService;
    @Autowired
    private ThietBiService tbService;

    public static Integer TryParseInt(String txt) {
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @GetMapping("/")
    public String homepage(Model model) {
        Iterable<ThietBi> list = tbService.GetList();
        // for (ThietBi tb : list) {
        // }
        model.addAttribute("data", list);
        model.addAttribute("loggedin", true);
        return "index";
    }

    // Khu vực đăng nhập

    @GetMapping("/dangnhap")
    public String getDangNhap() {
        return "login";
    }

    @GetMapping("/dangxuat")
    public String getDangXuat(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("pw");
        return "redirect:/dangnhap";
    }

    @PostMapping("/dangnhap")
    public String postDangNhap(HttpSession session, @RequestBody MultiValueMap<String, String> formData,
            Model model) {
        // Get post data from form
        String requestedUsernameOrEmail = formData.getFirst("usernameOrEmail");

        model.addAttribute("usernameOrEmail", requestedUsernameOrEmail);

        String password = formData.getFirst("password");

        if (tvService.CheckLogin(requestedUsernameOrEmail, password)) {
            session.setAttribute("username", requestedUsernameOrEmail);
            session.setAttribute("pw", password);
            return "redirect:/";
        } else
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không hợp lệ.");

        return "login";
    }

    @GetMapping("/quenmatkhau")
    public String getQuenMatKhau() {
        return "forgot_password";
    }

    @PostMapping("/quanmatkhau")
    public String postQuenMatKhau(@RequestBody MultiValueMap<String, String> formData,
            Model model) {

        String requestedUsername = formData.getFirst("username");

        if (tvService.CheckUsernameIsRegistered(requestedUsername)) {

        } else {
            model.addAttribute("error", "Tài khoản này chưa đăng ký thành viên hoặc không tồn tại trong hệ thống.");
        }
        return "forgot_password";
    }

    // Khu vực đăng ký

    @GetMapping("/dangky")
    public String getDangKy() {
        return "register";
    }

    @PostMapping("/dangky")
    public String postDangKy(@RequestBody MultiValueMap<String, String> formData, Model model) {
        // Get post data from form
        String requestedUsername = formData.getFirst("username");
        String email = formData.getFirst("email");
        String fullname = formData.getFirst("fullname");

        Integer maTV = TryParseInt(requestedUsername);

        model.addAttribute("username", requestedUsername);
        model.addAttribute("email", email);
        model.addAttribute("fullname", fullname);

        String password = formData.getFirst("password");
        String confirm_password = formData.getFirst("confirm_password");

        boolean check = true;

        if (tvService.CheckEmailExists(email)) {
            // Email đã được sử dụng
            model.addAttribute("error", "Email đã tồn tại trong cơ sở dữ liệu, hãy sử dụng email khác.");
            check = false;
        }

        if (!password.equals(confirm_password)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp với mật khẩu đã nhập.");
            check = false;
        }

        if (tvService.CheckUsername(requestedUsername)) {
            // Tài khoản đã có trong cơ sở dữ liệu

            if (tvService.CheckUsernameIsRegistered(requestedUsername)) {
                // Thành viên đã đăng ký tài khoản
                model.addAttribute("error", "Thành viên này đã đăng ký tài khoản rồi.");
                check = false;
            }

            if (check) {
                if (tvService.UpdateThanhVien(requestedUsername, email, password) != null) {
                    model.addAttribute("success", "Tạo tài khoản thành công, vui lòng đăng nhập!");
                } else
                    model.addAttribute("error", "Lưu thông tin thất bại.");
            }

        } else {
            // Chưa có tài khoản

            if (maTV == null) {
                model.addAttribute("error", "Mã thành viên nhập vào phải là một dãy số nguyên.");
                check = false;
            }

            if (check) {
                // Thoả mọi điều kiện đặt ra
                if (tvService.CreateThanhVien(requestedUsername, email, password, fullname) != null) {
                    model.addAttribute("success", "Tạo tài khoản thành công, vui lòng đăng nhập!");
                } else
                    model.addAttribute("error", "Lưu thông tin thất bại.");
            }
        }

        return "register";
    }

    // Khu vực đặt chỗ

    @GetMapping("/datcho/{id}")
    public String getDatCho(@PathVariable("id") ThietBi thietBi, Model model) {
        model.addAttribute("thietBi", thietBi);
        return "reservation";
    }

    // Mẫu
    @RequestMapping("/url")
    public String page(Model model) {
        model.addAttribute("attribute", "value");
        return "view.name";
    }

}
