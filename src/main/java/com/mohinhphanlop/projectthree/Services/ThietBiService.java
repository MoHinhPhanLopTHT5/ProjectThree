package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Repositories.ThietBiRepository;
import com.mohinhphanlop.projectthree.Repositories.ThongTinSDRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        Iterable<ThietBi> list = tbRepository.findAll();
        return list;
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

    public ThietBi FindByID(String maTB) {
        Optional<ThietBi> optionalThietBi = tbRepository.findById(Integer.parseInt(maTB));
        if (optionalThietBi.isPresent()) {
            return optionalThietBi.get();
        } else {
            return null;
        }
    }

    public List<ThietBi> DSThietBiHopLe() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();
        LocalDateTime lcd = LocalDateTime.now();
        lcd = lcd.plusHours(-1);
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println(dt.format(lcd));
        return tbRepository.DSThietBiHopLe(tomorrow, lcd);
    }

    public ThietBi CreateThietBi(Integer maTB, String tenTB, String motaTB) {
        ThietBi tb = new ThietBi();
        tb.setMaTB(maTB);
        tb.setTenTB(tenTB);
        tb.setMoTaTB(motaTB);
        return tbRepository.save(tb);
    }

    public boolean isExistMaThietBi(String maTB) {
        Iterable<ThietBi> list = tbRepository.findAll();
        for (ThietBi tb : list)
            if (tb.getMaTB().toString().equals(maTB))
                return true;
        return false;
    }

    public void updateThietBi(ThietBi thietBi) {
        ThietBi tb = tbRepository.findById(thietBi.getMaTB()).orElse(null);
        if (tb != null) {
            tb.setTenTB(thietBi.getTenTB());
            tb.setMoTaTB(thietBi.getMoTaTB());
            tbRepository.save(tb);
        } else {
            throw new EntityNotFoundException("Thiết bị không tồn tại");
        }
    }

    public boolean deleteThietBi(ThietBi thietBi) {
        ThietBi tb = tbRepository.findById(thietBi.getMaTB()).orElse(null);
        if (tb != null) {
            if (thietBiRemovable(tb)) {
                deleteThongTinSDByMaTB(tb);
                tbRepository.delete(tb);
            } else
                return false;
        } else
            return false;
        return true;
    }

    public boolean thietBiRemovable(ThietBi tb) {
        Iterable<ThongTinSD> list = ttsdRepository.findAllByMaTB(tb.getMaTB());
        for (ThongTinSD ttsd : list) {
            if (ttsd.getTGDatcho() != null)
                return false;
            if (ttsd.getTGMuon() != null && ttsd.getTGTra() == null)
                return false;
        }
        return true;
    }

    public void deleteThongTinSDByMaTB(ThietBi tb) {
        Iterable<ThongTinSD> list = ttsdRepository.findAllByMaTB(tb.getMaTB());
        for (ThongTinSD ttsd : list) {
            ttsdRepository.delete(ttsd);
        }
    }

    public List<ThietBi> deleteNhieuThietBi(String kyTu, Iterable<ThietBi> list) {
        List<ThietBi> listCurr = new ArrayList<>();
        for (ThietBi tb : list) {
            String thietbi = tb.getMaTB().toString().substring(0, 1);
            if (kyTu.startsWith(thietbi)) {
                if (!deleteThietBi(tb))
                    listCurr.add(tb);

            }
        }
        return listCurr;
    }
}
