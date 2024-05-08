package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThanhVien;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ThanhVienRepository extends CrudRepository<ThanhVien, Long> {

    @Query("SELECT tv FROM ThanhVien tv WHERE SUBSTRING(CAST(tv.maTV AS string), 3, 2) = :year ORDER BY SUBSTRING(CAST(tv.maTV AS string), 7, 4) DESC")
    List<ThanhVien> GetListThanhVienTheoNam(String year);

    @Query("SELECT TV.maTV FROM ThanhVien TV WHERE TV.maTV NOT IN (SELECT TT.thanhvien.maTV FROM ThongTinSD TT WHERE tGTra IS NULL AND tGVao IS NULL) AND TV.maTV NOT IN (SELECT XL.thanhvien.maTV FROM XuLy XL WHERE trangThaiXL = 0) AND SUBSTRING(CAST(TV.maTV AS string),3,2) = :lastSchoolYear")
    List<Integer> GetIDHopLe(String lastSchoolYear);
}
