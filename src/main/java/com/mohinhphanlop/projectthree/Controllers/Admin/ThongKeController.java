package com.mohinhphanlop.projectthree.Controllers.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quantri/thongke")
public class ThongKeController {

    @GetMapping("")
    public String getIndex() {
        return "admin/thongke/index";
    }
}
