package com.mohinhphanlop.projectthree.Controllers.Admin;

import java.util.Optional;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
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
import com.mohinhphanlop.projectthree.Services.ThietBiService;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    private ThongTinSDService ttsdService;

    @Autowired
    private ThietBiService tbSerive;

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

    @GetMapping("/muontrathietbi")
    public String MuonTraThietBi() {
        return "admin/muontrathietbi";
    }

    @PostMapping("/trathietbi")
    public ResponseEntity<?> TraThietBi(Model model, @RequestBody MultiValueMap<String, String> data) {
        String maTB = data.getFirst("maTB");
        try {
            ThietBi thietbi = tbSerive.FindByID(maTB);
            if (thietbi == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thiết bị không tồn tại!");
            } else {
                ThongTinSD ttsd = null;
                for (ThongTinSD tt : thietbi.getDS_ThongTinSD()) {
                    if (tt.getTGMuon() != null && tt.getTGTra() == null) {
                        ttsd = tt;
                    }
                }
                if (ttsd == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiết bị này dường như chưa được mượn!");
                } else {
                    if (ttsdService.TraThietBi(ttsd) != null) {
                        return ResponseEntity.status(HttpStatus.OK).body("Trả thiết bị thành công!");
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi chưa thể trả thiết bị này!");
                }
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi dữ liệu nhập");
        }
    }

    @PostMapping("/muonthietbi")
    public ResponseEntity<?> MuonThietBi(Model model, @RequestBody MultiValueMap<String, String> data) {
        String maTV = data.getFirst("maTV");
        String maTB = data.getFirst("maTB");
        if (maTV == null || maTB == null || maTV.isEmpty() || maTB.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi dữ liệu nhập không phù hợp!");
        }
        try {
            Optional<ThanhVien> tv = tvService.FindThanhVienById(Long.valueOf(maTV));
            if (tv.isPresent()) {
                ThietBi tb = tbSerive.FindByID(maTB);
                if (tb != null) {
                    List<XuLy> listXuLy = tv.get().getDS_XuLy();
                    listXuLy = listXuLy
                            .stream()
                            .filter(item -> item.getTrangThaiXL() == 0)
                            .collect(Collectors.toList());
                    int tongXuLy = listXuLy.size();
                    if (tongXuLy == 0) {
                        ThongTinSD ttsd = ttsdService.MuonThietBi(tv.get(), tb);
                        if (ttsd != null) {
                            return ResponseEntity.ok("Mượn thiết bị thành công");
                        } else {
                            ttsd = ttsdService.LayTTSD(tv.get(), tb);
                            if (ttsd != null && ttsdService.MuonThietBiDaDat(ttsd) != null) {
                                return ResponseEntity.ok("Mượn thiết bị đã đặt thành công");
                            }
                            return ResponseEntity.status(502)
                                    .body("Thiết bị này đã được mượn hoặc đã được đặt chỗ trước");
                        }
                    } else {
                        return ResponseEntity.status(502).body(listXuLy);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thiết bị không tồn tại");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thành viên không tồn tại");
            }
        } catch (NumberFormatException ex) {
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi dữ liệu nhập không phù hợp!");
    }
}
