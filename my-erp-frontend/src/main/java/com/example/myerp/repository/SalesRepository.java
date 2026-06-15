// my-erp-frontend/src/main/java/com/example/myerp/repository/SalesRepository.java
package com.example.myerp.repository;

import com.example.myerp.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
}
