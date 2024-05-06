package com.mohinhphanlop.projectthree.Controllers.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mohinhphanlop.projectthree.Services.ThanhVienService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/quantri")
public class AdminController {
    @GetMapping("")
    public String getIndex() {
        return "admin/index";
    }

    @GetMapping("/tenpath")
    public String getIndexWithModel(Model model) {
        model.addAttribute("tenModel", "Gia tri model");
        // khi lấy trong template thì dùng ${tenModel}
        // có thể truyền giá trị của một object là model của một bảng, ví dụ ThanhVien
        return "admin/index";
    }

    @Autowired
    private ThanhVienService tvService; // trỏ đến class chứa code BLL (lớp nghiệp vụ)

    // Hàm post mẫu, xử lý thành công thì return template
    // gửi thông báo thành công thì là success
    // thất bại thì là error
    // thông qua model.addAttribute()
    @PostMapping("/path")
    public String postTenHam(@RequestBody MultiValueMap<String, String> formData, Model model,
            HttpServletRequest request, HttpSession session) {

        // Lấy dữ liệu từ form
        String email = formData.getFirst("email");
        String password = formData.getFirst("password");
        String new_password = formData.getFirst("new_password");

        // Lấy tên đăng nhập của người dùng
        String username = session.getAttribute("username").toString();

        // Kiểm tra đủ điều kiện để cập nhật
        boolean check = true;

        // Hiển thị thông tin thành viên vừa cập nhật
        model.addAttribute("ThanhVien", tvService.getByUsernameOrEmail(username));
        return "user";
    }

}
