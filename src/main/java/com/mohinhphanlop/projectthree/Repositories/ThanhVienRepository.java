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
}
