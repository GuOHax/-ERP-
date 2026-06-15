// my-erp-frontend/src/main/java/com/example/myerp/entity/Role.java
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
@Table(name = "sys_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Column(name = "role_key", length = 50)
    private String roleKey;

    @Column(name = "role_sort")
    private Integer roleSort;

    @Column(name = "role_status")
    private Integer roleStatus;

    @Column(name = "role_remark", columnDefinition = "TEXT")
    private String roleRemark;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
