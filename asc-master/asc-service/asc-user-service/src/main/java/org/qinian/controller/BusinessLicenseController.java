package org.qinian.controller;


import cn.hutool.core.bean.BeanUtil;
import org.qinian.domain.Result;
import org.qinian.domain.dto.businessLicense.AddBusinessLicenseDto;
import org.qinian.domain.pojo.BusinessLicense;
import org.qinian.service.IBusinessLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/business-license")
public class BusinessLicenseController {
    @Autowired
    private IBusinessLicenseService businessLicenseService;

    // 创建营业执照
    @PostMapping
    public Result createBL(@RequestBody AddBusinessLicenseDto addBusinessLicenseDto) {
        BusinessLicense businessLicense = BeanUtil.copyProperties(addBusinessLicenseDto, BusinessLicense.class);

        boolean isSuccess = businessLicenseService.save(businessLicense);
        if (isSuccess) {
            return Result.success(200, "营业执照创建成功", businessLicense);
        } else {
            return Result.fail(500, "创建营业执照失败", null);
        }
    }

    // 营业执照查询根据id
    @GetMapping("/{id}")
    public Result getBLById(@PathVariable Long id) {
        BusinessLicense businessLicense = businessLicenseService.getById(id);
        if (businessLicense != null) {
            return Result.success(200, "营业执照获取成功", businessLicense);
        } else {
            return Result.fail(404, "营业执照不存在", null);
        }
    }

    // 查询所有营业执照
    @GetMapping
    public Result getAllBLs() {
        List<BusinessLicense> businessLicenseList = businessLicenseService.list();
        return Result.success(200, "所有营业执照信息获取成功", businessLicenseList);
    }

    // 修改营业执照
    @PutMapping("/{id}")
    public Result updateBL(@PathVariable Long id, @RequestBody BusinessLicense businessLicense) {
        businessLicense.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = businessLicenseService.updateById(businessLicense);
        if (isSuccess) {
            return Result.success(200, "营业执照信息更新成功", businessLicense);
        } else {
            return Result.fail(404, "营业执照信息不存在", null);
        }
    }

    // 删除营业执照
    @DeleteMapping("/{id}")
    public Result deleteBL(@PathVariable Long id) {
        boolean isSuccess = businessLicenseService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "营业执照信息删除成功", null);
        } else {
            return Result.fail(404, "营业执照信息不存在", null);
        }
    }
}
