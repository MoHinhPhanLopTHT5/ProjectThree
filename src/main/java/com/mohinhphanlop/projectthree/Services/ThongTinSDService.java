package com.mohinhphanlop.projectthree.Services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
            if (localDate.isBefore(localDateNow))
                return false;

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

    public Page<ThongTinSD> findAllBytGVaoNotNull(Pageable pageable) {
        return ttSDRepository.findAllBytGVaoNotNull(pageable);
    }

    public Page<ThongTinSD> findAllBytGVaoNotNull(Pageable pageable, String matv, String hoten, String khoa,
            String nganh, String tgvao) {

        Integer maTV = -1;
        try {
            maTV = Integer.parseInt(matv);
        } catch (NumberFormatException e) {
            maTV = -1;
        }

        // tgvao string to Date
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tgvao);
            return ttSDRepository.findAllBytGVaoNotNull(pageable, maTV, hoten, khoa, nganh, date);
        } catch (java.text.ParseException e) {
            return ttSDRepository.findAllBytGVaoNotNull(pageable, maTV, hoten, khoa, nganh);
        }
    }

    // Thống kê thiết bị
    public Page<ThongTinSD> findAllBytGMuonNotNull(Pageable pageable) {
        return ttSDRepository.findAllBytGMuonNotNull(pageable);
    }

    public Page<ThongTinSD> findAllBytGMuonNotNull(Pageable pageable, String matb, String tentb, String ngaymuon,
            String ngaytra) {

        Integer maTB;
        try {
            maTB = Integer.parseInt(matb);
        } catch (NumberFormatException e) {
            maTB = -1;
        }

        // tgvao string to Date

        Date dateNgaymuon = null, dateNgayTra = null;
        try {
            dateNgaymuon = new SimpleDateFormat("yyyy-MM-dd").parse(ngaymuon);
        } catch (ParseException e) {

        }

        try {
            dateNgayTra = new SimpleDateFormat("yyyy-MM-dd").parse(ngaytra);
        } catch (ParseException e) {

        }

        if (dateNgaymuon != null && dateNgayTra != null) {
            return ttSDRepository.findAllBytGMuonNotNullAndtGTraNotNull(pageable, maTB, tentb, dateNgaymuon,
                    dateNgayTra);
        } else if (dateNgaymuon != null) {
            return ttSDRepository.findAllBytGMuonNotNull(pageable, maTB, tentb, dateNgaymuon);
        } else if (dateNgayTra != null) {
            return ttSDRepository.findAllBytGTraNotNull(pageable, maTB, tentb, dateNgayTra);
        }
        return ttSDRepository.findAllBytGMuonNotNull(pageable, maTB, tentb);
    }

    // đặt chỗ nếu được

    public Page<ThongTinSD> findAllBytGDatchoNotNull(Pageable pageable) {
        return ttSDRepository.findAllBytGDatchoNotNull(pageable);
    }

    public Page<ThongTinSD> findAllBytGDatchoNotNull(Pageable pageable, String matb, String tentb, String matv,
            String hoten, String tgdatcho) {
        Integer MaTB = null, MaTV = null;

        try {
            MaTB = Integer.parseInt(matb);
        } catch (NumberFormatException e) {
            MaTB = -1;
        }

        try {
            MaTV = Integer.parseInt(matv);
        } catch (NumberFormatException e) {
            MaTV = -1;
        }

        try {
            Date tgDatCho = new SimpleDateFormat("yyyy-MM-dd").parse(tgdatcho);
            return ttSDRepository.findAllBytGDatchoNotNull(pageable, MaTB, MaTV, tentb, hoten, tgDatCho);
        } catch (ParseException e) {

        }

        return ttSDRepository.findAllBytGDatchoNotNull(pageable, MaTB, MaTV, tentb, hoten);
    }

    public boolean deleteThongTinSD(Integer maTT) {
        ThongTinSD ttsd = ttSDRepository.findById(maTT).orElse(null);
        if (ttsd == null)
            return false;
        ttSDRepository.delete(ttsd);
        return true;
    }

    public ThongTinSD findById(Integer maTT) {
        return ttSDRepository.findById(maTT).orElse(null);
    }
}
