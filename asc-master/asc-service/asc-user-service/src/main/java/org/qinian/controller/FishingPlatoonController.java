package org.qinian.controller;


import lombok.RequiredArgsConstructor;
import org.qinian.domain.Result;
import org.qinian.domain.dto.EmailDto;
import org.qinian.domain.dto.UpdatePWDDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonLoginFormDTO;
import org.qinian.domain.dto.fishingPlatoon.FishingPlatoonRegisterFormDTO;
import org.qinian.domain.dto.fishingPlatoon.SelectByInvitationKeyDto;
import org.qinian.domain.pojo.FishingPlatoon;
import org.qinian.domain.vo.UserLoginVO;
import org.qinian.service.IFishingPlatoonService;
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
@RequestMapping("/fishing-platoon")
@RequiredArgsConstructor
public class FishingPlatoonController {
    /**
     * 用户登录
     * 用户注册
     * 用户登出
     * 用户修改密码
     */

    private final IFishingPlatoonService fishingPlatoonService;

    @PostMapping("/login")
    public Result login(@RequestBody FishingPlatoonLoginFormDTO fishingPlatoonLoginFormDTO) throws Exception {
        UserLoginVO userLoginVO = fishingPlatoonService.login(fishingPlatoonLoginFormDTO);

        if (userLoginVO == null) {
            return Result.fail(400, "登录失败", null);
        }

        return Result.success(200, "登录成功", userLoginVO);
    }

    /**
     * 注册完成用户不会自动登录
     *
     * @param fishingPlatoonRegisterFormDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody FishingPlatoonRegisterFormDTO fishingPlatoonRegisterFormDTO) {
        Boolean register = fishingPlatoonService.register(fishingPlatoonRegisterFormDTO);

        if (!register) {
            return Result.fail(400, "注册失败", null);
        }

        return Result.success(200, "注册成功", null);
    }

    /**
     * 根据邮箱查询用户的信息
     *
     * @return
     */
    @PostMapping("/select")
    public Result selectByEmail(@RequestBody EmailDto email) {
        FishingPlatoon fishingPlatoon = fishingPlatoonService.selectByEmail(email.getEmail());

        return Result.success(200, "ok", fishingPlatoon);
    }

    /**
     * 查询唯一邀请码对应的渔排管理者用户名
     *
     * @return
     */
    @PostMapping("/select-by-ik")
    public Result selectByInvitationKey(@RequestBody SelectByInvitationKeyDto selectByInvitationKeyDto) {
        FishingPlatoon fishingPlatoon = fishingPlatoonService.selectByInvitationKey(selectByInvitationKeyDto.getInvitationKey());

        if (fishingPlatoon == null) {
            return Result.fail(200, "ok", "邀请码无效！");
        }

        return Result.success(200, "ok", "管理者：" + fishingPlatoon.getUsername());
    }

    @GetMapping("/logout")
    public Result logout() {
        // 如果jwt有redis存储这里要删除，这个项目没有

        return Result.success(200, "登出成功", null);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePWDDTO updatePWDDTO) throws Exception {
        Boolean ok = fishingPlatoonService.updatePassword(updatePWDDTO);

        if (!ok) {
            return Result.fail(400, "修改密码失败", null);
        }

        return Result.success(200, "修改密码成功", null);
    }
}
