package com.mohinhphanlop.projectthree.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Controllers.MainController;
import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Repositories.ThanhVienRepository;
import com.mohinhphanlop.projectthree.Repositories.XuLyRepository;

@Service
public class XuLyService {
    @Autowired
    private XuLyRepository xuLyRepository;
    @Autowired
    private ThanhVienRepository tvRepository;

    public XuLy addXuLy(String matv, String hinhThucXL, String sotien, String ngayxl, String trangthaixl) {

        XuLy xl = new XuLy();
        ThanhVien tv = tvRepository.findById(MainController.TryParseInt(matv)).orElse(null);

        Iterable<XuLy> list = xuLyRepository.findAll(Sort.by(Sort.Direction.DESC, "maXL"));

        int count = 0;
        for (XuLy item : list) {
            count = 1;
            xl.setMaXL(item.getMaXL() + 1);
            break;
        }

        if (count == 0)
            xl.setMaXL(1);

        if (tv == null)
            return null;

        xl.setThanhvien(tv);
        xl.setHinhThucXL(hinhThucXL);
        xl.setSoTien(MainController.TryParseInt(sotien));
        xl.setTrangThaiXL(MainController.TryParseInt(trangthaixl));

        Date ngayXL = DateService.dateStringtoDateType(ngayxl);
        xl.setNgayXL(ngayXL);

        return xuLyRepository.save(xl);
    }

    public XuLy updateXuLy(Integer maXL, String hinhThucXL, String soTien, String ngayxl,
            String trangThaiXL) {
        XuLy xl = xuLyRepository.findById(maXL).orElse(null);

        if (xl == null)
            return null;

        xl.setHinhThucXL(hinhThucXL);

        xl.setSoTien(MainController.TryParseInt(soTien));

        Date ngayXL = DateService.dateStringtoDateType(ngayxl);
        xl.setNgayXL(ngayXL);
        xl.setTrangThaiXL(Integer.parseInt(trangThaiXL));

        return xuLyRepository.save(xl);

    }

    public boolean deleteXuLy(Integer maXL) {
        XuLy xl = xuLyRepository.findById(maXL).orElse(null);

        if (xl == null)
            return false;
        xuLyRepository.delete(xl);
        return true;
    }

    public Page<XuLy> findAll(Pageable pageable) {
        return xuLyRepository.findAll(pageable);
    }

    public Page<XuLy> findAll(Pageable pageable, String matv, String sotien, String hoten, String hinhthucxl,
            String ngayxl, Integer trangthaixl) {

        Integer soTien, maTV;

        try {
            soTien = Integer.parseInt(sotien);
        } catch (NumberFormatException e) {
            soTien = -1;
        }

        try {
            maTV = Integer.parseInt(matv);
        } catch (NumberFormatException e) {
            maTV = -1;
        }

        Date ngayXL;

        try {
            ngayXL = new SimpleDateFormat("yyyy-MM-dd").parse(ngayxl);
            return xuLyRepository.findAll(pageable, maTV, soTien, hoten, hinhthucxl, ngayXL, trangthaixl);
        } catch (ParseException e) {
        }

        return xuLyRepository.findAll(pageable, maTV, soTien, hoten, hinhthucxl, trangthaixl);
    }

    public XuLy findByThanhVienId(Integer id) {
        return xuLyRepository.findByThanhVienId(id);
    }

    public Page<XuLy> findAllByThanhVienId(Pageable pageable, String id) {
        return xuLyRepository.findAllByThanhVienId(pageable, MainController.TryParseInt(id));
    }

    public XuLy findById(Integer id) {
        return xuLyRepository.findById(id).get();
    }
}
