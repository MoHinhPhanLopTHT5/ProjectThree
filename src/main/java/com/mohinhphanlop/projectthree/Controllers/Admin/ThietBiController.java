package com.mohinhphanlop.projectthree.Controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quantri/thietbi")
public class ThietBiController {
    @GetMapping("")
    public String getIndex() {
        return "admin/thietbi/index";
    }
}
