package com.mohinhphanlop.projectthree.Controllers.Admin;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    private ThongTinSDService ttsdService;

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

    @PostMapping("/vaokhuhoctap")
    public ResponseEntity<?> VaoKhuHocTap(@RequestBody MultiValueMap<String, String> maTV_JSON) {
        int id;
        try {
            id = Integer.parseInt(maTV_JSON.getFirst("id"));
            Optional<ThanhVien> tv = tvService.FindThanhVienById(id);
            if (tv.isPresent()) {
                List<XuLy> listXuLy = tv.get().getDS_XuLy();
                listXuLy = listXuLy
                        .stream()
                        .filter(item -> item.getTrangThaiXL() == 0)
                        .collect(Collectors.toList());
                int tongXuLy = listXuLy.size();
                if (tongXuLy == 0) {
                    ThongTinSD ttsd = ttsdService.CreateNewInfo(id);
                    if (ttsd != null) {
                        Map<String, Object> responseMap = new HashMap<>();
                        responseMap.put("ttsd", ttsd.getTGVao());
                        responseMap.put("tv", tv.get());
                        return ResponseEntity.ok(responseMap);
                    } else {
                        return ResponseEntity.status(502).body("Vào khu học tập không thành công");
                    }
                } else {
                    return ResponseEntity.status(502).body(listXuLy);
                }
            } else {
                return ResponseEntity.status(404).body("Mã thành viên không tồn tại");
            }
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(500).body("Vui lòng nhập dữ liệu hợp lệ");
        }
    }
}
