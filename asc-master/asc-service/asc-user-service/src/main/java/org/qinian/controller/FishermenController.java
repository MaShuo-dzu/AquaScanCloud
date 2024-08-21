package org.qinian.controller;


import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.EmailDto;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishermen.FishermenLoginFormDTO;
import org.qinian.domain.dto.fishermen.FishermenRegisterFormDTO;
import org.qinian.domain.pojo.Fishermen;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.service.IFishermenService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/fishermen")
@RequiredArgsConstructor
public class FishermenController {
    /**
     * 用户登录
     * 用户注册
     * 用户登出
     * 用户修改密码
     */

    private final IFishermenService fishermenService;

    @PostMapping("/login")
    public Result login(@RequestBody FishermenLoginFormDTO fishermenLoginFormDTO) throws Exception {
        UserLoginVO userLoginVO = fishermenService.login(fishermenLoginFormDTO);

        if (userLoginVO == null) {
            return Result.fail(400, "登录失败", null);
        }

        return Result.success(200, "登录成功", userLoginVO);
    }

    /**
     * 注册完成用户不会自动登录
     *
     * @param fishermenRegisterFormDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody FishermenRegisterFormDTO fishermenRegisterFormDTO) throws Exception {
        System.out.println("fishermenRegisterFormDTO = " + fishermenRegisterFormDTO);
        Boolean ok = fishermenService.register(fishermenRegisterFormDTO);

        if (ok) {
            return Result.success(200, "注册成功", null);
        } else {
            return Result.fail(400, "注册失败", null);
        }
    }

    /**
     * 根据邮箱查询用户的信息
     *
     * @return
     */
    @PostMapping("/select")
    public Result selectByEmail(@RequestBody EmailDto email) {
        Fishermen fishermen = fishermenService.selectByEmail(email.getEmail());

        return Result.success(200, "ok", fishermen);
    }

    @GetMapping("/logout")
    public Result logout() {
        // 如果jwt有redis存储这里要删除，这个项目没有

        return Result.success(200, "登出成功", null);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePWDDTO updatePWDDTO) throws Exception {
        Boolean ok = fishermenService.updatePassword(updatePWDDTO);

        if (!ok) {
            return Result.fail(400, "修改密码失败", null);
        }

        return Result.success(200, "修改密码成功", null);
    }
}
