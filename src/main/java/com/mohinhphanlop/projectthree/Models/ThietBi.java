/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mohinhphanlop.projectthree.Models;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;
import java.math.BigInteger;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@Entity(name = "ThietBi")
@Table(name = "thietbi")
public class ThietBi {
    @Id
    private Integer MaTB;

    private String TenTB;

    @Column(name = "motatb")
    private String MoTaTB;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thietbi")
    private List<ThongTinSD> DS_ThongTinSD;

    @Transient
    private boolean occupied;
}
