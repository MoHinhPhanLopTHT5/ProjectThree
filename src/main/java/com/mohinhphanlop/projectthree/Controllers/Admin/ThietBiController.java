package com.mohinhphanlop.projectthree.Controllers.Admin;

import br.com.objectos.way.xls.Workbook;
import br.com.objectos.way.xls.pojo.Row;
import br.com.objectos.way.xls.pojo.Sheet;
import static com.mohinhphanlop.projectthree.Controllers.MainController.TryParseInt;
import com.mohinhphanlop.projectthree.Models.ThietBi;
import com.mohinhphanlop.projectthree.Services.ThietBiService;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/quantri/thietbi")
public class ThietBiController {
    @Autowired
    private ThietBiService tbService;

    @GetMapping("") // trang chủ kết hợp với tìm kiếm theo tên
    public String getQuanLyThietBi(Model model, @RequestParam("page") Optional<Integer> page,
            @RequestParam("tenTB") Optional<String> tenTB,
            Pageable pageable) {
        String TenTB = tenTB.orElse("");
        Page<ThietBi> list = tbService.GetList(TenTB, pageable);
        model.addAttribute("data", list);
        model.addAttribute("TenTB", TenTB);
        return "admin/thietbi/index";
    }

    @GetMapping("/sua/{id}") // cập nhật thiết bị
    public String getUpdateThietBi(@PathVariable("id") Integer maTB, Model model) {
        ThietBi thietBi = tbService.GetByID(maTB.toString());
        model.addAttribute("thietBi", thietBi);
        model.addAttribute("id", maTB);
        return "admin/thietbi/update";
    }

    @PostMapping("/sua/{id}")
    public String postUpdateThietBi(Model model, @ModelAttribute("thietBi") ThietBi thietBi,
            @PathVariable("id") Integer maTB) {
        tbService.updateThietBi(thietBi);
        model.addAttribute("success", "Cập nhật thiết bị thành công!");
        return "admin/thietbi/update";
    }

    @GetMapping("/xoa/{id}") // xóa thiết bị
    public String getDeleteThietBi(@PathVariable("id") Integer maTB, Model model) {
        ThietBi thietBi = tbService.GetByID(maTB.toString());
        model.addAttribute("thietBi", thietBi);
        return "admin/thietbi/delete";
    }

    @PostMapping("/xoa/{id}")
    public String postDeleteThietBi(@PathVariable("id") Integer maTB, @ModelAttribute("thietBi") ThietBi thietBi,
            Model model) {
        if (tbService.deleteThietBi(thietBi)) {
            model.addAttribute("success", "Xoá thiết bị thành công");
        } else {
            model.addAttribute("error", "Xoá thiết bị thất bại do thiết bị đang được mượn hoặc đặt chỗ!");
        }
        return "admin/thietbi/delete";
    }

    private int tongSoLuongTBThem;

    @GetMapping("/soLuongTB") // thêm thiết bị
    public String getSoLuongThietBi() {
        return "admin/thietbi/soluongthietbi";
    }

    @PostMapping("/requestSoLuongTB")
    public String postSoLuongThietBi(@RequestParam("quantity") int quantity) {
        tongSoLuongTBThem = quantity;
        return "admin/thietbi/create";
    }

    @GetMapping("/taomoi")
    public String getCreateThietBi(Model model) {
        // if (tongSoLuongTBThem > 0) {
        // ThietBi thietBi = new ThietBi();
        // model.addAttribute("thietBi", thietBi);
        // tongSoLuongTBThem--;
        // return "/quantri/thietbi/create";
        // } else
        // return "/quantri/thietbi/index";

        return "admin/thietbi/create";
    }

    @PostMapping("/taomoi")
    public String postCreateThietBi(@RequestBody MultiValueMap<String, String> formData, Model model) {
        String requestedId = formData.getFirst("maTB");
        String name = formData.getFirst("tenTB");
        String discription = formData.getFirst("moTaTB");

        Integer id = TryParseInt(requestedId);

        model.addAttribute("maTB", requestedId);
        model.addAttribute("tenTB", name);
        model.addAttribute("moTaTB", discription);

        if (tbService.isExistMaThietBi(requestedId)) {
            model.addAttribute("error", "Mã Thiết bị đã tồn tại không thể thêm!");
            tongSoLuongTBThem += 1;
        } else {
            tbService.CreateThietBi(id, name, discription);
            model.addAttribute("success", "Thêm thiết bị thành công!");
        }
        return "admin/thietbi/create";
    }

    @GetMapping("/excel")
    public String getExcelImport() {
        return "/admin/thietbi/excel";
    }

    @PostMapping("/excel")
    public String postExcelImport(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Vui lòng chọn file Excel để import");
            return "admin/thietbi/excel";
        }

        try (InputStream inputStream = file.getInputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(1); // sheet đầu tiên
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    String discrip = row.getCell(2).getStringCellValue();
                    tbService.CreateThietBi(id, name, discrip);
                }
            }
            model.addAttribute("success", "Import thành công!");
            workbook.close();
        } catch (Exception e) {
            model.addAttribute("error", "Import thất bại!" + e.getMessage());
        }

        return "admin/thietbi/excel";
    }

    @GetMapping("/xoanhieu")
    public String getXoaNhieuThietBi() {
        return "admin/thietbi/deletemultiple";
    }

    @PostMapping("/xoanhieu")
    public String postXoaNhieuThietBi(@RequestParam("deviceID") String deviceID, Model model) {
        Iterable<ThietBi> list = tbService.GetList();
        List<ThietBi> listTBKoXoaDuoc = tbService.deleteNhieuThietBi(deviceID, list);

        if (listTBKoXoaDuoc.size() > 0) {
            model.addAttribute("listTBKoXoaDuoc", listTBKoXoaDuoc);
            model.addAttribute("error",
                    "Có một số thiết bị không thể xoá do đang được mượn hoặc đặt chỗ, hãy kiểm tra danh sách sau:");
        } else {
            model.addAttribute("success", "Xoá các thiết bị thành công!");
        }

        return "admin/thietbi/deletemultiple";
    }
}