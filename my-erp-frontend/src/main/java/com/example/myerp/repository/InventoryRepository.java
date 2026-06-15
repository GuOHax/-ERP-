// my-erp-frontend/src/main/java/com/example/myerp/repository/InventoryRepository.java
package com.example.myerp.repository;

import com.example.myerp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
