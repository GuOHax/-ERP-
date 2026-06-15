// my-erp-frontend/src/main/java/com/example/myerp/entity/Inventory.java
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
@Table(name = "erp_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "inventory_quantity")
    private Integer inventoryQuantity;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "inventory_amount", precision = 12, scale = 2)
    private BigDecimal inventoryAmount;

    @Column(name = "warehouse", length = 50)
    private String warehouse;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
