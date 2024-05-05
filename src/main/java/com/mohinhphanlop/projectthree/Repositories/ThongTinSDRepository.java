package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

/**
 *
 * @author Admin
 */
@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD, Integer>, 
JpaRepository<ThongTinSD, Integer>, PagingAndSortingRepository<ThongTinSD, Integer> {
    public Iterable<ThongTinSD> findAllByOrderByMaTTDesc();
    public Page<ThongTinSD> findAllBytGDatchoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);
    
    // danh sách thành viên vào
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable); // lay thong tin SD kem thoi gian vao
    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv WHERE tv.khoa like %:khoa% AND tv.nganh like %:nganh%")
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("khoa") String khoa, @Param("nganh") String nganh);
    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv WHERE tv.khoa like %:khoa% AND tv.nganh like %:nganh% and Date(t.tGVao) = %:tgvao%")
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("khoa") String khoa, @Param("nganh") String nganh, @Param("tgvao") Date tgvao);

    // danh sách thiết bị mượn
    // 1     method để lấy toàn bộ thiết bị
    // (1) toàn bộ - đã có, 
//     // ++ tên thiết bị (1), thời gian mượn (1), thời gian trả (1), (1) chung của mượn trả 
//     @Query("SELECT t FROM ThongTinSD t INNER JOIN t.ThietBi tv WHERE tv.tenTB like %:tenTB% AND Date(t.tGMuon) = %:tgmuon% and Date(t.tGTra) = %:tgtra%")
//     public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("tenTB") String tenTB, @Param("tgmuon") Date tgmuon, @Param("tgtra") Date tgtra);
//      @Query("SELECT t FROM ThongTinSD t INNER JOIN t.ThietBi tv WHERE tv.tenTB like %:tenTB% and Date(t.tGMuon) = %:tgmuon%" )
//      public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("tenTB") String tenTB , @Param("tgmuon") Date tgmuon);
//     @Query("SELECT t FROM ThongTinSD t INNER JOIN t.ThietBi tv WHERE tv.tenTB like %:tenTB%")
//     public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("tenTB") String tenTB);
//     @Query("SELECT t FROM ThongTinSD t INNER JOIN t.ThietBi tv WHERE tv.tenTB like %:tenTB% AND Date(t.tGtra) = %:tgtra%")
//     // public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("tenTB") String tenTB, @Param("tgtra") Date tgtra);
//     // public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable, @Param("tenTB") String tenTB , @Param("tgtra") Date tgtra);

//     public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);
// }
