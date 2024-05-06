package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Repositories.ThietBiRepository;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

@Service
public class ThietBiService {
    @Autowired
    private ThietBiRepository tbRepository;
    @Autowired
    private ThongTinSDRepository ttsdRepository;

    public ThietBi GetByID(String id) {
        return tbRepository.findById(Integer.parseInt(id)).get();
    }

    public Iterable<ThietBi> GetList() {
        // Lấy danh sách thiết bị
        Iterable<ThietBi> list = tbRepository.findAll();
        return list;
    }
}
