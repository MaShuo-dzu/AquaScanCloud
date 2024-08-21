package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.dto.raftMonitor.AddRaftMonitor;
import org.qinian.domain.pojo.FishRaftMonitor;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IFishRaftMonitorService extends IService<FishRaftMonitor> {

    void alert(AddRaftMonitor addRaftMonitor);
}
