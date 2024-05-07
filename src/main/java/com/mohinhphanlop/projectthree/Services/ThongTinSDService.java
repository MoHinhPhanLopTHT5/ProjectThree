package com.mohinhphanlop.projectthree.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ThongTinSDService {

    @Autowired
    private ThongTinSDRepository ttSDRepository;
    @Autowired
    private ThietBiService tbService;
    @Autowired
    private ThanhVienService tvService;

    public void RemoveAllTGDatchoOver1Hour() {
        Iterable<ThongTinSD> listThongTinSD = ttSDRepository.findAll();
        // Lấy danh sách thông tin sử dụng ứng với thiết bị
        for (ThongTinSD ttsd : listThongTinSD) {
            // Chuyển Date sang LocalDate
            if (ttsd.getTGDatcho() != null) {
                LocalDateTime tgDatCho = ttsd.getTGDatcho()
                        .toInstant()
                        .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                        .toLocalDateTime();
                LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

                if (tgDatCho.plusHours(1).isBefore(now)) {
                    // Hết thời gian đặt chỗ
                    ttSDRepository.delete(ttsd);
                }
            }
        }
    }

    public boolean CheckTrangThaiDatCho(String maTB, String date) {
        ThietBi tb = tbService.GetByID(maTB);
        if (tb != null) {

            // date to LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(date + ":00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00"));
            LocalDate localDate = localDateTime.toLocalDate();

            LocalDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
            LocalDate localDateNow = now.toLocalDate();

            // Ngày đặt là quá khứ
            if (localDate.isBefore(localDateNow)) {
                return false;
            }

            Iterable<ThongTinSD> listThongTinSD = tb.getDS_ThongTinSD();
            for (ThongTinSD ttsd : listThongTinSD) {
                if (ttsd.getTGDatcho() != null) {
                    LocalDate tgDatCho = ttsd.getTGDatcho()
                            .toInstant()
                            .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
                            .toLocalDate();
                    if (tgDatCho.isEqual(localDate)) {
                        // Thiết bị đã được đặt chỗ trong ngày chỉ định
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }

    public Integer findLastID() {
        Iterable<ThongTinSD> list = ttSDRepository.findAllByOrderByMaTTDesc();
        for (ThongTinSD thongTinSD : list) {
            return thongTinSD.getMaTT();
        }
        return 0;
    }

    public ThongTinSD CreateThoiGianDatCho(String maTV, String maTB, String date) {
        ThietBi tb = tbService.GetByID(maTB);
        if (tb != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(date + ":00",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:00"));

            ThongTinSD ttsd = new ThongTinSD();

            ttsd.setTGDatcho(Date.from(localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()));
            ttsd.setThanhvien(tvService.getByUsernameOrEmail(maTV));
            ttsd.setThietbi(tb);
            ttsd.setMaTT(findLastID() + 1);

            return ttSDRepository.save(ttsd);

        }
        return null;
    }

    public ThongTinSD CreateNewInfo(long maTV) {
        ThongTinSD ttsd = new ThongTinSD();
        Date dateNow = new Date();
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTimeNow = sdfDateTime.format(dateNow);
        try {
            ttsd.setTGVao(sdfDateTime.parse(dateTimeNow));
        } catch (ParseException ex) {
            Logger.getLogger(ThongTinSDService.class.getName()).log(Level.SEVERE, null, ex);
        }
        ttsd.setThanhvien(tvService.FindThanhVienById(maTV).get());
        return ttSDRepository.save(ttsd);
    }

    public ThongTinSD TraThietBi(ThongTinSD tt) {
        Date dateNow = new Date();
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTimeNow = sdfDateTime.format(dateNow);
        try {
            tt.setTGTra(sdfDateTime.parse(dateTimeNow));
        } catch (ParseException ex) {
            Logger.getLogger(ThongTinSDService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ttSDRepository.save(tt);
    }
}
