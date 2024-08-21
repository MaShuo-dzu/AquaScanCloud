package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.pojo.AlertInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IAlertInfoService extends IService<AlertInfo> {

    List<AlertInfo> getAllByRaftId(Long id);
}
