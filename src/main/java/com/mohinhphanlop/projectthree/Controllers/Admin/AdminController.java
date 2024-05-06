package com.mohinhphanlop.projectthree.Controllers.Admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/quantri")
public class AdminController {
    @GetMapping("")
    public String getIndex() {
        return "admin/index";
    }

    @Autowired
    private ThongTinSDService ttSDService;

    @GetMapping("/datcho")
    public String getDatCho(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
            @RequestParam("matb") Optional<String> matb, @RequestParam("tentb") Optional<String> tentb,
            @RequestParam("matv") Optional<String> matv, @RequestParam("hoten") Optional<String> hoten,
            @RequestParam("tgdatcho") Optional<String> tgdatcho) {

        ttSDService.RemoveAllTGDatchoOver1Hour();

        model.addAttribute("matb", matb.orElse(""));
        model.addAttribute("tentb", tentb.orElse(""));
        model.addAttribute("matv", matv.orElse(""));
        model.addAttribute("hoten", hoten.orElse(""));
        model.addAttribute("tgdatcho", tgdatcho.orElse(""));

        Page<ThongTinSD> list;
        if (!matb.orElse("").isEmpty() || !tentb.orElse("").isEmpty() || !matv.orElse("").isEmpty()
                || !hoten.orElse("").isEmpty() || !tgdatcho.orElse("").isEmpty())
            list = ttSDService.findAllBytGDatchoNotNull(pageable, matb.orElse(""), tentb.orElse(""), matv.orElse(""),
                    hoten.orElse(""), tgdatcho.orElse(""));
        else
            list = ttSDService.findAllBytGDatchoNotNull(pageable);

        model.addAttribute("data", list);

        // Add total pages

        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            int[] pageNumbers = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pageNumbers[i] = i + 1;
            }
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "admin/datcho";
    }

    @GetMapping("/datcho/{id}/xoa")
    public String getXoaDatCho(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("MaTT", id);
        return "admin/xoadatcho";
    }

    @PostMapping("/datcho/{id}/xoa")
    public String postXoaDatCho(Model model, @PathVariable("id") Integer id) {

        ThongTinSD thongTinSD = ttSDService.findById(id);
        if (thongTinSD == null) {
            model.addAttribute("error", "Xoá thất bại do không tìm thấy!");
            return "admin/xoadatcho";
        }

        if (thongTinSD.getTGDatcho() == null) {
            model.addAttribute("error", "Xoá thất bại do đây không phải thông tin đặt chỗ!");
            return "admin/xoadatcho";
        }

        if (ttSDService.deleteThongTinSD(id))
            model.addAttribute("success", "Xoá thành công");
        else
            model.addAttribute("error", "Xoá thất bại");
        return "admin/xoadatcho";
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
