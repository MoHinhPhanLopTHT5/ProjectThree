package com.mohinhphanlop.projectthree.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Repositories.ThanhVienRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThanhVienService {

    @Autowired
    private ThanhVienRepository tvRepository; // class của Spring boot cho các thao tác mặc định của framework
    // bao gồm thêm sửa xoá bằng method .save(), findAll để tìm kiếm
    // xoá bằng hàm deleteById() hoặc delete(ThanhVien tv) (tham số để xoá, có thể
    // là ThietBi)

    public boolean CheckLogin(String usernameOrEmail, String password) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            String maTV = tv.getMaTV().toString();
            String email = tv.getEmail() == null ? "-1" : tv.getEmail();
            String pw = tv.getPassword();
            if ((maTV.equals(usernameOrEmail) || email.equals(usernameOrEmail))
                    && pw.equals(password)) {
                return true;
            }
        }
        return false;
    }

    public ThanhVien getByUsernameOrEmail(String usernameOrEmail) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            String maTV = tv.getMaTV().toString();
            String email = tv.getEmail() == null ? "-1" : tv.getEmail();
            if (maTV.equals(usernameOrEmail) || email.equals(usernameOrEmail)) {
                return tv;
            }
        }
        return null;
    }

    public boolean CheckUsername(String username) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            if (tv.getMaTV().toString().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckUsernameIsRegistered(String username) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            if (tv.getMaTV().toString().equals(username) && tv.getEmail() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckEmailExists(String email) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            String emailTV = tv.getEmail() == null ? "-1" : tv.getEmail();
            if (emailTV.equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckNotTheSameEmail(ThanhVien tvCurrent, String email) {
        if (tvCurrent.getEmail().equals(email)) {
            return false;
        }
        return true;
    }

    public ThanhVien UpdateThanhVien(String maThanhVien, String email, String password) {
        ThanhVien tv = tvRepository.findById(Long.parseLong(maThanhVien)).get();
        tv.setEmail(email);
        tv.setPassword(password);
        return tvRepository.save(tv);
    }

    public ThanhVien CreateThanhVien(String maThanhVien, String email, String password, String fullname) {
        ThanhVien tv = new ThanhVien();
        tv.setMaTV(Integer.parseInt(maThanhVien));
        tv.setEmail(email);
        tv.setPassword(password);
        tv.setHoTen(fullname);
        return tvRepository.save(tv);
    }

    public Iterable<ThanhVien> GetList() {
        return tvRepository.findAll();
    }

    public Iterable<XuLy> GetListXuLyFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Long.parseLong(maThanhVien)).get();
        return tv.getDS_XuLy();
    }

    public Iterable<ThongTinSD> GetListThongTinSDFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Long.parseLong(maThanhVien)).get();
        return tv.getDS_ThongTinSD();
    }

    public Iterable<ThongTinSD> GetListThongTinSDDangMuonFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Long.parseLong(maThanhVien)).get();

        Iterable<ThongTinSD> listTemp = tv.getDS_ThongTinSD();
        ArrayList<ThongTinSD> list = new ArrayList<ThongTinSD>();

        for (ThongTinSD thongTinSD : listTemp) {
            if (thongTinSD.getTGMuon() != null) {
                list.add(thongTinSD);
            }
        }
        return list;
    }

    public Iterable<ThongTinSD> GetListThongTinSDDatchoFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Long.parseLong(maThanhVien)).get();

        Iterable<ThongTinSD> listTemp = tv.getDS_ThongTinSD();
        ArrayList<ThongTinSD> list = new ArrayList<ThongTinSD>();

        for (ThongTinSD thongTinSD : listTemp) {
            if (thongTinSD.getTGDatcho() != null) {
                list.add(thongTinSD);
            }
        }
        return list;
    }

    public List<XuLy> GetListXuLyFromMember(long id) {
        return tvRepository.findById(id).get().getDS_XuLy();
    }

    public String SaveThanhVien(ThanhVien tv) {
        if (CheckEmailExists(tv.getEmail())) {
            return "Email tồn tại";
        }
        try {
            int id;
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yy");
            List<ThanhVien> listTv = tvRepository.GetListThanhVienTheoNam(sdf.format(now));
            if (listTv.isEmpty()) {
                id = 0;
            } else {
                id = Integer.parseInt(listTv.get(0).getMaTV().toString().substring(listTv.get(0).getMaTV().toString().length() - 4));
            }
            tv.setMaTV(tv.getMaTV() + id + 1);
            tvRepository.save(tv);
            return "Thêm thành công";
        } catch (NumberFormatException ex) {
        }
        return "Thêm không thành công";
    }

    public boolean UpdateThanhVien(ThanhVien tv) {
        try {
            tvRepository.save(tv);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    public Optional<ThanhVien> FindThanhVienById(long id) {
        return tvRepository.findById(id);
    }

    public String Delete(long id) {
        String result = "Xóa không thành công thành viên này";
        try {
            // Check isProcess
            List<XuLy> processList = tvRepository.findById(id).get().getDS_XuLy();
            int count = processList
                    .stream()
                    .filter(item -> item.getTrangThaiXL() == 0)
                    .toList()
                    .size();
            if (count > 0) {
                result = "Thành viên này chưa được xử lý";
            } else {
                count = 0;
                // Check equipment has not been returned
                List<ThongTinSD> usageList = tvRepository.findById(id).get().getDS_ThongTinSD();
                count = usageList
                        .stream()
                        .filter(item -> item.getTGMuon() != null && item.getTGTra() == null)
                        .toList()
                        .size();
                if (count > 0) {
                    result = "Thành viên này chưa trả thiết bị";
                } else {
                    tvRepository.deleteById(id);
                    result = "Xóa thành viên này thành công!";
                }
            }
        } catch (Exception ex) {
        }
        return result;
    }
}
