package com.mohinhphanlop.projectthree.Controllers;

import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;
import com.mohinhphanlop.projectthree.Services.XuLyService;

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
    @Autowired
    private ThongTinSDService ttSDService;
    @Autowired
    private XuLyService xuLyService;

    @GetMapping("")
    public String get() {
        return "redirect:/hoso/";
    }

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
        String sdt = formData.getFirst("sdt");
        String password = formData.getFirst("password");
        String new_password = formData.getFirst("new_password");

        // Lấy tên đăng nhập của người dùng
        String username = session.getAttribute("username").toString();

        // Kiểm tra đủ điều kiện để cập nhật
        boolean check = true;

        ThanhVien tv = tvService.getByUsernameOrEmail(username);
        // Trường hợp đổi email
        if ((tvService.CheckNotTheSameEmail(tv, email))) {
            // Trường hợp email nhập vào là email mới
            if (tvService.CheckEmailExists(email)) {
                // email đã tồn tại
                model.addAttribute("error", "Email đã được sử dụng bới một tài khoản khác!");
                check = false;
            }
        }
        // Trường hợp đổi sdt
        if ((tvService.CheckNotTheSameSDT(tv, sdt))) {
            // Trường hợp email nhập vào là sdt mới
            if (tvService.CheckSDTExists(sdt)) {
                // sdt đã tồn tại
                model.addAttribute("error", "SĐT đã được sử dụng bới một tài khoản khác!");
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

        // Nếu có đủ điều kiện và mật khẩu chính xác thì cập nhật thông tin
        if (check && tvService.getByUsernameOrEmail(username).getPassword().equals(password)) {
            if (!new_password.isEmpty())
                password = new_password;

            tvService.UpdateThanhVien(username, email, password, sdt);
            session.setAttribute("pw", password);
            model.addAttribute("success", "Cập nhật thông tin thành công!");
        } else if (model.getAttribute("error") == null)
            model.addAttribute("error", "Mật khẩu không chính xác!");

        // Hiển thị thông tin thành viên vừa cập nhật
        model.addAttribute("ThanhVien", tvService.getByUsernameOrEmail(username));
        return "user";
    }

    @GetMapping("/trangthaivipham")
    public String getTrangThaiViPham(HttpSession session, Model model, Pageable pageable) {
        String username = session.getAttribute("username").toString();
        Page<XuLy> list = xuLyService.findAllByThanhVienId(pageable, username);

        model.addAttribute("listXuLy", list);

        int totalPages = list.getTotalPages();
        if (totalPages > 0) {
            int[] pageNumbers = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pageNumbers[i] = i + 1;
            }
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "user_violations";
    }

    @GetMapping("/thietbidatcho")
    public String getThietBiDatCho(HttpSession session, Model model) {
        ttSDService.RemoveAllTGDatchoOver1Hour();
        String username = session.getAttribute("username").toString();
        model.addAttribute("listThongTinSD", tvService.GetListThongTinSDDatchoFrom(username));
        return "user_reservations";
    }

    @GetMapping("/thietbidangmuon")
    public String getThietBiDangMuon(HttpSession session, Model model) {
        String username = session.getAttribute("username").toString();
        model.addAttribute("listThongTinSD", tvService.GetListThongTinSDDangMuonFrom(username));
        return "user_borrowing";
    }

}
