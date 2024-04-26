/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "ThanhVien")
@Table(name = "thanhvien")
public class ThanhVien {

    @Id
    private Integer MaTV;

    @Column(name = "hoten")
    private String HoTen;

    private String Khoa;

    @Column(nullable = true)
    private String Nganh;

    @Column(nullable = true)
    private String SDT;

    @Column(nullable = false)
    private String Password;

    @Column(nullable = true)
    private String Email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhvien")
    private List<ThongTinSD> DS_ThongTinSD;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhvien")
    private List<XuLy> DS_XuLy;
}
