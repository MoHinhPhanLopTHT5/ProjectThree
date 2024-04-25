package com.mohinhphanlop.projectthree.Controllers;

import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mohinhphanlop.projectthree.Services.ThanhVienService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/hoso")
public class HoSoController {
    @Autowired
    private ThanhVienService tvService;

    @GetMapping("/")
    public String getCapNhat(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String username = session.getAttribute("username").toString();
        model.addAttribute("ThanhVien", tvService.getByUsernameOrEmail(username));
        return "user";
    }

    /**
     * Cập nhật thông tin cá nhân
     * 
     * @param formData dữ liệu POST từ Form
     * @param model    Model
     * @param request  HttpServletRequest
     * @param session  HttpSession
     * @return Trang giao diện thành viên
     */
    @PostMapping("/")
    public String postCapNhat(@RequestBody MultiValueMap<String, String> formData, Model model,
            HttpServletRequest request, HttpSession session) {

        // Khởi tạo session nếu chưa có
        if (session == null)
            session = request.getSession();

        // Lấy dữ liệu từ form
        String email = formData.getFirst("email");
        String password = formData.getFirst("password");
        String new_password = formData.getFirst("new_password");

        // Lấy tên đăng nhập của người dùng
        String username = session.getAttribute("username").toString();

        // Kiểm tra đủ điều kiện để cập nhật
        boolean check = true;

        // Trường hợp đổi email
        if ((tvService.CheckNotTheSameEmail(tvService.getByUsernameOrEmail(username), email))) {
            // Trường hợp email nhập vào là email mới
            if (tvService.CheckEmailExists(email)) {
                // email đã tồn tại
                model.addAttribute("error", "Email đã được sử dụng bới một tài khoản khác!");
                check = false;
            }
        }

        // Trường hợp đổi mật khẩu
        if (!new_password.isEmpty()) {
            // cập nhật mật khẩu
            if (new_password.equals(password)) {
                model.addAttribute("error", "Mật khẩu mới không được trùng với mật khẩu cũ!");
                check = false;
            }
        }

        // Nếu có đủ điều kiện thì cập nhật thông tin
        if (check) {
            if (!new_password.isEmpty())
                password = new_password;

            tvService.UpdateThanhVien(username, email, password);
            session.setAttribute("pw", password);
            model.addAttribute("success", "Cập nhật thông tin thành công!");
        }

        // Hiển thị thông tin thành viên vừa cập nhật
        model.addAttribute("ThanhVien", tvService.getByUsernameOrEmail(username));
        return "user";
    }

}