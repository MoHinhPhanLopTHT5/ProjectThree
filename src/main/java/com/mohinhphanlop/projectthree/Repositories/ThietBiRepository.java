package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThietBi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
public interface ThietBiRepository
                extends JpaRepository<ThietBi, Integer>,
                PagingAndSortingRepository<ThietBi, Integer>, CrudRepository<ThietBi, Integer> {

        Page<ThietBi> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable);

        @Query("SELECT t FROM ThietBi t WHERE t.tenTB LIKE %:tenTB%")
        Page<ThietBi> findAllByTenTB(@Param("tenTB") String tenTB,
                        @PageableDefault(value = 10, page = 0) Pageable pageable);
}
