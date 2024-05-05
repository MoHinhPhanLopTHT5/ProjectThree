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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quantri/xuly")
public class XuLyController {
    @Autowired
    private XuLyService xuLyService;
    
    @GetMapping("")
    public String getIndex(Model model, @RequestParam("page") Optional<Integer> page,
            @RequestParam("hinhThucXL") Optional<String> hinhThucXL,
            Pageable pageable) {
        String HinhThucXL = hinhThucXL.orElse("");
        Page<XuLy> xuLyList = xuLyService.getListXuLy(HinhThucXL, pageable);
        model.addAttribute("data", xuLyList);
        
        
        int totalPages = xuLyList.getTotalPages();
        if (totalPages > 0) {
            int[] pageNumbers = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pageNumbers[i] = i + 1;
            }
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("HinhThucXL", HinhThucXL);
        return "admin/xuly/index";
    }
    
    
    
    
}
