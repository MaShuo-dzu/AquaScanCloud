package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.dto.fishRaft.AddLocationDto;
import org.qinian.domain.pojo.Location;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface ILocationService extends IService<Location> {

    Location saveLocation(AddLocationDto addLocationDto);
}
