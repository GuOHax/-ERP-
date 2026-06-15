// my-erp-frontend/src/main/java/com/example/myerp/service/InventoryService.java
package com.example.myerp.service;

import com.example.myerp.entity.Inventory;
import java.util.List;

public interface InventoryService {
    Inventory save(Inventory inventory);
    Inventory update(Long id, Inventory inventory);
    boolean delete(Long id);
    Inventory findById(Long id);
    List<Inventory> findAll();
}
