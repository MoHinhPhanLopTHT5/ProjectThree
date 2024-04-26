package com.mohinhphanlop.projectthree.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.hibernate.mapping.Set;
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

    public Iterable<ThietBi> GetList() {
        // Lấy danh sách thiết bị
        Iterable<ThietBi> list = thietBiRepository.findAll();
        for (ThietBi thietBi : list) {

            // Lấy danh sách thông tin sử dụng ứng với thiết bị
            List<ThongTinSD> listTTSD = thietBi.getDS_ThongTinSD();
            for (ThongTinSD ttsd : listTTSD) {
                // Chuyển Date sang LocalDate
                if (ttsd.getTGDatcho() != null) {
                    LocalDateTime tgDatCho = ttsd.getTGDatcho()
                            .toInstant()
                            .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                            .toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

                    if (tgDatCho.plusHours(1).isBefore(now)) {
                        // Hết thời gian đặt chỗ
                        thietBi.setOccupied(false);
                        thongTinSDRepository.delete(ttsd);
                    } else
                        thietBi.setOccupied(true);
                } else // trường hợp không có đặt chỗ
                    thietBi.setOccupied(false);

                if (ttsd.getTGMuon() != null && ttsd.getTGTra() == null)
                    thietBi.setOccupied(true);
            }
        }
        return list;
    }
}
