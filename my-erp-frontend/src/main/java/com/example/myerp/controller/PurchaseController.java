// my-erp-frontend/src/main/java/com/example/myerp/controller/PurchaseController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Purchase;
import com.example.myerp.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/add")
    public Result<Purchase> add(@RequestBody Purchase purchase) {
        Purchase saved = purchaseService.save(purchase);
        return saved != null
            ? Result.success(saved, "添加采购记录成功")
            : Result.error("添加采购记录失败");
    }

    @PutMapping("/update/{id}")
    public Result<Purchase> update(@PathVariable Long id, @RequestBody Purchase purchase) {
        Purchase updated = purchaseService.update(id, purchase);
        return updated != null
            ? Result.success(updated, "更新采购记录成功")
            : Result.error("更新采购记录失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return purchaseService.delete(id)
            ? Result.success(null, "删除采购记录成功")
            : Result.error("删除采购记录失败");
    }

    @GetMapping("/list")
    public Result<List<Purchase>> list() {
        return Result.success(purchaseService.findAll(), "查询成功");
    }

    @GetMapping("/get/{id}")
    public Result<Purchase> get(@PathVariable Long id) {
        Purchase purchase = purchaseService.findById(id);
        return purchase != null
            ? Result.success(purchase, "查询成功")
            : Result.error("未找到该采购记录");
    }
}
