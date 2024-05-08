package com.mohinhphanlop.projectthree.Services;

import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelExportService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Autowired
    private ThanhVienService tvService;
    @Autowired
    private ThietBiService tbService;
    @Autowired
    private ThongTinSDService ttsdService;
    @Autowired
    private XuLyService xlService;

    private void init() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(String type) {
        sheet = workbook.createSheet(type);

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(16);
        style.setFont(font);

        switch (type) {
            case "ThanhVien":
                createCell(row, 0, "MaTV", style);
                createCell(row, 1, "HoTen", style);
                createCell(row, 2, "Khoa", style);
                createCell(row, 3, "Nganh", style);
                createCell(row, 4, "SDT", style);
                createCell(row, 5, "Password", style);
                createCell(row, 6, "Email", style);
                break;

            case "ThietBi":
                createCell(row, 0, "MaTB", style);
                createCell(row, 1, "TenTB", style);
                createCell(row, 2, "MoTaTB", style);
                break;
            case "TTSD":
                createCell(row, 0, "MaTT", style);
                createCell(row, 1, "MaTV", style);
                createCell(row, 2, "MaTB", style);
                createCell(row, 3, "TGVao", style);
                createCell(row, 4, "TGMuon", style);
                createCell(row, 5, "TGTra", style);
                createCell(row, 6, "TGDatcho", style);
                break;
            case "XuLy":
                createCell(row, 0, "MaXL", style);
                createCell(row, 1, "MaTV", style);
                createCell(row, 2, "HinhThucXL", style);
                createCell(row, 3, "SoTien", style);
                createCell(row, 4, "NgayXL", style);
                createCell(row, 5, "TrangThaiXL", style);
                break;

            default:
                break;
        }
    }

    private void writeDataLines(String type) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        switch (type) {
            case "ThanhVien":
                Iterable<ThanhVien> listThanhVien = tvService.GetList();
                for (ThanhVien tv : listThanhVien) {
                    Row row = sheet.createRow(rowCount++);
                    int columnCount = 0;

                    createCell(row, columnCount++, tv.getMaTV(), style);
                    createCell(row, columnCount++, tv.getHoTen(), style);
                    createCell(row, columnCount++, tv.getKhoa() == null ? "" : tv.getKhoa(), style);
                    createCell(row, columnCount++, tv.getNganh() == null ? "" : tv.getNganh(), style);
                    createCell(row, columnCount++, tv.getSDT() == null ? "" : tv.getSDT(), style);
                    createCell(row, columnCount++, tv.getPassword(), style);
                    createCell(row, columnCount++, tv.getEmail() == null ? "" : tv.getEmail(), style);

                }
                break;
            case "TTSD":
                Iterable<ThongTinSD> listTTSD = ttsdService.GetList();
                for (ThongTinSD ttsd : listTTSD) {
                    Row row = sheet.createRow(rowCount++);
                    int columnCount = 0;

                    createCell(row, columnCount++, ttsd.getMaTT(), style);
                    createCell(row, columnCount++, ttsd.getThanhvien() == null ? "" : ttsd.getThanhvien().getMaTV(),
                            style);
                    createCell(row, columnCount++, ttsd.getThietbi() == null ? "" : ttsd.getThietbi().getMaTB(), style);
                    createCell(row, columnCount++, ttsd.getTGVao() == null ? "" : ttsd.getTGVao(), style);
                    createCell(row, columnCount++, ttsd.getTGMuon() == null ? "" : ttsd.getTGMuon(), style);
                    createCell(row, columnCount++, ttsd.getTGTra() == null ? "" : ttsd.getTGTra(), style);
                    createCell(row, columnCount++, ttsd.getTGDatcho() == null ? "" : ttsd.getTGDatcho(), style);

                }
                break;
            case "XuLy":
                Iterable<XuLy> listXuLy = xlService.GetList();
                for (XuLy xl : listXuLy) {
                    Row row = sheet.createRow(rowCount++);
                    int columnCount = 0;

                    createCell(row, columnCount++, xl.getMaXL(), style);
                    createCell(row, columnCount++, xl.getThanhvien() == null ? "" : xl.getThanhvien().getMaTV(), style);
                    createCell(row, columnCount++, xl.getHinhThucXL() == null ? "" : xl.getHinhThucXL(), style);
                    createCell(row, columnCount++, xl.getSoTien() == null ? "" : xl.getSoTien(), style);
                    createCell(row, columnCount++, xl.getNgayXL() == null ? "" : xl.getNgayXL(), style);
                    createCell(row, columnCount++, xl.getTrangThaiXL() == null ? "" : xl.getTrangThaiXL(), style);

                }
                break;
            case "ThietBi":
                Iterable<ThietBi> listThietBi = tbService.GetList();
                for (ThietBi tb : listThietBi) {
                    Row row = sheet.createRow(rowCount++);
                    int columnCount = 0;

                    createCell(row, columnCount++, tb.getMaTB(), style);
                    createCell(row, columnCount++, tb.getTenTB(), style);
                    createCell(row, columnCount++, tb.getMoTaTB() == null ? "" : tb.getMoTaTB(), style);

                }
                break;

            default:
                break;
        }

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            // Date to string
            Date converted = (Date) value;
            cell.setCellValue(converted.toString());
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public void export(HttpServletResponse response) throws IOException {
        init();
        writeHeaderLine("ThanhVien");
        writeDataLines("ThanhVien");
        writeHeaderLine("ThietBi");
        writeDataLines("ThietBi");
        writeHeaderLine("TTSD");
        writeDataLines("TTSD");
        writeHeaderLine("XuLy");
        writeDataLines("XuLy");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
