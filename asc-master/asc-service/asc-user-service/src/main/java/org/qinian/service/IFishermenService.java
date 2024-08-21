package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishermen.FishermenLoginFormDTO;
import org.qinian.domain.dto.fishermen.FishermenRegisterFormDTO;
import org.qinian.domain.pojo.Fishermen;
import org.qinian.domain.vo.UserLoginVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IFishermenService extends IService<Fishermen> {
    UserLoginVO login(FishermenLoginFormDTO loginFormDTO) throws Exception;

    /**
     * 渔民用户的注册由渔民管理者（FishingPlatoon）负责
     * 渔民自己注册需要注册码+绑定身份证
     *
     * @param registerFormDTO
     * @return
     */
    Boolean register(FishermenRegisterFormDTO registerFormDTO) throws Exception;

    Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception;

    Fishermen selectByEmail(String email);
}
