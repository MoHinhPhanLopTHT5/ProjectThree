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
public interface ThongTinSDRepository extends CrudRepository<ThongTinSD, Integer>, JpaRepository<ThongTinSD, Integer> {
    public Iterable<ThongTinSD> findAllByOrderByMaTTDesc();
}
