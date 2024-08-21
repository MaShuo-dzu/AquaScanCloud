package org.qinian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinian.dao.AlertInfoMapper;
import org.qinian.domain.pojo.AlertInfo;
import org.qinian.service.IAlertInfoService;
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
public class AlertInfoServiceImpl extends ServiceImpl<AlertInfoMapper, AlertInfo> implements IAlertInfoService {

    @Override
    public List<AlertInfo> getAllByRaftId(Long id) {
        return baseMapper.getAllByRaftId(id);
    }
}