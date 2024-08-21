package org.qinian.controller;


import cn.hutool.core.bean.BeanUtil;
import org.qinian.domain.Result;
import org.qinian.domain.dto.identityCard.AddIdentityCardDto;
import org.qinian.domain.pojo.IdentityCard;
import org.qinian.service.IIdentityCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qinian
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/identity-card")
public class IdentityCardController {
    @Autowired
    private IIdentityCardService identityCardService;

    // 创建身份证信息
    @PostMapping
    public Result createIdentityCard(@RequestBody AddIdentityCardDto addIdentityCardDto) {
        IdentityCard identityCard = BeanUtil.copyProperties(addIdentityCardDto, IdentityCard.class);

        boolean isSuccess = identityCardService.save(identityCard);
        if (isSuccess) {
            return Result.success(200, "身份证信息创建成功", identityCard);
        } else {
            return Result.fail(500, "创建身份证信息失败", null);
        }
    }

    // 身份证信息查询根据id
    @GetMapping("/{id}")
    public Result getIdentityCardById(@PathVariable Long id) {
        IdentityCard identityCard = identityCardService.getById(id);
        if (identityCard != null) {
            return Result.success(200, "身份证信息获取成功", identityCard);
        } else {
            return Result.fail(404, "身份证信息不存在", null);
        }
    }

    // 查询所有身份证信息
    @GetMapping
    public Result getAllIdentityCards() {
        List<IdentityCard> identityCardList = identityCardService.list();
        return Result.success(200, "所有身份证信息获取成功", identityCardList);
    }

    // 修改身份证信息
    @PutMapping("/{id}")
    public Result updateIdentityCard(@PathVariable Long id, @RequestBody IdentityCard identityCard) {
        identityCard.setId(id); // 设置ID以更新对应的记录
        boolean isSuccess = identityCardService.updateById(identityCard);
        if (isSuccess) {
            return Result.success(200, "身份证信息更新成功", identityCard);
        } else {
            return Result.fail(404, "身份证信息不存在", null);
        }
    }

    // 删除身份证信息
    @DeleteMapping("/{id}")
    public Result deleteIdentityCard(@PathVariable Long id) {
        boolean isSuccess = identityCardService.removeById(id);
        if (isSuccess) {
            return Result.success(200, "身份证信息删除成功", null);
        } else {
            return Result.fail(404, "身份证信息不存在", null);
        }
    }
}
