package com.mohinhphanlop.projectthree.Services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Repositories.XuLyRepository;

@Service
public class XuLyService {

    @Autowired
    private XuLyRepository xuLyRepository;

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
}
