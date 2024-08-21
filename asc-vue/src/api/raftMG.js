import { noTokenReq, req } from './axiosFun';

// 渔排点数据获取
export const getRaft = async (params) => { return await req("get", "/fish-raft-point", params) };
