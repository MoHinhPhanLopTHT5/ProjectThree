package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Repositories.ThietBiRepository;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;
import java.util.Optional;

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

    public ThietBi FindByID(String maTB) {
        Optional<ThietBi> optionalThietBi = tbRepository.findById(Integer.parseInt(maTB));
        if (optionalThietBi.isPresent()) {
            return optionalThietBi.get();
        } else {
            // Xử lý trường hợp không tìm thấy giá trị
            return null; // hoặc ném một exception phù hợp
        }
    }
}
