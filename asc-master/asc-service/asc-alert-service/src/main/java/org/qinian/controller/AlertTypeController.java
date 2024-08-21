package org.qinian.controller;

//

import cn.hutool.core.bean.BeanUtil;
import org.qinian.domain.Result;
import org.qinian.domain.dto.alert.AddAlertTypeDto;
import org.qinian.domain.pojo.AlertType;
import org.qinian.service.IAlertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 告警类型的增删改查： 管理员控制器。
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 * <p>

 */
@RestController
@RequestMapping("/alert-type")
public class AlertTypeController {
    @Autowired
    private IAlertTypeService alertTypeService;

    // 创建告警类型
    @PostMapping
    public Result createAlertType(@RequestBody AddAlertTypeDto addAlertTypeDto) {
        AlertType alertType = BeanUtil.copyProperties(addAlertTypeDto, AlertType.class);

        boolean isSuccess = alertTypeService.save(alertType);
        if (isSuccess) {
            return Result.success(200, "告警类型创建成功", alertType);
        } else {
            return Result.fail(500, "创建告警类型失败", null);
        }
    }

    // 根据 ID 获取告警类型
    @GetMapping("/{id}")
    public Result getAlertTypeById(@PathVariable Long id) {
        AlertType alertType = alertTypeService.getById(id);
        if (alertType != null) {
            return Result.success(200, "告警类型获取成功", alertType);
        } else {
            return Result.fail(404, "告警类型不存在", null);
        }
    }

    // 获取所有告警类型
    @GetMapping
    public Result getAllAlertTypes() {
        List<AlertType> alertTypes = alertTypeService.list();
        return Result.success(200, "所有告警类型获取成功", alertTypes);
    }

    // 更新告警类型
    @PutMapping("/{id}")
    public Result updateAlertType(@PathVariable Long id, @RequestBody AlertType alertType) {
        alertType.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = alertTypeService.updateById(alertType);
        if (isSuccess) {
            return Result.success(200, "告警类型更新成功", alertType);
        } else {
            return Result.fail(404, "告警类型不存在", null);
        }
    }

    // 根据 ID 删除告警类型
    @DeleteMapping("/{id}")
    public Result deleteAlertType(@PathVariable Long id) {
        boolean isSuccess = alertTypeService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "告警类型删除成功", null);
        } else {
            return Result.fail(404, "告警类型不存在", null);
        }
    }
}
