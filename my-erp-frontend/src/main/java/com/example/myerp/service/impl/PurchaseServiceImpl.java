// my-erp-frontend/src/main/java/com/example/myerp/service/impl/PurchaseServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Purchase;
import com.example.myerp.repository.PurchaseRepository;
import com.example.myerp.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase save(Purchase purchase) {
        purchase.setCreateTime(LocalDateTime.now());
        if (purchase.getPurchaseDate() == null) purchase.setPurchaseDate(LocalDateTime.now());
        if (purchase.getPurchaseStatus() == null) purchase.setPurchaseStatus("待审核");
        return purchaseRepository.save(purchase);
    }

    @Override
    public Purchase update(Long id, Purchase purchase) {
        Purchase existing = purchaseRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setPurchaseNo(purchase.getPurchaseNo());
        existing.setProductName(purchase.getProductName());
        existing.setSupplierName(purchase.getSupplierName());
        existing.setPurchaseAmount(purchase.getPurchaseAmount());
        existing.setPurchaseQuantity(purchase.getPurchaseQuantity());
        existing.setPurchaseDate(purchase.getPurchaseDate());
        existing.setPurchaser(purchase.getPurchaser());
        existing.setPurchaseStatus(purchase.getPurchaseStatus());
        existing.setRemark(purchase.getRemark());
        return purchaseRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!purchaseRepository.existsById(id)) return false;
        purchaseRepository.deleteById(id);
        return true;
    }

    @Override
    public Purchase findById(Long id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    @Override
    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}
