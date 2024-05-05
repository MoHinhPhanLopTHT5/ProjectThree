package com.mohinhphanlop.projectthree.Services;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Repositories.XuLyRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

@Service
public class XuLyService {
    @Autowired
    private XuLyRepository xuLyRepository;
    
    public XuLy addXuLy(int maXL, int maTV, String hinhThucXL, int soTien, Date ngayXL, int trangThaiXL) {
       
        XuLy xl = new XuLy();
        ThanhVien tv = new ThanhVien();
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
    
    public List<XuLy> getByKeyWord(String keyword) {
        return xuLyRepository.findByKeyword(keyword);
    }
    
    public Page<XuLy> getListXuLy(String hinhThucXL, Pageable pageable) {
        Page<XuLy> list;
        if (hinhThucXL.isEmpty())
            list = xuLyRepository.findAll(pageable);
        else
            list = xuLyRepository.findAllByHinhThucXL(hinhThucXL, pageable);
        
        return list;
    }

}
