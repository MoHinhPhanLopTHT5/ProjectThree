package com.mohinhphanlop.projectthree.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    public ThanhVien UpdateThanhVien(String maThanhVien, String email, String password) {
        ThanhVien tv = thanhVienRepository.findById(Long.parseLong(maThanhVien)).get();
        tv.setEmail(email);
        tv.setPassword(password);
        return thanhVienRepository.save(tv);
    }

    public Iterable<ThanhVien> GetThanhViens() {
        return thanhVienRepository.findAll();
    }
}
