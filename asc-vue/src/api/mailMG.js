import { noTokenReq } from './axiosFun';

// 生成验证码（不需要登录）
export const getCode = async(params) => { return await noTokenReq("post", "/code/generate-verify-code", params) };

// 验证验证码
export const verifyCode = async(params) => { return await noTokenReq("post", "/code/check-verify-code", params) };

// 发送邮箱验证码
export const seedCode = async(params) => { return await noTokenReq("post", "/email/sendVerificationCode", params) };

// 发送邮件
export const seedText = async(params) => { return await noTokenReq("post", "/email/sendSimpleEmail", params) };

// 发送文件
export const seedFile = async(params) => { return await noTokenReq("post", "/email/sendEmailWithAttachment", params) };
