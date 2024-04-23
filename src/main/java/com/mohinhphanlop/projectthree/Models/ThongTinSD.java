/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;
import java.util.Date;
import jakarta.persistence.*;
import java.math.BigInteger;
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
    private Integer MaTT;
    
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TGVao;
    
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TGMuon;
    
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TGTra;
    
    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date TGDatcho;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTV", nullable = false)
    private ThanhVien thanhvien;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTB", nullable = true)
    private ThietBi thietbi;
}
