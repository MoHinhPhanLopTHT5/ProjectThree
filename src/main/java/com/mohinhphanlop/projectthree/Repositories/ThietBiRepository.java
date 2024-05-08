package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThietBi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ThietBiRepository
                extends PagingAndSortingRepository<ThietBi, Integer>, CrudRepository<ThietBi, Integer> {

        Page<ThietBi> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable);

        Page<ThietBi> findAllByTenTB(String tenTB, @PageableDefault(value = 10, page = 0) Pageable pageable);
        
}
