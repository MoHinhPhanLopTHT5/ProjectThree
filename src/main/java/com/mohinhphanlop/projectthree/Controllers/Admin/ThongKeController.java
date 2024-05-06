package com.mohinhphanlop.projectthree.Controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;
import com.mohinhphanlop.projectthree.Services.XuLyService;

@Controller
@RequestMapping("/quantri/thongke")
public class ThongKeController {

    @Autowired
    private ThongTinSDService ttSDService;
    @Autowired
    private XuLyService xuLyService;

    @GetMapping("")
    public String getIndex(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
            @RequestParam("matv") Optional<String> matv, @RequestParam("hoten") Optional<String> hoten,
            @RequestParam("khoa") Optional<String> khoa, @RequestParam("nganh") Optional<String> nganh,
            @RequestParam("tgvao") Optional<String> tgvao) {

        model.addAttribute("matv", matv.orElse(""));
        model.addAttribute("hoten", hoten.orElse(""));
        model.addAttribute("khoa", khoa.orElse(""));
        model.addAttribute("nganh", nganh.orElse(""));
        model.addAttribute("tgvao", tgvao.orElse(""));

        Page<ThongTinSD> list;
        if (matv.orElse("").isEmpty() || hoten.orElse("").isEmpty() || !khoa.orElse("").isEmpty()
                || !nganh.orElse("").isEmpty() || !tgvao.orElse("").isEmpty())
            list = ttSDService.findAllBytGVaoNotNull(pageable, matv.orElse(""), hoten.orElse(""), khoa.orElse(""),
                    nganh.orElse(""), tgvao.orElse(""));
        else
            list = ttSDService.findAllBytGVaoNotNull(pageable);

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

        return "admin/thongke/index";
    }

    @GetMapping("/thietbi")
    public String getThietBi(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
            @RequestParam("matb") Optional<String> matb, @RequestParam("tentb") Optional<String> tentb,
            @RequestParam("tgmuon") Optional<String> tgmuon,
            @RequestParam("tgtra") Optional<String> tgtra) {

        model.addAttribute("matb", matb.orElse(""));
        model.addAttribute("tentb", tentb.orElse(""));
        model.addAttribute("tgmuon", tgmuon.orElse(""));
        model.addAttribute("tgtra", tgtra.orElse(""));

        Page<ThongTinSD> list;
        if (!matb.orElse("").isEmpty() || !tentb.orElse("").isEmpty() || !tgmuon.orElse("").isEmpty()
                || !tgtra.orElse("").isEmpty())
            list = ttSDService.findAllBytGMuonNotNull(pageable, matb.orElse(""), tentb.orElse(""), tgmuon.orElse(""),
                    tgtra.orElse(""));
        else
            list = ttSDService.findAllBytGMuonNotNull(pageable);

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

        return "admin/thongke/thietbi";
    }

    @GetMapping("/datcho")
    public String getDatCho(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
            @RequestParam("matb") Optional<String> matb, @RequestParam("tentb") Optional<String> tentb,
            @RequestParam("matv") Optional<String> matv, @RequestParam("hoten") Optional<String> hoten,
            @RequestParam("tgdatcho") Optional<String> tgdatcho) {

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

        return "admin/thongke/datcho";
    }

    @GetMapping("/vipham")
    public String getVipham(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
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

        return "admin/thongke/vipham";
    }
}
