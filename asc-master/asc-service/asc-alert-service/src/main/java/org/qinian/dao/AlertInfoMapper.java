package org.qinian.dao;
/**
 * 11
 **/
/**/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.qinian.domain.pojo.AlertInfo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface AlertInfoMapper extends BaseMapper<AlertInfo> {

    @Select("select * from alert_info where Fish_raft_id = #{id}")
    List<AlertInfo> getAllByRaftId(Long id);
}

