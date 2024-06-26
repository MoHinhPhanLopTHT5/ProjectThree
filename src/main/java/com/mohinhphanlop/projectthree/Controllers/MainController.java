/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.mohinhphanlop.projectthree.Controllers;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.DateService;
import com.mohinhphanlop.projectthree.Services.EmailService;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.ThietBiService;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;
import com.mohinhphanlop.projectthree.Services.XuLyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ThanhVienService tvService;
    @Autowired
    private ThietBiService tbService;
    @Autowired
    private ThongTinSDService ttSDService;
    @Autowired
    private XuLyService xuLyService;

    public static Integer TryParseInt(String txt) {
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    // url parameters
    @GetMapping("/")
    public String homepage(Model model, @RequestParam("page") Optional<Integer> page,
            @RequestParam("tenTB") Optional<String> tenTB,
            Pageable pageable, HttpSession session) {

        ttSDService.RemoveAllTGDatchoOver1Hour();

        Iterable<XuLy> xuLy = xuLyService.findByThanhVienId(TryParseInt(session.getAttribute("username").toString()));

        for (XuLy item : xuLy) {
            model.addAttribute("error", "Bạn hiện không thể đặt chỗ do đang trong thời gian xử lý vi phạm!");
            model.addAttribute("xuLy", item);
            return "suspended";
        }

        String TenTB = tenTB.orElse("");
        Page<ThietBi> list = tbService.GetList(TenTB, pageable);
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

        model.addAttribute("TenTB", TenTB);

        return "index";
        // return "index2";
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
        session.invalidate();
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
            Integer username = tvService.getByUsernameOrEmail(requestedUsernameOrEmail).getMaTV();
            session.setAttribute("username", username);
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
                    ThanhVien tv = tvService.getByUsernameOrEmail(requestedUsername);
                    if (tvService.UpdateThanhVien(requestedUsername, // Thực hiện cập nhật mật khẩu
                            tv.getEmail(), new_password, tv.getSDT()) != null) {
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
        String sdt = formData.getFirst("sdt");

        Integer maTV = TryParseInt(requestedUsername);

        model.addAttribute("username", requestedUsername);
        model.addAttribute("email", email);
        model.addAttribute("fullname", fullname);
        model.addAttribute("sdt", sdt);

        String password = formData.getFirst("password");
        String confirm_password = formData.getFirst("confirm_password");

        boolean check = true;

        if (tvService.CheckEmailExists(email)) {
            // Email đã được sử dụng
            model.addAttribute("error", "Email đã tồn tại trong cơ sở dữ liệu, hãy sử dụng email khác.");
            check = false;
        }

        if (tvService.CheckSDTExists(sdt)) {
            // SDT đã được sử dụng
            model.addAttribute("error", "SĐT đã tồn tại trong cơ sở dữ liệu, hãy sử dụng SĐT khác.");
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
                if (tvService.UpdateThanhVien(requestedUsername, email, password, sdt) != null) {
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
                if (tvService.CreateThanhVien(requestedUsername, email, sdt, password, fullname) != null) {
                    model.addAttribute("success", "Tạo tài khoản thành công, vui lòng đăng nhập!");
                } else
                    model.addAttribute("error", "Lưu thông tin thất bại.");
            }
        }

        return "register";
    }

    // Khu vực đặt chỗ

    @GetMapping("/datcho")
    public String getDatChoIndex(Model model) {
        return "redirect:/";
    }

    @GetMapping("/datcho/{id}")
    public String getDatCho(@PathVariable("id") ThietBi thietBi, Model model, HttpSession session) {
        ttSDService.RemoveAllTGDatchoOver1Hour();

        Iterable<XuLy> xuLyList = xuLyService
                .findByThanhVienId(TryParseInt(session.getAttribute("username").toString()));

        for (XuLy xuLy : xuLyList) {
            model.addAttribute("error", "Bạn hiện không thể đặt chỗ do đang trong thời gian xử lý vi phạm!");
            model.addAttribute("xuLy", xuLy);
            return "suspended";
        }

        Iterable<ThongTinSD> listTTSD = ttSDService.findByMaTBAndtGDatchoNotNull(thietBi.getMaTB());

        // Chỉ thêm vào nếu ngày đặt chỗ là hôm nay
        for (ThongTinSD ttsd : listTTSD) {
            LocalDate localDateNow = DateService.dateTypeToLocalDateType(new Date());
            LocalDate localDatetGDatcho = DateService.dateTypeToLocalDateType(ttsd.getTGDatcho());
            if (localDateNow.equals(localDatetGDatcho)) {
                model.addAttribute("ttsd", ttsd);
                model.addAttribute("tgDatChoHomNay", true);
                break;
            }
        }

        model.addAttribute("username", session.getAttribute("username").toString());
        model.addAttribute("thietBi", thietBi);
        return "reservation";
    }

    @GetMapping("/datcho/{id}/xoa")
    public String getXoaDatCho(Model model, HttpSession session, @PathVariable("id") Integer id) {

        ThongTinSD ttsd = ttSDService.findBymaTTAndtGDatchoNotNull(id);
        if (ttsd != null) {
            if (ttsd.getThanhvien().getMaTV().toString().equals(session.getAttribute("username").toString()))
                model.addAttribute("MaTT", ttsd.getMaTT());
            else
                model.addAttribute("error", "Bạn không có quyền xoá thông tin này!");
        } else
            model.addAttribute("error", "Thông tin sử dụng này không phải là thông tin đặt chỗ!");
        return "cancel_reservation";
    }

    @PostMapping("/datcho/{id}/xoa")
    public String postXoaDatCho(Model model, HttpSession session, @PathVariable("id") Integer id) {
        ThongTinSD ttsd = ttSDService.findBymaTTAndtGDatchoNotNull(id);
        if (ttsd != null) {
            if (ttsd.getThanhvien().getMaTV().toString().equals(session.getAttribute("username").toString())) {
                if (ttSDService.deleteThongTinSD(ttsd.getMaTT()))
                    model.addAttribute("success", "Xóa thành công!");
                else
                    model.addAttribute("error", "Xoá thất bại!");
            } else
                model.addAttribute("error", "Bạn không có quyền xoá thông tin này!");
        } else
            model.addAttribute("error", "Thông tin sử dụng này không phải là thông tin đặt chỗ!");
        return "cancel_reservation";
    }

    @PostMapping("/datcho/{id}")
    public String postDatCho(HttpSession session, @PathVariable("id") ThietBi thietBi,
            @RequestBody MultiValueMap<String, String> formData,
            Model model) {

        String date = formData.getFirst("date");
        String MaTV = session.getAttribute("username").toString();
        boolean check = true;
        if (ttSDService.CheckTrangThaiDatCho(thietBi.getMaTB().toString(), date) == 0) {
            model.addAttribute("error",
                    "Ngày đã nhập là ngày trong quá khứ hoặc thiết bị này đã được đặt chỗ vào ngày bạn nhập, hãy chọn một ngày khác!");
            check = false;
        } else if (ttSDService.CheckTrangThaiDatCho(thietBi.getMaTB().toString(), date) == -1) {
            model.addAttribute("error", "Thiết bị đang được mượn, không thể đặt chỗ!");
            check = false;
        }

        if (check) {
            if (ttSDService.CreateThoiGianDatCho(MaTV, thietBi.getMaTB().toString(), date) != null)
                model.addAttribute("success", "Đặt chỗ mượn thành công!");
            else
                model.addAttribute("error", "Đặt chỗ mượn thất bại do lỗi phía máy chủ!");
        }

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
