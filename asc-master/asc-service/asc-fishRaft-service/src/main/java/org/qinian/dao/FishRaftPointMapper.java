package org.qinian.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.qinian.domain.pojo.FishRaftPoint;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface FishRaftPointMapper extends BaseMapper<FishRaftPoint> {

    @Select("select * from fish_raft_point where Fishermen_id = #{id}")
    List<FishRaftPoint> getAllByFishmanId(Long id);
}
