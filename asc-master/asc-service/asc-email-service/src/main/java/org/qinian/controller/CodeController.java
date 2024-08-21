package org.qinian.controller;

import org.qinian.domain.Result;
import org.qinian.domain.dto.email.code.CheckCodeDto;
import org.qinian.domain.dto.email.code.GetCodeDto;
import org.qinian.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/generate-verify-code")
    public Result generateVerifyCode(@RequestBody GetCodeDto getCodeDto) {
        String code = codeService.generateVerifyCode(getCodeDto.getMail(), getCodeDto.getLength());

        return Result.success(200, "ok", code);
    }

    /**
     * 0验证码错误、1验证码正确、2验证码过期 （前端处理）
     * @return
     */
    @PostMapping("/check-verify-code")
    public Result checkVerifyCode(@RequestBody CheckCodeDto checkCodeDto) {
        Integer checked = codeService.checkVerifyCode(checkCodeDto.getEmail(), checkCodeDto.getCode());

        return Result.success(200, "ok", checked);
    }
}
