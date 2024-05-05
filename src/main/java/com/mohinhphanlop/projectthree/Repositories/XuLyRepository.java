package com.mohinhphanlop.projectthree.Repositories;

import com.mohinhphanlop.projectthree.Models.ThongTinSD;
import com.mohinhphanlop.projectthree.Models.XuLy;

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
public interface XuLyRepository extends CrudRepository<XuLy, Integer>, JpaRepository<XuLy, Integer>, PagingAndSortingRepository<XuLy, Integer> {
    // danh sách vi phạm
    // (1) theo số tiền
    // 1 - theo ngày xử lý
    // 1 - theo tên thành viên
    public Page<XuLy> findAll(@PageableDefault(value = 10, page = 0) Pageable pageable);
    
}
