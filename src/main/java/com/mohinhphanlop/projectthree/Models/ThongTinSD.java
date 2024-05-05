package com.mohinhphanlop.projectthree.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private ThanhVien thanhvien;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaTB", nullable = true)
    @JsonBackReference
    private ThietBi thietbi;
}
