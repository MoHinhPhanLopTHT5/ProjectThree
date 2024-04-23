/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package com.mohinhphanlop.projectthree.Controllers;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Repositories.ThanhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Admin
 */
@Controller
public class MainController {
    
    @Autowired
    private ThanhVienRepository thanhVienRepository;
    
    @GetMapping("/")
    public String homepage(Model model) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for(ThanhVien tv: list) {
            System.out.println(tv.getHoTen());
        }
        model.addAttribute("data", list);
        return "index";
    }
    
    @RequestMapping("/url")
    public String page(Model model) {
        model.addAttribute("attribute", "value");
        return "view.name";
    }
    
}
