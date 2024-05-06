package com.mohinhphanlop.projectthree.Controllers.Admin;

import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.XuLyService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quantri/xuly")
public class XuLyController {
    @Autowired
    private XuLyService xuLyService;

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

    @GetMapping("{id}")
    public String getDetail(Model model, @PathVariable Optional<Integer> id) {
        model.addAttribute("xuLy", xuLyService.findById(id.orElse(-1)));
        return "admin/xuly/chinhsua";
    }
}
