package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableDefault;

/**
 *
 * @author Admin
 */
@Repository
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD, Integer>, 
JpaRepository<ThongTinSD, Integer>, PagingAndSortingRepository<ThongTinSD, Integer> {
    public Iterable<ThongTinSD> findAllByOrderByMaTTDesc();
    public Page<ThongTinSD> findAllBytGVaoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable); // lay thong tin SD kem thoi gian vao
    public Page<ThongTinSD> findAllBytGDatchoNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);
    public Page<ThongTinSD> findAllBytGMuonNotNull(@PageableDefault(value = 10, page = 0) Pageable pageable);
}
