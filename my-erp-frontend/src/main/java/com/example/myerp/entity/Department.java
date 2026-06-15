package com.example.myerp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deptinfo")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deptid")  // 修改为全小写
    private Integer deptID;

    @Column(name = "deptname", length = 20)  // 字段名改为小写
    private String deptName;

    @Column(name = "empcount")  // 字段名改为小写
    private Integer empCount;

    @Column(name = "parentid")  // 字段名改为小写
    private Integer parentID;

    @Column(name = "createtime")
    private LocalDateTime createTime;

    @Column(name = "deptorder")  // 字段名改为小写
    private Integer deptOrder;

    @Column(name = "isshow")
    private Boolean isShow;
}