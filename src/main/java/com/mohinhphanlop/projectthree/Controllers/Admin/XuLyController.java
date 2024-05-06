package com.mohinhphanlop.projectthree.Controllers.Admin;

import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.ThanhVienService;
import com.mohinhphanlop.projectthree.Services.XuLyService;
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
@RequestMapping("/quantri/xuly")
public class XuLyController {
    @Autowired
    private XuLyService xuLyService;
    @Autowired
    private ThanhVienService tvService;

    @GetMapping("")
    public String getIndex(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
            @RequestParam("matv") Optional<String> matv, @RequestParam("hoten") Optional<String> hoten,
            @RequestParam("hinhthucxl") Optional<String> hinhthucxl, @RequestParam("ngayxl") Optional<String> ngayxl,
            @RequestParam("sotien") Optional<String> sotien) {

        model.addAttribute("matv", matv.orElse(""));
        model.addAttribute("hoten", hoten.orElse(""));
        model.addAttribute("hinhthucxl", hinhthucxl.orElse(""));
        model.addAttribute("ngayxl", ngayxl.orElse(""));
        model.addAttribute("sotien", sotien.orElse(""));

        Page<XuLy> list;
        if (!matv.orElse("").isEmpty() || !hoten.orElse("").isEmpty() || !hinhthucxl.orElse("").isEmpty()
                || !ngayxl.orElse("").isEmpty() || !sotien.orElse("").isEmpty())
            list = xuLyService.findAll(pageable, matv.orElse(""), sotien.orElse(""), hoten.orElse(""),
                    hinhthucxl.orElse(""), ngayxl.orElse(""));
        else
            list = xuLyService.findAll(pageable);

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

        return "admin/xuly/index";
    }

    @GetMapping("/them")
    public String getAddNew(Model model) {
        model.addAttribute("listTV", tvService.GetList());
        return "admin/xuly/them";
    }

    @PostMapping("/them")
    public String postAddNew(Model model,
            @RequestBody MultiValueMap<String, String> formData) {

        String matv = formData.getFirst("matv");
        String hinhthucxl = formData.getFirst("hinhthucxl");
        String sotien = formData.getFirst("sotien");
        String ngayxl = formData.getFirst("ngayxl");
        String trangthaixl = formData.getFirst("trangthaixl");

        if (xuLyService.addXuLy(matv, hinhthucxl, sotien, ngayxl, trangthaixl) != null) {
            model.addAttribute("success", "Thêm mới xử lý vi phạm thành công!");
        } else
            model.addAttribute("error", "Thêm mới xử lý vi phạm thất bại!");

        return "admin/xuly/them";
    }

    @GetMapping("{id}")
    public String getDetail(Model model, @PathVariable Optional<Integer> id) {
        model.addAttribute("xuLy", xuLyService.findById(id.orElse(-1)));
        return "admin/xuly/chinhsua";
    }

    @PostMapping("{id}")
    public String postDetail(Model model, @PathVariable Optional<Integer> id,
            @RequestBody MultiValueMap<String, String> formData) {

        String hinhthucxl = formData.getFirst("hinhthucxl");
        String sotien = formData.getFirst("sotien");
        String ngayxl = formData.getFirst("ngayxl");
        String trangthaixl = formData.getFirst("trangthaixl");

        if (xuLyService.updateXuLy(id.orElse(-1), hinhthucxl, sotien,
                ngayxl, trangthaixl) != null) {
            model.addAttribute("success", "Cập nhật xử lý vi phạm thành công!");
        } else
            model.addAttribute("error", "Cập nhật xử lý vi phạm thất bại!");

        model.addAttribute("xuLy", xuLyService.findById(id.orElse(-1)));
        return "admin/xuly/chinhsua";
    }

    @GetMapping("{id}/xoa")
    public String getDelete(Model model, @PathVariable Optional<Integer> id) {
        model.addAttribute("xuLy", xuLyService.findById(id.orElse(-1)));
        return "admin/xuly/xoa";
    }

    @PostMapping("{id}/xoa")
    public String postDelete(Model model, @PathVariable Optional<Integer> id) {
        if (xuLyService.deleteXuLy(id.orElse(-1))) {
            model.addAttribute("success", "Xoá thành công");
        } else
            model.addAttribute("error", "Xoá thất bại");
        return "admin/xuly/xoa";
    }
}
