package org.qinian.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.qinian.domain.pojo.FishingPlatoon;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface FishingPlatoonMapper extends BaseMapper<FishingPlatoon> {
    @Select("select * from fishing_platoon where invitation_code = #{invitationCode}")
    FishingPlatoon selectByInvitationCode(String invitationCode);
}
