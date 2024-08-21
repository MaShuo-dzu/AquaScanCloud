package org.qinian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.qinian.dao.FishRaftMonitorMapper;
import org.qinian.domain.dto.alert.AddAlertInfoDto;
import org.qinian.domain.dto.raftMonitor.AddRaftMonitor;
import org.qinian.domain.pojo.FishRaftMonitor;
import org.qinian.enums.AlertLevels;
import org.qinian.enums.AlertTypes;
import org.qinian.model.RemoteAlertService;
import org.qinian.service.IFishRaftMonitorService;
import org.springframework.data.redis.core.RedisTemplate;
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
@RequiredArgsConstructor
public class FishRaftMonitorServiceImpl extends ServiceImpl<FishRaftMonitorMapper, FishRaftMonitor> implements IFishRaftMonitorService {
    private final RedisTemplate<String, Object> redisTemplate;

    private final RemoteAlertService remoteAlertService;

    @Override
    public void alert(AddRaftMonitor addRaftMonitor) {
        String key = "alert_ai_" + addRaftMonitor.getFishRaftId();

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 1. 获取该渔排点的历史记录
            AddRaftMonitor lastInfo = (AddRaftMonitor) redisTemplate.opsForValue().get(key);

            // 2.渔排数据变更警告
            assert lastInfo != null;
            if (!lastInfo.getRaftArea().equals(addRaftMonitor.getRaftArea()) || !lastInfo.getRaftNumber().equals(addRaftMonitor.getRaftNumber())) {
                remoteAlertService.createAlertInfo(
                        new AddAlertInfoDto(
                                addRaftMonitor.getFishRaftId(),
                                AlertTypes.AI_ALERT,
                                AlertLevels.HEAT,
                                "渔排数据变更警告"
                        )
                );
            }
        } else {
            // 无该渔排点的历史记录
            redisTemplate.opsForValue().set(key, addRaftMonitor);
        }
    }
}
