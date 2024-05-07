package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThietBi;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ThietBiRepository
        extends PagingAndSortingRepository<ThietBi, Integer>, CrudRepository<ThietBi, Integer> {
    @Query("SELECT TB FROM ThietBi TB WHERE TB.maTB NOT IN (SELECT TT.thietbi.maTB FROM ThongTinSD TT WHERE ((TT.tGMuon IS NOT NULL AND TT.tGTra IS NULL) OR (:tomorrow > TT.tGDatcho AND :datenow < TT.tGDatcho)) and TT.thietbi.maTB IS NOT NULL)")
    List<ThietBi> DSThietBiHopLe(Date tomorrow, LocalDateTime datenow);
}
