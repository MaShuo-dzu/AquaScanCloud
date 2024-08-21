package org.qinian.controller;


import org.qinian.domain.Result;
import org.qinian.domain.dto.fishRaft.AddLocationDto;
import org.qinian.domain.pojo.Location;
import org.qinian.service.ILocationService;
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
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private ILocationService locationService;

    // 创建经纬度位置
    @PostMapping
    public Result createLocation(@RequestBody AddLocationDto addLocationDto) {
        Location location = locationService.saveLocation(addLocationDto);
        if (location.getId() != null) {
            return Result.success(200, "经纬度位置创建成功", location);
        } else {
            return Result.fail(500, "创建经纬度位置失败", null);
        }
    }

    // 经纬度位置信息查询根据id
    @GetMapping("/{id}")
    public Result getLocationById(@PathVariable Long id) {
        Location location = locationService.getById(id);
        if (location != null) {
            return Result.success(200, "经纬度位置信息获取成功", location);
        } else {
            return Result.fail(404, "经纬度位置信息不存在", null);
        }
    }

    // 查询所有经纬度位置
    @GetMapping
    public Result getAllLocations() {
        List<Location> locationList = locationService.list();
        return Result.success(200, "所有经纬度位置信息获取成功", locationList);
    }

    // 修改经纬度位置
    @PutMapping("/{id}")
    public Result updateLocation(@PathVariable Long id, @RequestBody Location location) {
        location.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = locationService.updateById(location);
        if (isSuccess) {
            return Result.success(200, "经纬度位置信息更新成功", location);
        } else {
            return Result.fail(404, "经纬度位置信息不存在", null);
        }
    }

    // 删除经纬度位置
    @DeleteMapping("/{id}")
    public Result deleteLocation(@PathVariable Long id) {
        boolean isSuccess = locationService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "经纬度位置信息删除成功", null);
        } else {
            return Result.fail(404, "经纬度位置信息不存在", null);
        }
    }
}
