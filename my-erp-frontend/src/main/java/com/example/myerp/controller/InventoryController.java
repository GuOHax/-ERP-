// my-erp-frontend/src/main/java/com/example/myerp/controller/InventoryController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Inventory;
import com.example.myerp.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public Result<Inventory> add(@RequestBody Inventory inventory) {
        Inventory saved = inventoryService.save(inventory);
        return saved != null
            ? Result.success(saved, "添加库存记录成功")
            : Result.error("添加库存记录失败");
    }

    @PutMapping("/update/{id}")
    public Result<Inventory> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updated = inventoryService.update(id, inventory);
        return updated != null
            ? Result.success(updated, "更新库存记录成功")
            : Result.error("更新库存记录失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return inventoryService.delete(id)
            ? Result.success(null, "删除库存记录成功")
            : Result.error("删除库存记录失败");
    }

    @GetMapping("/list")
    public Result<List<Inventory>> list() {
        return Result.success(inventoryService.findAll(), "查询成功");
    }

    @GetMapping("/get/{id}")
    public Result<Inventory> get(@PathVariable Long id) {
        Inventory inventory = inventoryService.findById(id);
        return inventory != null
            ? Result.success(inventory, "查询成功")
            : Result.error("未找到该库存记录");
    }
}
