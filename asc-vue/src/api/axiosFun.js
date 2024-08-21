import axios from 'axios';

const baseUrl = "http://localhost:8080";

const noTokenReq = (method, url, params) => {
    return new Promise((resolve, reject) => {
        axios({
            method: method,
            url: baseUrl + url,
            headers: {
                'Content-Type': 'application/json',
            },
            data: params,
            traditional: true
        }).then(resp => {
            resolve(resp);
        }, err => {
            reject(err)
        })
    })
};
// 通用公用方法
const req = (method, url, params) => {

    return new Promise((resolve, reject) => {
        axios({
            method: method,
            url: baseUrl + url,
            headers: {
                'Content-Type': 'application/json',
                'token': JSON.parse(localStorage.getItem('token')?localStorage.getItem('token'):"")?.token
            },
            data: params,
            traditional: true
        }).then(resp => {
            resolve(resp);
        }, err => {
            reject(err)
        })
    })
};

export {
    noTokenReq,
    req
}