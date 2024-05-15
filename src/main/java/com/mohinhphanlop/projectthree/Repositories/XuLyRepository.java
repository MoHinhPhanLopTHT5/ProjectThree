package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.XuLy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface XuLyRepository
                extends CrudRepository<XuLy, Integer>, JpaRepository<XuLy, Integer>,
                PagingAndSortingRepository<XuLy, Integer> {
        // danh sách vi phạm
        // (1) theo số tiền
        // 1 - theo ngày xử lý
        // 1 - theo tên thành viên
        public Page<XuLy> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable);

        @Query("SELECT x FROM XuLy x INNER JOIN x.thanhvien tv WHERE (:matv = -1 or tv.maTV = :matv) and (:soTien = -1 or x.soTien >= :soTien) and tv.hoTen like %:hoten% and x.hinhThucXL like %:hinhthucxl% and (:trangthaixl = -1 or x.trangThaiXL = :trangthaixl)")
        public Page<XuLy> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("matv") Integer matv,
                        @Param("soTien") Integer soTien, @Param("hoten") String hoten,
                        @Param("hinhthucxl") String hinhthucxl, @Param("trangthaixl") Integer trangthaixl);

        @Query("SELECT x FROM XuLy x INNER JOIN x.thanhvien tv WHERE (:matv = -1 or tv.maTV = :matv) and (:soTien = -1 or x.soTien >= :soTien) and tv.hoTen like %:hoten% and x.hinhThucXL like %:hinhthucxl% and date(x.ngayXL) = :ngayxl and (:trangthaixl = -1 or x.trangThaiXL = :trangthaixl)")
        public Page<XuLy> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("matv") Integer matv,
                        @Param("soTien") Integer soTien, @Param("hoten") String hoten,
                        @Param("hinhthucxl") String hinhthucxl,
                        @Param("ngayxl") Date ngayxl, @Param("trangthaixl") Integer trangthaixl);

        @Query("SELECT x FROM XuLy x INNER JOIN x.thanhvien tv WHERE tv.maTV = :id and x.trangThaiXL = 1 ORDER BY x.ngayXL DESC")
        public Iterable<XuLy> findByThanhVienId(@Param("id") Integer id);

        @Query("SELECT x FROM XuLy x INNER JOIN x.thanhvien tv WHERE tv.maTV = :id")
        public Page<XuLy> findAllByThanhVienId(@PageableDefault(value = 10, page = 0) Pageable pageable,
                        @Param("id") Integer id);
}
