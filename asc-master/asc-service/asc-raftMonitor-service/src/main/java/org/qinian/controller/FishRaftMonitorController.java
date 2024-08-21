package org.qinian.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.raftMonitor.AddRaftMonitor;
import org.qinian.domain.pojo.FishRaftMonitor;
import org.qinian.service.IFishRaftMonitorService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@RequestMapping("/fish-raft-monitor")
@RequiredArgsConstructor
public class FishRaftMonitorController {
    private final IFishRaftMonitorService fishRaftMonitorService;

    // 创建实时渔排检测数据
    @PostMapping
    public Result createFishRaftMonitor(@RequestBody AddRaftMonitor addRaftMonitor) {
        FishRaftMonitor fishRaftMonitor = BeanUtil.copyProperties(addRaftMonitor, FishRaftMonitor.class);
        fishRaftMonitor.setCreateTime(LocalDateTime.now());

        boolean isSuccess = fishRaftMonitorService.save(fishRaftMonitor);
        if (isSuccess) {
            // 算法告警
            fishRaftMonitorService.alert(addRaftMonitor);

            return Result.success(200, "实时渔排检测数据创建成功", fishRaftMonitor);
        } else {
            return Result.fail(500, "创建实时渔排检测数据失败", null);
        }
    }

    // 实时渔排检测数据查询根据id
    @GetMapping("/{id}")
    public Result getFishRaftMonitorById(@PathVariable Long id) {
        FishRaftMonitor raftMonitor = fishRaftMonitorService.getById(id);
        if (raftMonitor != null) {
            return Result.success(200, "实时渔排检测数据获取成功", raftMonitor);
        } else {
            return Result.fail(404, "实时渔排检测数据不存在", null);
        }
    }

    // 查询所有实时渔排检测数据
    @GetMapping
    public Result getFishRaftMonitors() {
        List<FishRaftMonitor> fishRaftMonitorList = fishRaftMonitorService.list();
        return Result.success(200, "所有实时渔排检测数据获取成功", fishRaftMonitorList);
    }

    // 修改实时渔排检测数据
    @PutMapping("/{id}")
    public Result updateFishRaftMonitor(@PathVariable Long id, @RequestBody FishRaftMonitor fishRaftMonitor) {
        fishRaftMonitor.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = fishRaftMonitorService.updateById(fishRaftMonitor);
        if (isSuccess) {
            return Result.success(200, "实时渔排检测数据更新成功", fishRaftMonitor);
        } else {
            return Result.fail(404, "实时渔排检测数据不存在", null);
        }
    }

    // 删除实时渔排检测数据
    @DeleteMapping("/{id}")
    public Result deleteFishRaftMonitor(@PathVariable Long id) {
        boolean isSuccess = fishRaftMonitorService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "实时渔排检测数据删除成功", null);
        } else {
            return Result.fail(404, "实时渔排检测数据不存在", null);
        }
    }
}
