package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.XuLy;
import java.util.List;
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
public interface XuLyRepository extends PagingAndSortingRepository<XuLy, Integer>, JpaRepository<XuLy, Integer> {
    @Query(value="SELECT * FROM xuly xl WHERE xl.hinhThucXL LIKE %:keyword%",
            nativeQuery = true)
    List<XuLy> findByKeyword(@Param("keyword") String keyword);
    
    Page<XuLy> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable);
    Page<XuLy> findAllByHinhThucXL(String hinhThucXL, @PageableDefault(value = 10, page = 0) Pageable pageable);

}
