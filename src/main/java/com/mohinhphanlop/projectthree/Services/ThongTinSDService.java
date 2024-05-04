package com.mohinhphanlop.projectthree.Services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public Page<ThongTinSD> findAllBytGVaoNotNull(Pageable pageable, String khoa, String nganh, String tgvao) {        
        Page<ThongTinSD> list = ttSDRepository.findAllBytGVaoNotNull(pageable);
        ArrayList <ThongTinSD> listTemp = new ArrayList<ThongTinSD>();
        
        // Parse TGVao with format yyyy-MM-DD
        LocalDate TGVaoTemp = LocalDate.parse(tgvao, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // LocalDate to Date
        Date TGVao = Date.from(TGVaoTemp.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for (ThongTinSD ttsd : list) {
            if (ttsd.getThanhvien().getKhoa().toLowerCase().contains(khoa.toLowerCase()) 
            || ttsd.getThanhvien().getNganh().toLowerCase().contains(nganh.toLowerCase()) ||
            ttsd.getTGVao().equals(TGVao)) {
                listTemp.add(ttsd);
            }
        }

        // Get PageRequest from Pageable
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), listTemp.size());

        return new PageImpl<>(listTemp.subList(start, end), pageRequest, listTemp.size());
    }
}
