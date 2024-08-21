package org.qinian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinian.dao.FishRaftPointMapper;
import org.qinian.domain.pojo.FishRaftPoint;
import org.qinian.service.IFishRaftPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@Service
public class FishRaftPointServiceImpl extends ServiceImpl<FishRaftPointMapper, FishRaftPoint> implements IFishRaftPointService {

    @Override
    public List<FishRaftPoint> getAllByFishmanId(Long id) {
        return baseMapper.getAllByFishmanId(id);
    }
}
