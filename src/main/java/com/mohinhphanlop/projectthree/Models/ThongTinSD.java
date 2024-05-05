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
@Entity(name = "ThongTinSD")
@Table(name = "thongtinsd")
public class ThongTinSD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTT;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tGVao;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tGMuon;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tGTra;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tGDatcho;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTV", nullable = false)
    private ThanhVien thanhvien;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTB", nullable = true)
    private ThietBi thietbi;
}
