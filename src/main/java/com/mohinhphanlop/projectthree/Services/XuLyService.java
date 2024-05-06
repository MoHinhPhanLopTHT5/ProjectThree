package com.mohinhphanlop.projectthree.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public XuLy addXuLy(int maXL, int maTV, String hinhThucXL, int soTien, Date ngayXL, int trangThaiXL) {

        XuLy xl = new XuLy();
        ThanhVien tv = null;
        tv.setMaTV(maTV);

        xl.setThanhvien(tv);
        xl.setHinhThucXL(hinhThucXL);
        xl.setSoTien(soTien);
        xl.setTrangThaiXL(trangThaiXL);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        ngayXL = new Date();
        xl.setNgayXL(ngayXL);

        return xuLyRepository.save(xl);

    }

    public XuLy updateXuLy(int maXL, int maTV, String hinhThucXL, int soTien, Date ngayXL, int trangThaiXL) {
        XuLy xl = xuLyRepository.findById(maXL).get();
        ThanhVien tv = new ThanhVien();
        tv.setMaTV(maTV);

        xl.setThanhvien(tv);
        xl.setHinhThucXL(hinhThucXL);
        xl.setSoTien(soTien);
        xl.setNgayXL(ngayXL);
        xl.setTrangThaiXL(trangThaiXL);

        return xuLyRepository.save(xl);

    }

    public void deleteXuLy(int maXL) {
        xuLyRepository.deleteById(maXL);
    }

    public Page<XuLy> findAll(Pageable pageable) {
        return xuLyRepository.findAll(pageable);
    }

    public Page<XuLy> findAll(Pageable pageable, String matv, String sotien, String hoten, String hinhthucxl,
            String ngayxl) {

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
            return xuLyRepository.findAll(pageable, maTV, soTien, hoten, hinhthucxl, ngayXL);
        } catch (ParseException e) {
        }

        return xuLyRepository.findAll(pageable, maTV, soTien, hoten, hinhthucxl);
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
