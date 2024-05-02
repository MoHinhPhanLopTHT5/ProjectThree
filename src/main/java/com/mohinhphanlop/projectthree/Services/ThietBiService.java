package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Repositories.ThietBiRepository;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

@Service
public class ThietBiService {
    @Autowired
    private ThietBiRepository tbRepository;
    // @Autowired
    // private ThongTinSDRepository ttsdRepository;

    public ThietBi GetByID(String id) {
        return tbRepository.findById(Integer.parseInt(id)).get();
    }

    public Page<ThietBi> GetList(String tenTB, Pageable pageable) {
        // Lấy danh sách thiết bị
        Page<ThietBi> list;
        if (tenTB.isEmpty())
            list = tbRepository.findAll(pageable);
        else
            list = tbRepository.findAllByTenTB(tenTB, pageable);
        return list;
    }
}
