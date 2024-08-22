package org.qinian.controller;

import cn.hutool.core.bean.BeanUtil;
import org.qinian.domain.Result;
import org.qinian.domain.dto.fishRaft.AddFishRaftDto;
import org.qinian.domain.dto.fishRaft.AddLocationDto;
import org.qinian.domain.dto.fishRaft.PyRaftDto;
import org.qinian.domain.dto.weather.WeatherLoopAddDto;
import org.qinian.domain.pojo.FishRaftPoint;
import org.qinian.domain.vo.GetRaftVo;
import org.qinian.model.RemoteWeatherService;
import org.qinian.service.IFishRaftPointService;
import org.qinian.service.ILocationService;
import org.qinian.utils.HttpRequestSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 渔排点的增删改查
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/fish-raft-point")
public class FishRaftPointController {
    @Autowired
    private IFishRaftPointService fishRaftPointService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private RemoteWeatherService remoteWeatherService;

    // 创建渔排点
    @PostMapping
    public Result createFishRaft(@RequestBody AddFishRaftDto addFishRaftDto) throws IOException {
        FishRaftPoint fishRaftPoint = BeanUtil.copyProperties(addFishRaftDto, FishRaftPoint.class);
        fishRaftPoint.setCreateTime(LocalDateTime.now());
        fishRaftPoint.setUpdateTime(LocalDateTime.now());
        // 添加对应的 location id
        fishRaftPoint.setLocationId(locationService.saveLocation(addFishRaftDto.getLocationDto()).getId());

        boolean isSuccess = fishRaftPointService.save(fishRaftPoint);
        if (isSuccess) {
            // 加入监测微服务循环
            PyRaftDto pyRaftDto = BeanUtil.copyProperties(fishRaftPoint, PyRaftDto.class);
            pyRaftDto.setFishermen_id(fishRaftPoint.getFishermenId());   // 变量名字不一样
            pyRaftDto.setLatitude(addFishRaftDto.getLocationDto().getLatitude());
            pyRaftDto.setLongitude(addFishRaftDto.getLocationDto().getLongitude());

            // 加入天气监测服务循环
            AddLocationDto addLocationDto = addFishRaftDto.getLocationDto();
            WeatherLoopAddDto weatherLoopAddDto = new WeatherLoopAddDto(
                    fishRaftPoint.getId(),
                    fishRaftPoint.getLocationId(),
                    addLocationDto.getLatitude(),
                    addLocationDto.getLongitude()
            );

            //  请求发送
            HttpRequestSender.sendPostRequest("http://127.0.0.1:9008/api/python/raft/add", pyRaftDto);
            remoteWeatherService.addRaft(weatherLoopAddDto);

            return Result.success(200, "渔排点创建成功", fishRaftPoint);
        } else {
            return Result.fail(500, "创建渔排点失败", null);
        }
    }

    // 渔排点信息查询根据id
    @GetMapping("/{id}")
    public Result getFishRaftById(@PathVariable Long id) {
        FishRaftPoint raftPoint = fishRaftPointService.getById(id);
        if (raftPoint != null) {
            return Result.success(200, "渔排点信息获取成功", raftPoint);
        } else {
            return Result.fail(404, "渔排点信息不存在", null);
        }
    }

    // 查询所有渔排点
    @GetMapping
    public Result getAllFishRafts() {
        System.out.println("获取所有渔排点信息 --> " + LocalDateTime.now());
        List<FishRaftPoint> fishRaftPointList = fishRaftPointService.list();

        // create vo
        List<GetRaftVo> getRaftVoList = new ArrayList<>();
        for (int i = 0; i < fishRaftPointList.size(); i++) {
            GetRaftVo getRaftVo = BeanUtil.copyProperties(fishRaftPointList.get(i), GetRaftVo.class);
            getRaftVo.setId("row-" + i);
            getRaftVo.setRaftId(fishRaftPointList.get(i).getId());
            getRaftVo.setParentId(null);
            getRaftVo.setLatitude(locationService.getById(fishRaftPointList.get(i).getLocationId()).getLatitude());
            getRaftVo.setLongitude(locationService.getById(fishRaftPointList.get(i).getLocationId()).getLongitude());

            getRaftVoList.add(getRaftVo);
        }

        return Result.success(200, "所有渔排点信息获取成功", getRaftVoList);
    }

    // 根据渔民id查询所有渔排点
    @GetMapping("/select/{id}")
    public Result getAllFishRaftsById(@PathVariable Long id) {
        List<FishRaftPoint> fishRaftPointList = fishRaftPointService.getAllByFishmanId(id);

        return Result.success(200, "所有渔排点信息获取成功", fishRaftPointList);
    }

    // 修改渔排点
    @PutMapping("/{id}")
    public Result updateFishRaft(@PathVariable Long id, @RequestBody FishRaftPoint fishRaftPoint) {
        fishRaftPoint.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = fishRaftPointService.updateById(fishRaftPoint);
        if (isSuccess) {
            return Result.success(200, "告警信息更新成功", fishRaftPoint);
        } else {
            return Result.fail(404, "告警信息不存在", null);
        }
    }

    // 删除渔排点
    @DeleteMapping("/{id}")
    public Result deleteFishRaft(@PathVariable Long id) throws IOException {
        boolean isSuccess = fishRaftPointService.removeById(id);
        if (isSuccess) {

            //  分割算法监测
            HttpRequestSender.sendPostRequest("http://127.0.0.1:9008/api/python/raft/delete", id);

            return Result.success(200, "渔排点信息删除成功", null);
        } else {
            return Result.fail(404, "渔排点信息不存在", null);
        }
    }
}
