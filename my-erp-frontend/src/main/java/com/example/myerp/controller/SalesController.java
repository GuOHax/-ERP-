// my-erp-frontend/src/main/java/com/example/myerp/controller/SalesController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Sales;
import com.example.myerp.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/add")
    public Result<Sales> add(@RequestBody Sales sales) {
        Sales saved = salesService.save(sales);
        return saved != null
            ? Result.success(saved, "添加销售记录成功")
            : Result.error("添加销售记录失败");
    }

    @PutMapping("/update/{id}")
    public Result<Sales> update(@PathVariable Long id, @RequestBody Sales sales) {
        Sales updated = salesService.update(id, sales);
        return updated != null
            ? Result.success(updated, "更新销售记录成功")
            : Result.error("更新销售记录失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return salesService.delete(id)
            ? Result.success(null, "删除销售记录成功")
            : Result.error("删除销售记录失败");
    }

    @GetMapping("/list")
    public Result<List<Sales>> list() {
        return Result.success(salesService.findAll(), "查询成功");
    }

    @GetMapping("/get/{id}")
    public Result<Sales> get(@PathVariable Long id) {
        Sales sales = salesService.findById(id);
        return sales != null
            ? Result.success(sales, "查询成功")
            : Result.error("未找到该销售记录");
    }
}
