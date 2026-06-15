// my-erp-frontend/src/main/java/com/example/myerp/entity/Employee.java
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
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "em_id")
    private Long emId;

    @Column(name = "em_no", length = 20, nullable = false, unique = true)
    private String emNo;             // 员工编号

    @Column(name = "em_name", length = 20)
    private String emName;           // 姓名

    @Column(name = "em_pwd", length = 100)
    private String emPwd;            // 密码

    @Column(name = "em_tel", length = 11)
    private String emTel;            // 电话

    @Column(name = "em_email", length = 50)
    private String emEmail;          // 邮箱

    @Column(name = "em_icon", length = 100)
    private String emIcon;           // 头像

    @Column(name = "em_roleId")
    private Long emRoleId;           // 所属角色 ID

    @Column(name = "em_remark", columnDefinition = "TEXT")
    private String emRemark;         // 备注

    @Column(name = "em_status")
    private Integer emStatus;        // 状态（0=禁用，1=启用）

    @Column(name = "em_sex")
    private Integer emSex;           // 性别（0=未知，1=男，2=女）

    @Column(name = "em_createTime")
    private LocalDateTime emCreateTime; // 创建时间

    @Column(name = "em_storeId")
    private Long emStoreId;          // 门店 ID

    @Column(name = "dept_id")
    private Integer deptId;          // 所属部门 ID
}
