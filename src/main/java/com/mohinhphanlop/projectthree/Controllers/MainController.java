/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.mohinhphanlop.projectthree.Controllers;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Services.EmailService;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThietBiService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

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
        model.addAttribute("data", list);
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

    @Autowired
    private EmailService emailService;

    @GetMapping("/quenmatkhau")
    public String getQuenMatKhau(@RequestParam("code") Optional<String> code, HttpSession session, Model model) {
        if (code.isPresent()) {
            // Nếu có code
            String email_uuid = session.getAttribute("email_uuid") == null ? ""
                    : session.getAttribute("email_uuid").toString();
            if (email_uuid.equals(code.get())) {
                model.addAttribute("code", true);
                model.addAttribute("codeValue", email_uuid);
            } else {
                model.addAttribute("code", false);
                model.addAttribute("error", "Mã xác thực không hợp lệ!");
            }
        }
        return "forgot_password";
    }

    /**
     * Phương thức xử lý yêu cầu thay đổi mật khẩu từ phía người dùng.
     *
     * @param code     Mã xác thực gửi từ email.
     * @param formData Dữ liệu từ form.
     * @param model    Model.
     * @param request  HttpServletRequest.
     * @param session  HttpSession.
     * @return Trang web.
     */
    @PostMapping("/quenmatkhau")
    public String postQuenMatKhau(@RequestBody MultiValueMap<String, String> formData,
            Model model, HttpServletRequest request, HttpSession session) {

        String code = formData.getFirst("code") == null ? "" : formData.getFirst("code");
        System.out.println(code);
        if (!code.isEmpty()) { // Nếu có code
            String email_uuid = session.getAttribute("email_uuid") == null ? ""
                    : session.getAttribute("email_uuid").toString(); // Lấy email_uuid từ HttpSession
            if (email_uuid.equals(code)) { // Kiểm tra đúng với mã xác thực trong email

                String new_password = formData.getFirst("new_password"); // Lấy mật khẩu mới từ form
                String confirm_password = formData.getFirst("confirm_password"); // Lấy mật khẩu xác nhận từ form
                if (new_password.equals(confirm_password)) { // Kiểm tra mật khẩu xác nhận trùng với mật khẩu mới
                    String requestedUsername = session
                            .getAttribute("requested_username")
                            .toString(); // Lấy tên đăng
                                         // nhập từ
                                         // HttpSession
                    if (tvService.UpdateThanhVien(requestedUsername, // Thực hiện cập nhật mật khẩu
                            tvService.getByUsernameOrEmail(requestedUsername).getEmail(), new_password) != null) {
                        model.addAttribute("success", "Thay đổi mật khẩu thành công!");
                        session.removeAttribute("email_uuid");
                        session.removeAttribute("requested_username");
                    } else
                        model.addAttribute("error", "Không thể thay đổi mật khẩu!");
                } else
                    model.addAttribute("error", "Mật khẩu xác nhận không khớp!");

            } else {
                model.addAttribute("error", "Mã xác thực không hợp lệ!");
            }
            return "forgot_password";

        } else { // Nếu không có code
            String requestedUsername = formData.getFirst("username"); // Lấy tên đăng nhập từ form
            String email_uuid = session.getAttribute("email_uuid") == null ? ""
                    : session.getAttribute("email_uuid").toString(); // Lấy email_uuid từ HttpSession

            if (tvService.CheckUsernameIsRegistered(requestedUsername)) { // Kiểm tra tài khoản đã đăng ký
                if (email_uuid.isEmpty()) { // chỉ gửi nếu email_uuid chưa set
                    String email = tvService.getByUsernameOrEmail(requestedUsername).getEmail(); // Lấy email từ hệ
                                                                                                 // thống
                    String uuid = emailService.GuiEmailQuenMatKhau(email, request); // Gửi email
                    session.setAttribute("email_uuid", uuid); // Thêm email_uuid vào HttpSession
                    session.setAttribute("requested_username", requestedUsername); // Thêm tên đăng nhập vào HttpSession
                    model.addAttribute("success", "Đã gửi email xác thực, hãy kiểm tra hộp thư của bạn!");
                } else {
                    model.addAttribute("error", "Đã gửi email xác thực, hãy kiểm tra hộp thư của bạn!");
                }
            } else {
                model.addAttribute("error", "Tài khoản này chưa đăng ký thành viên hoặc không tồn tại trong hệ thống.");
            }
            return "forgot_password";
        }
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
