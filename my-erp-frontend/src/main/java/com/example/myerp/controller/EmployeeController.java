// my-erp-frontend/src/main/java/com/example/myerp/controller/EmployeeController.java
package com.example.myerp.controller;

import com.example.myerp.common.Result;
import com.example.myerp.entity.Employee;
import com.example.myerp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 新增员工
     * POST /api/employee/add
     * RequestBody: Employee 对象
     */
    @PostMapping("/add")
    public Result<Employee> addEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.save(employee);
        if (saved != null) {
            return Result.success(saved, "添加员工成功");
        } else {
            return Result.error("添加员工失败");
        }
    }

    /**
     * 更新员工
     * PUT /api/employee/update/{id}
     * RequestBody: Employee 对象
     */
    @PutMapping("/update/{id}")
    public Result<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updated = employeeService.update(id, employee);
        if (updated != null) {
            return Result.success(updated, "更新员工成功");
        } else {
            return Result.error("更新员工失败");
        }
    }

    /**
     * 删除员工
     * DELETE /api/employee/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteEmployee(@PathVariable Long id) {
        boolean ok = employeeService.delete(id);
        if (ok) {
            return Result.success(null, "删除员工成功");
        } else {
            return Result.error("删除员工失败");
        }
    }

    /**
     * 切换员工状态（启用/禁用）
     * PUT /api/employee/toggle/{id}
     */
    @PutMapping("/toggle/{id}")
    public Result<Employee> toggleStatus(@PathVariable Long id) {
        Employee emp = employeeService.toggleStatus(id);
        if (emp != null) {
            return Result.success(emp, "切换状态成功");
        } else {
            return Result.error("切换状态失败");
        }
    }

    /**
     * 复制员工
     * POST /api/employee/copy/{id}
     */
    @PostMapping("/copy/{id}")
    public Result<Employee> copyEmployee(@PathVariable Long id) {
        Employee emp = employeeService.copy(id);
        if (emp != null) {
            return Result.success(emp, "复制员工成功");
        } else {
            return Result.error("复制员工失败");
        }
    }

    /**
     * 查询所有员工（不分页）
     * GET /api/employee/list
     */
    @GetMapping("/list")
    public Result<List<Employee>> listEmployees() {
        List<Employee> list = employeeService.findAll();
        return Result.success(list, "查询成功");
    }

    /**
     * 根据 ID 查询单个员工
     * GET /api/employee/get/{id}
     */
    @GetMapping("/get/{id}")
    public Result<Employee> getEmployee(@PathVariable Long id) {
        Employee emp = employeeService.findById(id);
        if (emp != null) {
            return Result.success(emp, "查询成功");
        } else {
            return Result.error("未找到该员工");
        }
    }

    /**
     * 分页/模糊查询员工
     * GET /api/employee/page?page={page}&size={size}&keyword={keyword}
     */
    @GetMapping("/page")
    public Result<Page<Employee>> listByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Employee> result = employeeService.findByPage(keyword, pageable);
        return Result.success(result, "分页查询成功");
    }

    /**
     * 导出员工列表为 CSV，并作为下载文件返回
     * GET /api/employee/export
     */
    @GetMapping("/export")
    public ResponseEntity<Resource> exportEmployees() {
        try {
            List<Employee> list = employeeService.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("编号,姓名,电话,邮箱,状态,创建时间\n");
            for (Employee e : list) {
                sb.append(e.getEmNo()).append(",")
                  .append(e.getEmName()).append(",")
                  .append(e.getEmTel()).append(",")
                  .append(e.getEmEmail()).append(",")
                  .append(e.getEmStatus() == 1 ? "启用" : "禁用").append(",")
                  .append(e.getEmCreateTime()).append("\n");
            }
            byte[] bytes = sb.toString().getBytes("UTF-8");
            ByteArrayResource resource = new ByteArrayResource(bytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.csv");
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 上传员工头像
     * POST /api/employee/upload/{id}
     * MultipartFile “file”
     */
    @PostMapping("/upload/{id}")
    public Result<String> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        try {
            // 将文件保存到项目根目录下的 uploads/ 文件夹
            String folder = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filepath = folder + filename;
            file.transferTo(new File(filepath));
            Employee emp = employeeService.findById(id);
            if (emp == null) {
                return Result.error("员工不存在");
            }
            // 保存相对路径（前端访问时： http://localhost:8081/uploads/filename）
            emp.setEmIcon("/uploads/" + filename);
            employeeService.update(id, emp);
            return Result.success(emp.getEmIcon(), "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }
}
