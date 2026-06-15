// my-erp-frontend/src/main/java/com/example/myerp/entity/Purchase.java
package com.example.myerp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "erp_purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "purchase_no", length = 30, nullable = false)
    private String purchaseNo;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "supplier_name", length = 100)
    private String supplierName;

    @Column(name = "purchase_amount", precision = 12, scale = 2)
    private BigDecimal purchaseAmount;

    @Column(name = "purchase_quantity")
    private Integer purchaseQuantity;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "purchaser", length = 50)
    private String purchaser;

    @Column(name = "purchase_status", length = 20)
    private String purchaseStatus;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
