package com.mohinhphanlop.projectthree.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;
import com.mohinhphanlop.projectthree.Repositories.ThanhVienRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

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
            String email = tv.getEmail() == null ? "-1" : tv.getEmail().isEmpty() ? "-1" : tv.getEmail();
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

    public boolean CheckSDTExists(String sdt) {
        Iterable<ThanhVien> list = tvRepository.findAll();
        for (ThanhVien tv : list) {
            String sdtTV = tv.getSDT() == null ? "-1" : tv.getSDT();
            if (sdtTV.equals(sdt)) {
                return true;
            }
        }
        return false;
    }

    public boolean CheckNotTheSameEmail(ThanhVien tvCurrent, String email) {
        if (tvCurrent.getEmail() == null)
            return true;
        if (tvCurrent.getEmail().equals(email)) {
            return false;
        }
        return true;
    }

    public boolean CheckNotTheSameSDT(ThanhVien tvCurrent, String sdt) {
        if (tvCurrent.getSDT() == null)
            return false;
        if (tvCurrent.getSDT().equals(sdt)) {
            return false;
        }
        return true;
    }

    public ThanhVien UpdateThanhVien(String maThanhVien, String email, String password, String sdt) {
        ThanhVien tv = tvRepository.findById(Integer.parseInt(maThanhVien)).get();
        tv.setEmail(email);
        tv.setPassword(password);
        tv.setSDT(sdt);
        return tvRepository.save(tv);
    }

    public ThanhVien CreateThanhVien(String maThanhVien, String email, String sdt, String password, String fullname) {
        ThanhVien tv = new ThanhVien();
        tv.setMaTV(Integer.parseInt(maThanhVien));
        tv.setEmail(email);
        tv.setSDT(sdt);
        tv.setPassword(password);
        tv.setHoTen(fullname);
        return tvRepository.save(tv);
    }

    public Iterable<ThanhVien> GetList() {
        return tvRepository.findAll();
    }

    public Iterable<XuLy> GetListXuLyFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Integer.parseInt(maThanhVien)).get();
        return tv.getDS_XuLy();
    }

    public Iterable<ThongTinSD> GetListThongTinSDFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Integer.parseInt(maThanhVien)).get();
        return tv.getDS_ThongTinSD();
    }

    public Iterable<ThongTinSD> GetListThongTinSDDangMuonFrom(String maThanhVien) {
        ThanhVien tv = tvRepository.findById(Integer.parseInt(maThanhVien)).get();

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
        ThanhVien tv = tvRepository.findById(Integer.parseInt(maThanhVien)).get();

        Iterable<ThongTinSD> listTemp = tv.getDS_ThongTinSD();
        ArrayList<ThongTinSD> list = new ArrayList<ThongTinSD>();

        for (ThongTinSD thongTinSD : listTemp) {
            if (thongTinSD.getTGDatcho() != null) {
                list.add(thongTinSD);
            }
        }
        return list;
    }

    public List<XuLy> GetListXuLyFromMember(Integer id) {
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
                id = Integer.parseInt(
                        listTv.get(0).getMaTV().toString().substring(listTv.get(0).getMaTV().toString().length() - 4));
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

    public Optional<ThanhVien> FindThanhVienById(Integer id) {
        return tvRepository.findById(id);
    }

    public String Delete(Integer id) {
        String result = "Xóa không thành công thành viên này";
        try {
            // Check isProcess
            List<XuLy> processList = tvRepository.findById(id).get().getDS_XuLy();
            int count = processList
                    .stream()
                    .filter(item -> item.getTrangThaiXL() == 1)
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

    public String DeleteMany(String lastSchoolYear) {
        String result = "Xóa không thành công";
        try {
            List<Integer> listMaHopLe = tvRepository.GetIDHopLe(lastSchoolYear);
            tvRepository.deleteAllById(listMaHopLe);
            result = "Xóa thành công";
        } catch (Exception ex) {
        }
        return result;
    }

    private String checkValidTitleOfFile(Sheet sheet, String... titles) {
        for (int i = 0; i < titles.length; i++) {
            if (!sheet.getRow(0).getCell(i).getStringCellValue().equals(titles[i])) {
                return "The title of the " + (i + 1) + " column is not in the correct format (Must be '" + titles[i]
                        + "')";
            }
        }
        return null;
    }

    private boolean checkLenghtNumeric(Cell cell, int maxLenght) {
        return ((long) cell.getNumericCellValue() + "").length() <= maxLenght;
    }

    private boolean checkLenghtString(Cell cell, int maxLenght) {
        return cell.getStringCellValue().length() <= maxLenght;
    }

    public String importFileExcelMembers(MultipartFile file) {
        String message = "";
        Workbook workbook = null;
        int success = 0;
        int error = 0;
        int all = 0;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    String errors = checkValidTitleOfFile(sheet, "MaTV", "HoTen", "Khoa", "Nganh", "SDT", "Password",
                            "Email");
                    if (errors != null) {
                        message = errors;
                        break;
                    }
                } else {
                    boolean isValid = true;
                    ThanhVien tv = new ThanhVien();
                    OUTER: for (int i = 0; i <= 6; i++) {
                        switch (i) {
                            case 0 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                all++;
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.NUMERIC) {
                                        if (checkLenghtNumeric(cell, 10)) {
                                            if (!tvRepository
                                                    .findById((int) cell.getNumericCellValue())
                                                    .isPresent()) {
                                                tv.setMaTV((int) cell.getNumericCellValue());
                                            } else {
                                                error++;
                                                isValid = false;
                                                break OUTER;
                                            }
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                            case 1 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.STRING) {
                                        if (checkLenghtString(cell, 100)) {
                                            tv.setHoTen(cell.getStringCellValue());
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                            case 2 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.STRING) {
                                        if (checkLenghtString(cell, 100)) {
                                            tv.setKhoa(cell.getStringCellValue());
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                            case 3 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.STRING) {
                                        if (checkLenghtString(cell, 100)) {
                                            tv.setNganh(cell.getStringCellValue());
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                            case 4 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.STRING) {
                                        if (checkLenghtString(cell, 15)) {
                                            tv.setSDT(cell.getStringCellValue());
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                            case 5 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    switch (cell.getCellType()) {
                                        case STRING -> {
                                            if (checkLenghtString(cell, 10)) {
                                                tv.setPassword(cell.getStringCellValue());
                                            } else {
                                                error++;
                                                isValid = false;
                                                break OUTER;
                                            }
                                        }
                                        case NUMERIC -> {
                                            if (checkLenghtNumeric(cell, 10)) {
                                                tv.setPassword((long) cell.getNumericCellValue() + "");
                                            } else {
                                                error++;
                                                isValid = false;
                                                break OUTER;
                                            }
                                        }
                                        default -> {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    }
                                }
                            }
                            case 6 -> {
                                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (cell == null) {
                                    error++;
                                    isValid = false;
                                    break OUTER;
                                } else {
                                    if (cell.getCellType() == CellType.STRING) {
                                        if (checkLenghtString(cell, 25)) {
                                            tv.setEmail(cell.getStringCellValue());
                                        } else {
                                            error++;
                                            isValid = false;
                                            break OUTER;
                                        }
                                    } else {
                                        error++;
                                        isValid = false;
                                        break OUTER;
                                    }
                                }
                            }
                        }
                    }
                    try {
                        if (isValid) {
                            tvRepository.save(tv);
                            success++;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
            System.out.println(all);
            System.out.println(error);
            if ((success + error) == all) {
                if (error == all) {
                    message = "Không thành công";
                } else {
                    message = "Thành công";
                }
            } else {
                message = "Không thành công";
            }
            workbook.close();
        } catch (IOException ex) {
        } finally {
            try {
                workbook.close();
            } catch (IOException ex) {
            }
        }
        return message;
    }
}
