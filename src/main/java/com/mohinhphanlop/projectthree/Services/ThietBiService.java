package com.mohinhphanlop.projectthree.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Repositories.ThietBiRepository;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

@Service
public class ThietBiService {
    @Autowired
    private ThietBiRepository thietBiRepository;
    @Autowired
    private ThongTinSDRepository thongTinSDRepository;

    public ThietBi GetByID(String id) {
        return thietBiRepository.findById(Integer.parseInt(id)).get();
    }

    public Iterable<ThietBi> GetList() {
        // Lấy danh sách thiết bị
        Iterable<ThietBi> list = thietBiRepository.findAll();
        return list;
    }
}
