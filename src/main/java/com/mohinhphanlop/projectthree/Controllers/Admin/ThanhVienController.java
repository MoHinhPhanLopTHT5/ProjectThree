package com.mohinhphanlop.projectthree.Controllers.Admin;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quantri/thanhvien")
public class ThanhVienController {

    @Autowired
    private ThanhVienService tvService;

    private ArrayList<String> listKhoa = new ArrayList<>();
    private ArrayList<String> listNganh = new ArrayList<>();

    @GetMapping("")
    public String getIndex(Model model) {
        Iterable<ThanhVien> list = tvService.GetList();
        model.addAttribute("members", list);
        return "admin/thanhvien/index";
    }

    @GetMapping("/them")
    public String ThemThanhVienForm(Model model) {
        ThanhVien thanhVien = new ThanhVien();
        model.addAttribute("member", thanhVien);
        return "admin/thanhvien/thanhvien_them";
    }

    @PostMapping("/them")
    public String ThemThanhVien(Model model, @ModelAttribute("member") ThanhVien thanhVien) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        String maTV = "11" + sdf.format(now) + thanhVien.getNganh().split(": ")[1] + "0000";
        thanhVien.setNganh(thanhVien.getNganh().split(": ")[0]);
        thanhVien.setMaTV(Integer.parseInt(maTV));
        thanhVien.setPassword(thanhVien.getSDT());
        String result = tvService.SaveThanhVien(thanhVien);
        if (result.equalsIgnoreCase("Thêm thành công")) {
            return "redirect:/quantri/thanhvien";
        }
        model.addAttribute("error", result);
        return "admin/thanhvien/thanhvien_them";
    }

    @GetMapping("/{id}/xem")
    public String XemThanhVien(Model model, @PathVariable("id") long id) {
        Optional<ThanhVien> thanhvien = tvService.FindThanhVienById(id);
        thanhvien.ifPresent(tv -> {
            model.addAttribute("member", thanhvien);
            List<XuLy> listXuLy = tvService.GetListXuLyFromMember(id);
            if (listXuLy != null) {
                model.addAttribute("xuLy", listXuLy);
            }
        });
        return "admin/thanhvien/thanhvien_xem";
    }

    @GetMapping("/{id}/sua")
    public String SuaThanhVienForm(Model model, @PathVariable("id") long id) {
        Optional<ThanhVien> thanhvien = tvService.FindThanhVienById(id);
        thanhvien.ifPresent(tv -> model.addAttribute("member", thanhvien));
        return "admin/thanhvien/thanhvien_sua";
    }

    @PostMapping("/capnhat")
    public String SuaThanhVien(Model model, @ModelAttribute("member") ThanhVien thanhVien, @RequestBody MultiValueMap<String, String> formData) {
        Optional<ThanhVien> tv = tvService.FindThanhVienById(thanhVien.getMaTV());
        if (tv.isPresent()) {
            thanhVien.setPassword(tv.get().getPassword());
            tvService.UpdateThanhVien(thanhVien);
            return "redirect:/quantri/thanhvien";
        }
        return "/admin/thanhvien/thanhvien_sua";
    }

    @PostMapping("/timkiem")
    public String TimKiemThanhVien(Model model, @RequestBody MultiValueMap<String, String> data) {
        String keySearch = data.getFirst("keySearch");
        Iterable<ThanhVien> list = tvService.GetList();
        List<ThanhVien> listFilter = new ArrayList<>();
        for (ThanhVien tv : list) {
            if (tv.getHoTen().toUpperCase().contains(keySearch.toUpperCase())) {
                listFilter.add(tv);
            }
        }
        model.addAttribute("members", listFilter);
        if (listFilter.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy thành viên nào!");
        }
        return "admin/thanhvien/index";
    }

    @PostMapping("/{id}/xoa")
    public ResponseEntity<String> deleteThanhVien(Model model, @PathVariable("id") long id) {
        String result = tvService.Delete(id);
        if (result.equals("Xóa thành viên này thành công!")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(500).body(result);
    }

    @PostMapping("/xoanhieu")
    public ResponseEntity<String> deleteNhieuThanhVien(@RequestBody MultiValueMap<String, String> yearJSON) {
        String result = tvService.DeleteMany(yearJSON.getFirst("year").substring(2));
        if (result.equals("Xóa thành công")) {
            return ResponseEntity.ok("Xóa thành công");
        }
        return ResponseEntity.status(500).body("Xóa không thành công");
    }
}
