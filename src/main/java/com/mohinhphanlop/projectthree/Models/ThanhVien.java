/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "ThanhVien")
@Table(name = "thanhvien")
public class ThanhVien {

    @Id
    private Integer maTV;

    @Column(name = "hoten")
    private String hoTen;

    private String khoa;

    @Column(nullable = true)
    private String nganh;

    @Column(nullable = true)
    private String sDT;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhvien", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ThongTinSD> DS_ThongTinSD;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thanhvien", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<XuLy> DS_XuLy;
}
