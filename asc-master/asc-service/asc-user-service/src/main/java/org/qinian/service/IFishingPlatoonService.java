package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonLoginFormDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonRegisterFormDTO;
import org.qinian.domain.pojo.FishingPlatoon;
import org.qinian.domain.vo.UserLoginVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IFishingPlatoonService extends IService<FishingPlatoon> {
    UserLoginVO login(FishingPlatoonLoginFormDTO fishingPlatoonLoginFormDTO) throws Exception;

    /**
     * 渔民用户的注册由渔民管理者（FishingPlatoon）负责
     *
     * @param fishingPlatoonRegisterFormDTO
     * @return
     */
    Boolean register(FishingPlatoonRegisterFormDTO fishingPlatoonRegisterFormDTO);

    Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception;

    FishingPlatoon selectByEmail(String email);

    FishingPlatoon selectByInvitationKey(String invitationKey);
}
