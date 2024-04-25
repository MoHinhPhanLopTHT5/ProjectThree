package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Repositories.ThanhVienRepository;

@Service
public class ThanhVienService {
    @Autowired
    private ThanhVienRepository thanhVienRepository;

    public boolean CheckLogin(String usernameOrEmail, String password) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for (ThanhVien tv : list) {
            String maTV = tv.getMaTV().toString();
            String email = tv.getEmail() == null ? "-1" : tv.getEmail();
            String pw = tv.getPassword();
            if ((maTV.equals(usernameOrEmail) || email.equals(usernameOrEmail))
                    && pw.equals(password))
                return true;
        }
        return false;
    }

    public ThanhVien getByUsernameOrEmail(String usernameOrEmail) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for (ThanhVien tv : list) {
            String maTV = tv.getMaTV().toString();
            String email = tv.getEmail() == null ? "-1" : tv.getEmail();
            if (maTV.equals(usernameOrEmail) || email.equals(usernameOrEmail))
                return tv;
        }
        return null;
    }

    public boolean CheckUsername(String username) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for (ThanhVien tv : list) {
            if (tv.getMaTV().toString().equals(username))
                return true;
        }
        return false;
    }

    public boolean CheckUsernameIsRegistered(String username) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for (ThanhVien tv : list) {
            if (tv.getMaTV().toString().equals(username) && tv.getEmail() != null)
                return true;
        }
        return false;
    }

    public boolean CheckEmailExists(String email) {
        Iterable<ThanhVien> list = thanhVienRepository.findAll();
        for (ThanhVien tv : list) {
            String emailTV = tv.getEmail() == null ? "-1" : tv.getEmail();
            if (emailTV.equals(email))
                return true;
        }
        return false;
    }

    public boolean CheckNotTheSameEmail(ThanhVien tvCurrent, String email) {
        if (tvCurrent.getEmail().equals(email))
            return false;

        return true;
    }

    public ThanhVien UpdateThanhVien(String maThanhVien, String email, String password) {
        ThanhVien tv = thanhVienRepository.findById(Long.parseLong(maThanhVien)).get();
        tv.setEmail(email);
        tv.setPassword(password);
        return thanhVienRepository.save(tv);
    }

    public ThanhVien CreateThanhVien(String maThanhVien, String email, String password, String fullname) {
        ThanhVien tv = new ThanhVien();
        tv.setMaTV(Integer.parseInt(maThanhVien));
        tv.setEmail(email);
        tv.setPassword(password);
        tv.setHoTen(fullname);
        return thanhVienRepository.save(tv);
    }

    public Iterable<ThanhVien> GetList() {
        return thanhVienRepository.findAll();
    }
}
