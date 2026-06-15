// my-erp-frontend/src/main/java/com/example/myerp/service/impl/InventoryServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Inventory;
import com.example.myerp.repository.InventoryRepository;
import com.example.myerp.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Inventory save(Inventory inventory) {
        inventory.setCreateTime(LocalDateTime.now());
        if (inventory.getInventoryQuantity() == null) inventory.setInventoryQuantity(0);
        if (inventory.getUnitPrice() != null && inventory.getInventoryQuantity() != null) {
            inventory.setInventoryAmount(
                inventory.getUnitPrice().multiply(
                    new java.math.BigDecimal(inventory.getInventoryQuantity())
                )
            );
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory update(Long id, Inventory inventory) {
        Inventory existing = inventoryRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setProductName(inventory.getProductName());
        existing.setProductCode(inventory.getProductCode());
        existing.setInventoryQuantity(inventory.getInventoryQuantity());
        existing.setUnitPrice(inventory.getUnitPrice());
        existing.setWarehouse(inventory.getWarehouse());
        existing.setCategory(inventory.getCategory());
        existing.setRemark(inventory.getRemark());
        if (existing.getUnitPrice() != null && existing.getInventoryQuantity() != null) {
            existing.setInventoryAmount(
                existing.getUnitPrice().multiply(
                    new java.math.BigDecimal(existing.getInventoryQuantity())
                )
            );
        }
        return inventoryRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!inventoryRepository.existsById(id)) return false;
        inventoryRepository.deleteById(id);
        return true;
    }

    @Override
    public Inventory findById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
}
