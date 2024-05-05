/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@Entity(name = "XuLy")
@Table(name = "xuly")
public class XuLy {

    @Id
    private Integer maXL;

    @Column(name = "hinhthucxl")
    private String hinhThucXL;

    @Column(name = "sotien")
    private Integer soTien;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date ngayXL;

    @Column(name = "trangthaixl")
    private Integer trangThaiXL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTV", nullable = false)
    @JsonBackReference
    private ThanhVien thanhvien;
}
