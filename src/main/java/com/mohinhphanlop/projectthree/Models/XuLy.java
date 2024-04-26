/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MaXL;

    @Column(name = "hinhthucxl")
    private String HinhThucXL;

    @Column(name = "sotien")
    private Integer SoTien;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date NgayXL;

    @Column(name = "trangthaixl")
    private Integer TrangThaiXL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTV", nullable = false)
    private ThanhVien thanhvien;
}
