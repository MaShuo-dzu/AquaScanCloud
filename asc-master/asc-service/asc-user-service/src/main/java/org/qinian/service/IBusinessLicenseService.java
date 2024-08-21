package org.qinian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinian.domain.pojo.BusinessLicense;
import org.qinian.entity.QCCInfo;

/**
 * <p>
 * 服务类： 业务许可证校验，用于渔排管理者注册账号后绑定唯一的营业许可信息
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
public interface IBusinessLicenseService extends IService<BusinessLicense> {
    /**
     * 检测营业许可证是否有效 （验证的过程采用消息队列，结果采用邮箱通知的形式返回）
     *
     * @param imageBase 图片的base64编码（经过ase解密）
     * @return 营业许可字符串
     */
    QCCInfo checkBL(String imageBase);
}
