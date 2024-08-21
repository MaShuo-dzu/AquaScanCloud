package org.qinian.controller;


import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.admin.AdminLoginFormDTO;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.service.IAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    /**
     * 用户登录
     * 用户登出
     * 用户修改密码
     */

    private final IAdminService adminService;

    @GetMapping("/login")
    public Result login(AdminLoginFormDTO adminLoginFormDTO) throws Exception {
        UserLoginVO userLoginVO = adminService.login(adminLoginFormDTO);

        return Result.success(200, "登录成功", userLoginVO);
    }

    @GetMapping("/logout")
    public Result logout() {
        // 如果jwt有redis存储这里要删除，这个项目没有

        return Result.success(200, "登出成功", null);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(UpdatePWDDTO updatePWDDTO) throws Exception {
        Boolean ok = adminService.updatePassword(updatePWDDTO);

        if (!ok) {
            return Result.fail(400, "修改密码失败", null);
        }

        return Result.success(200, "修改密码成功", null);
    }
}
