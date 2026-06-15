// my-erp-frontend/src/main/java/com/example/myerp/entity/Sales.java
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
@Table(name = "erp_sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    private Long salesId;

    @Column(name = "sales_no", length = 30, nullable = false)
    private String salesNo;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "sales_amount", precision = 12, scale = 2)
    private BigDecimal salesAmount;

    @Column(name = "sales_quantity")
    private Integer salesQuantity;

    @Column(name = "sales_date")
    private LocalDateTime salesDate;

    @Column(name = "sales_man", length = 50)
    private String salesMan;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
