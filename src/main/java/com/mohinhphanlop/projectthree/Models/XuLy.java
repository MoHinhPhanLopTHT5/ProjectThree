package com.mohinhphanlop.projectthree.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ThanhVien thanhvien;
}
