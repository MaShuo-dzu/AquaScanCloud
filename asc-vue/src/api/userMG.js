import { noTokenReq, req } from './axiosFun';

// 用户登录（邮箱或账号，无需登录token）
export const userLoginFisherman = async (params) => { return await noTokenReq("post", "/fishermen/login", params) };
export const userLoginPlatoon = async (params) => { return await noTokenReq("post", "/fishing-platoon/login", params) };

// 用户注册 (账号、密码、邮箱必填，无需登录token)
export const userRegisterFisherman = async (params) => { return await noTokenReq("post", "/fishermen/register", params) };
export const userRegisterPlatoon = async (params) => { return await noTokenReq("post", "/fishing-platoon/register", params) };

// 退出登录
export const userLogoutFisherman = async (params) => { return await req("post", "/fishermen/logout", params) };
export const userLogoutPlatoon = async (params) => { return await req("get", "/fishing-platoon/logout", params) };

// 密码修改 （通过邮箱，无需登录token）
export const userUpdatePasswordFisherman = async (params) => { return await noTokenReq("post", "/fishermen/update-password", params) };
export const userUpdatePasswordPlatoon = async (params) => { return await noTokenReq("post", "/fishing-platoon/update-password", params) };

// 获取当前用户的唯一邀请码 （只有渔排管理者有邀请码）
export const userGetInvitationKey = async (params) => { return await req("post", "/fishing-platoon/login", params) };

// 根据邀请码获取管理员信息
export const userGetInfoByIK = async (params) => { return await noTokenReq("post", "/fishing-platoon/select-by-ik", params) };

// 获取当前用户历史记录 （只有渔民有历史记录）
export const userGetHistory = async (params) => { return await req("post", "/fishermen/login", params) };

// 邮箱换绑
export const changeMailFisherman = async(params) => { return await req("post", "/fishermen/change-mail", params) };
export const changeMailPlatoon = async(params) => { return await req("post", "/fishing-platoon/change-mail", params) };

// 邮箱确认
export const checkMailFisherman = async(params) => { return await noTokenReq("post", "/fishermen/select", params) };
export const checkMailPlatoon = async(params) => { return await noTokenReq("post", "/fishing-platoon/select", params) };
