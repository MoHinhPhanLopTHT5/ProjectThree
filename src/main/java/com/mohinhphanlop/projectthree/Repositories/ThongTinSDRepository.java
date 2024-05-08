package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD, Integer>,
        JpaRepository<ThongTinSD, Integer>, PagingAndSortingRepository<ThongTinSD, Integer> {
    public Iterable<ThongTinSD> findAllByOrderByMaTTDesc();

    public Iterable<ThongTinSD> findAllBythietbiNotNull();

    // danh sách thành viên vào
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);
    // lay thong tin SD kem thoi gian vao

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv WHERE (:matv = -1 or tv.maTV = :matv) AND tv.hoTen like %:hoten% AND tv.khoa like %:khoa% AND tv.nganh like %:nganh% and t.tGVao is not null")
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("matv") Integer matv, @Param("hoten") String hoten, @Param("khoa") String khoa,
            @Param("nganh") String nganh);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv WHERE (:matv = -1 or tv.maTV = :matv) AND tv.hoTen like %:hoten% AND tv.khoa like %:khoa% AND tv.nganh like %:nganh% and Date(t.tGVao) = %:tgvao%")
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("matv") Integer matv, @Param("hoten") String hoten, @Param("khoa") String khoa,
            @Param("nganh") String nganh, @Param("tgvao") Date tgvao);

    // danh sách thiết bị mượn
    // 1 method để lấy toàn bộ thiết bị
    // (1) toàn bộ - đã có,
    // // ++ tên thiết bị (1), thời gian mượn (1), thời gian trả (1), (1) chung của
    // mượn trả

    public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE t.tGMuon is not null and (:maTB = -1 or tb.maTB = :maTB) and tb.tenTB like %:tenTB%")
    public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("maTB") Integer maTB, @Param("tenTB") String tenTB);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE (:maTB = -1 or tb.maTB = :maTB) and tb.tenTB like %:tenTB% and Date(t.tGMuon) = %:tgmuon%")
    public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("maTB") Integer maTB, @Param("tenTB") String tenTB, @Param("tgmuon") Date tgmuon);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE (:maTB = -1 or tb.maTB = :maTB) and tb.tenTB like %:tenTB% and Date(t.tGTra) = %:tgtra%")
    public Page<ThongTinSD> findAllBytGTraNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("maTB") Integer maTB, @Param("tenTB") String tenTB, @Param("tgtra") Date tgtra);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE (:maTB = -1 or tb.maTB = :maTB) and tb.tenTB like %:tenTB% AND Date(t.tGMuon) = %:tgmuon% AND Date(t.tGTra) = %:tgtra%")
    public Page<ThongTinSD> findAllBytGMuonNotNullAndtGTraNotNull(
            @PageableDefault(value = 10, page = 0) Pageable pageable, @Param("maTB") Integer maTB,
            @Param("tenTB") String tenTB,
            @Param("tgmuon") Date tgmuon, @Param("tgtra") Date tgtra);

    // Danh sách thiết bị đặt chỗ

    public Page<ThongTinSD> findAllBytGDatchoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv INNER JOIN t.thietbi tb WHERE (:maTB = -1 or tb.maTB = :maTB) and (:maTV = -1 or tv.maTV = :maTV) and tb.tenTB like %:tenTB% and tv.hoTen like %:hoTen% AND t.tGDatcho is not null")
    public Page<ThongTinSD> findAllBytGDatchoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("maTB") Integer maTB, @Param("maTV") Integer maTV,
            @Param("tenTB") String tenTB, @Param("hoTen") String hoTen);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thanhvien tv INNER JOIN t.thietbi tb WHERE (:maTB = -1 or tb.maTB = :maTB) and (:maTV = -1 or tv.maTV = :maTV) and tb.tenTB like %:tenTB% and tv.hoTen like %:hoTen% AND Date(t.tGDatcho) = %:tgDatcho%")
    public Page<ThongTinSD> findAllBytGDatchoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable,
            @Param("maTB") Integer maTB, @Param("maTV") Integer maTV,
            @Param("tenTB") String tenTB, @Param("hoTen") String hoTen, @Param("tgDatcho") Date tgDatcho);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE tb.maTB = :maTB AND t.tGDatcho is not null")
    public Iterable<ThongTinSD> findByMaTBAndtGDatchoNotNull(@Param("maTB") Integer maTB);

    @Query("SELECT t FROM ThongTinSD t INNER JOIN t.thietbi tb WHERE tb.maTB = :maTB AND date(t.tGDatcho) = :tgDatcho")
    public ThongTinSD findByMaTBAndtGDatchoEquals(@Param("maTB") Integer maTB,
            @Param("tgDatcho") Date tgDatcho);

    @Query("SELECT t FROM ThongTinSD t WHERE t.maTT = :maTT AND t.tGDatcho is not null")
    public ThongTinSD findBymaTTAndtGDatchoNotNull(@Param("maTT") Integer maTT);
}
