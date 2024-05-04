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
import com.mohinhphanlop.projectthree.Services.ThongTinSDService;

@Controller
@RequestMapping("/quantri/thongke")
public class ThongKeController {

    @Autowired
    private ThongTinSDService ttSDService;

    @GetMapping("")
    public String getIndex(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
    @RequestParam("khoa") Optional<String> khoa, @RequestParam("nganh") Optional<String> nganh, @RequestParam("tgvao") Optional<String> tgvao) {

        model.addAttribute("khoa", khoa.orElse(""));
        model.addAttribute("nganh", nganh.orElse(""));
        model.addAttribute("tgvao", tgvao.orElse(""));

        Page<ThongTinSD> list;
        if (khoa.isPresent() && nganh.isPresent() && !tgvao.orElse("").equals(""))
            list = ttSDService.findAllBytGVaoNotNull(pageable, khoa.orElse(""), nganh.orElse(""), tgvao.orElse(""));
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

    @GetMapping("/thietbimuon")
    public String getTBMuon(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
    @RequestParam("khoa") Optional<String> khoa, @RequestParam("nganh") Optional<String> nganh, @RequestParam("tgvao") Optional<String> tgvao) {

        model.addAttribute("khoa", khoa.orElse(""));
        model.addAttribute("nganh", nganh.orElse(""));
        model.addAttribute("tgvao", tgvao.orElse(""));

        Page<ThongTinSD> list;
        if (khoa.isPresent() && nganh.isPresent() && !tgvao.orElse("").equals(""))
            list = ttSDService.findAllBytGVaoNotNull(pageable, khoa.orElse(""), nganh.orElse(""), tgvao.orElse(""));
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

    @GetMapping("/xuly")
    public String getXuLy(Model model, Pageable pageable, @RequestParam("page") Optional<Integer> page,
    @RequestParam("khoa") Optional<String> khoa, @RequestParam("nganh") Optional<String> nganh, @RequestParam("tgvao") Optional<String> tgvao) {

        model.addAttribute("khoa", khoa.orElse(""));
        model.addAttribute("nganh", nganh.orElse(""));
        model.addAttribute("tgvao", tgvao.orElse(""));

        Page<ThongTinSD> list;
        if (khoa.isPresent() && nganh.isPresent() && !tgvao.orElse("").equals(""))
            list = ttSDService.findAllBytGVaoNotNull(pageable, khoa.orElse(""), nganh.orElse(""), tgvao.orElse(""));
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
}
