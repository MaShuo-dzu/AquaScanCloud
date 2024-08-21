<template>
    <div>
        <div class="container">
            <h1>Fisherman Register</h1>
            <div class="form">
                <input id="form-text" type="text" placeholder="设置账号" v-model="username">
                <input id="form-password" type="password" placeholder="设置密码" v-model="password">
                <input id="form-check-password" type="password" placeholder="确认密码" v-model="checkPassword">
                <input id="form-mail" type="mail" placeholder="邮箱" v-model="mail">
                <input id="form-phone" type="text" placeholder="手机号" v-model="phone">
                <input id="form-invitation" type="text" placeholder="邀请码" v-model="invitationKey" @blur="getPlatUsername">
                <span v-if="message">{{ message }}</span>
                <br>
                <br>
                <button class="btn-register" @click="RegisterF">注册</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import checkEmailType from '../utils/mailUtil';
import {userGetInfoByIK, userRegisterFisherman} from "../api/userMG";

export default {
    name: "RegisterFishermanPage",
    data() {
        return {
            username: '',
            password: '',
            checkPassword: '',
            mail: '',
            phone: '',
            invitationKey: '',
            message: ''
        };
    },
    methods: {
        getPlatUsername() {
            userGetInfoByIK({
                invitationKey: this.invitationKey
            }).then( (response) => {
                console.log(response.data)
                if (response.data.code === 200){
                    this.message = response.data.data
                } else {
                    this.$message.error(response.data.msg)
                }
            })
        },
        RegisterF() {
            if (this.username != "" && this.password != "") {
                if (this.checkPassword != "") {
                    if (this.password === this.checkPassword) {
                        if (this.mail != "") {
                            if (checkEmailType(this.mail)) {
                                // 注册 （检测邀请码是否正确 + 注册）
                                userRegisterFisherman({
                                    username: this.username,
                                    password: this.password,
                                    email: this.mail,
                                    phone: this.phone,
                                    invitationCode: this.invitationKey
                                }).then( (response) => {
                                    console.log(response.data)
                                    if (response.data.code === 200){
                                        this.$message.success("注册成功！")
                                        this.$router.push('/login');
                                    } else {
                                        this.$message.error(response.data.msg)
                                    }
                                })
                            } else {
                                ElMessage.warning('邮箱格式不正确！')
                            }
                        } else {
                            ElMessage.warning('邮箱不能为空！')
                        }
                    } else {
                        ElMessage.warning('两次输入的密码不一致！')
                    }
                } else {
                    ElMessage.warning('请再次确认密码！')
                }
            } else {
                ElMessage.warning('用户名或密码不能为空！')
            }
        },
    }
};
</script>

<style scoped>
.container {
    text-align: center;
    color: #fff;
}

.container h1 {
    font-size: 60px;
    font-weight: bold;
    letter-spacing: 2px;
    margin-bottom:10px;
    transition: 1s ease-in-out;
    color: rgba(255, 255, 255, 0.3);
    -webkit-background-clip: text;
    background-image: url(../assets/water.png);
    animation: wave 30s linear infinite;
}

@keyframes wave{
    from{
        background-position: 0 0;
    }
    to{
        background-position: 1000px 0;
    }
}

.form {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    z-index: 2;
    opacity: 1;
    transition: opacity 0.5s;
}

.form input {
    outline: none;
    border: 1px solid rgba(255,255,255,0.4);
    background-color: rgba(255,255,255,0.2);
    width: 250px;
    padding: 10px 15px;
    border-radius: 3px;
    margin: 0 auto 10px auto;
    text-align: center;
    color:#fff;
    font-size: 15px;
    transition: 0.25s;
}

.form input::placeholder {
    color: #fff;
    font-size: 14px;
    font-weight: 300;
}

.form #form-text, #form-password, #form-check-password, #form-mail, #form-invitation {
    animation: breathe 2s ease-in-out infinite;
}

/* 定义呼吸动画的关键帧 */
@keyframes breathe {
  0% {
    border-color: rgba(255,255,255,0.8); /* 初始边框颜色 */
    box-shadow: 0 0 0 0 rgba(255,255,255,0.8); /* 初始无阴影 */
  }
  50% {
    border-color: rgba(255,255,255,0.2); /* 动画中间阶段的边框颜色 */
    box-shadow: 0 0 8px 0 rgba(255,255,255,0.8); /* 动画中间阶段的阴影 */
  }
  100% {
    border-color: rgba(255,255,255,0.8); /* 动画结束时恢复初始边框颜色 */
    box-shadow: 0 0 0 0 rgba(255,255,255,0.8); /* 动画结束时恢复初始阴影 */
  }
}

.btn-register {
    outline: none;
    background-color:#fff;
    color: #222;
    border: none;
    width: 250px;
    padding: 10px 15px;
    border-radius: 3px;
    font-size: 15px;
    cursor: pointer;
    transition: 0.25s;
}

.form input:hover {
    background-color: rgba(255, 255, 255, 0.4);
}

.form input:focus {
    background-color: #fff;
    width: 300px;
    color: #222;
}

.btn-register:hover {
    background-color:#f5f7f9;
}

</style>