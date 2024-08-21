package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.admin.AdminLoginFormDTO;
import org.qinian.domain.pojo.Admin;
import org.qinian.domain.vo.UserLoginVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IAdminService extends IService<Admin> {
    UserLoginVO login(AdminLoginFormDTO adminLoginFormDTO) throws Exception;

    Boolean updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception;
}
