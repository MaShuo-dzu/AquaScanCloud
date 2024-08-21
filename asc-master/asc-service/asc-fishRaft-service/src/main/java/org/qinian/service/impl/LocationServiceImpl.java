package org.qinian.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinian.dao.LocationMapper;
import org.qinian.domain.dto.fishRaft.AddLocationDto;
import org.qinian.domain.pojo.Location;
import org.qinian.service.ILocationService;
import org.qinian.utils.LocationUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

    @Override
    public Location saveLocation(AddLocationDto addLocationDto) {
        Location location = BeanUtil.copyProperties(addLocationDto, Location.class);
        // 获取城市 adcode
        location.setAdcode(LocationUtil.getAdcodeByLocation(location.getLongitude(), location.getLatitude()));

        baseMapper.insert(location);
        return location;
    }
}
