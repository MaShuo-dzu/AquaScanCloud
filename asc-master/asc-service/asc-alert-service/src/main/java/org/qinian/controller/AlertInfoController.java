package org.qinian.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.alert.AddAlertInfoDto;
import org.qinian.domain.pojo.AlertInfo;
import org.qinian.service.IAlertInfoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 告警信息的增删改查。
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 * <p>
 *
 */
@RestController
@RequestMapping("/alert-info")
@RequiredArgsConstructor
public class AlertInfoController {
    private final IAlertInfoService alertInfoService;

    // 创建告警信息
    @PostMapping
    public Result createAlertInfo(@RequestBody AddAlertInfoDto addAlertInfoDto) {
        AlertInfo alertInfo = BeanUtil.copyProperties(addAlertInfoDto, AlertInfo.class);
        alertInfo.setAlertedTime(LocalDateTime.now());

        boolean isSuccess = alertInfoService.save(alertInfo);
        if (isSuccess) {
            return Result.success(200, "告警信息创建成功", alertInfo);
        } else {
            return Result.fail(500, "创建告警信息失败", null);
        }
    }

    // 根据 ID 获取告警信息
    @GetMapping("/{id}")
    public Result getAlertInfoById(@PathVariable Long id) {
        AlertInfo alertInfo = alertInfoService.getById(id);
        if (alertInfo != null) {
            return Result.success(200, "告警信息获取成功", alertInfo);
        } else {
            return Result.fail(404, "告警信息不存在", null);
        }
    }

    // 获取所有告警信息
    @GetMapping
    public Result getAllAlertInfos() {
        List<AlertInfo> alertInfos = alertInfoService.list();
        return Result.success(200, "所有告警信息获取成功", alertInfos);
    }

    // 根据渔排点id获取所有告警信息
    @GetMapping("/raft/{id}")
    public Result getAllByRaftId(@PathVariable Long id) {
        List<AlertInfo> alertInfos = alertInfoService.getAllByRaftId(id);

        return Result.success(200, "所有告警信息获取成功", alertInfos);
    }

    // 更新告警信息
    @PutMapping("/{id}")
    public Result updateAlertInfo(@PathVariable Long id, @RequestBody AlertInfo alertInfo) {
        alertInfo.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = alertInfoService.updateById(alertInfo);
        if (isSuccess) {
            return Result.success(200, "告警信息更新成功", alertInfo);
        } else {
            return Result.fail(404, "告警信息不存在", null);
        }
    }

    // 根据 ID 删除告警信息
    @DeleteMapping("/{id}")
    public Result deleteAlertInfo(@PathVariable Long id) {
        boolean isSuccess = alertInfoService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "告警信息删除成功", null);
        } else {
            return Result.fail(404, "告警信息不存在", null);
        }
    }
}
