package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.pojo.FishRaftPoint;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IFishRaftPointService extends IService<FishRaftPoint> {

    List<FishRaftPoint> getAllByFishmanId(Long id);
}