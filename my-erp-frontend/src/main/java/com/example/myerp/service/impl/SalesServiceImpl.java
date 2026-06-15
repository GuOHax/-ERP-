// my-erp-frontend/src/main/java/com/example/myerp/service/impl/SalesServiceImpl.java
package com.example.myerp.service.impl;

import com.example.myerp.entity.Sales;
import com.example.myerp.repository.SalesRepository;
import com.example.myerp.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Override
    public Sales save(Sales sales) {
        sales.setCreateTime(LocalDateTime.now());
        if (sales.getSalesDate() == null) sales.setSalesDate(LocalDateTime.now());
        return salesRepository.save(sales);
    }

    @Override
    public Sales update(Long id, Sales sales) {
        Sales existing = salesRepository.findById(id).orElse(null);
        if (existing == null) return null;
        existing.setSalesNo(sales.getSalesNo());
        existing.setProductName(sales.getProductName());
        existing.setCustomerName(sales.getCustomerName());
        existing.setSalesAmount(sales.getSalesAmount());
        existing.setSalesQuantity(sales.getSalesQuantity());
        existing.setSalesDate(sales.getSalesDate());
        existing.setSalesMan(sales.getSalesMan());
        existing.setRemark(sales.getRemark());
        return salesRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!salesRepository.existsById(id)) return false;
        salesRepository.deleteById(id);
        return true;
    }

    @Override
    public Sales findById(Long id) {
        return salesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Sales> findAll() {
        return salesRepository.findAll();
    }
}
