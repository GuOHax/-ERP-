// my-erp-frontend/src/main/java/com/example/myerp/service/PurchaseService.java
package com.example.myerp.service;

import com.example.myerp.entity.Purchase;
import java.util.List;

public interface PurchaseService {
    Purchase save(Purchase purchase);
    Purchase update(Long id, Purchase purchase);
    boolean delete(Long id);
    Purchase findById(Long id);
    List<Purchase> findAll();
}
