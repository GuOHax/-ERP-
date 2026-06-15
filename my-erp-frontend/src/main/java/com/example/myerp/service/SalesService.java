// my-erp-frontend/src/main/java/com/example/myerp/service/SalesService.java
package com.example.myerp.service;

import com.example.myerp.entity.Sales;
import java.util.List;

public interface SalesService {
    Sales save(Sales sales);
    Sales update(Long id, Sales sales);
    boolean delete(Long id);
    Sales findById(Long id);
    List<Sales> findAll();
}
