<template>
  <div>
    <div class="container">
      <h1>Welcome</h1>
      <div class="form">
        <input type="text" placeholder="您的账号" v-model="username">
        <input type="password" placeholder="您的密码" v-model="password">
        <button class="btn-login" @click="login">登录</button>
        <button class="btn-login" @click="forgetPD">忘记密码</button>

        <div style="margin-top: 20px">
          <el-radio-group v-model="roleRadio">
            <el-radio value="fisherman" border>我是渔民</el-radio>
            <el-radio value="platoon" border>我是管理者</el-radio>
          </el-radio-group>
        </div>
      </div>
    </div>

    <!-- 注册按钮 -->
    <div class="register">
      <a class="bottom-btn" @click.prevent="goToRegister">注册</a>
    </div>
  </div>
  
</template>

<script>
import {userLoginFisherman, userLoginPlatoon} from '@/api/userMG.js';
import { ElMessage } from 'element-plus'

export default {
  name: 'LoginPage',
  data() {
    return {
      username: '',
      password: '',
      error: '',
      roleRadio: 'fisherman'
    };
  },
  methods: {
    goToRegister() {
      if (this.roleRadio === "fisherman") {
        this.$router.push('/register-fisherman');
      } else {
        this.$router.push('/register-platoon');
      }
    },
    login() {
      if (this.username === "" || this.password === ""){
        ElMessage.warning('用户名或密码不能为空！')
      } else {
        if (this.roleRadio === "fisherman") {
          userLoginFisherman({
            username: this.username,
            password: this.password
          }).then( (response) => {
            if (response.data.code === 200){
              ElMessage.success('渔民登录成功！')

              let userToken = {
                "token": response.data.data.token,
                "role": "fisherman"
              };
              
              localStorage.setItem('token', JSON.stringify(userToken));
              
              localStorage.setItem('userId', response.data.data.userId);
              localStorage.setItem('username', response.data.data.username);

              this.$router.push('/home-fisherman');
            }else{
              ElMessage.error(response.data.msg)
            }
          })
        } else {
          userLoginPlatoon({
            username: this.username,
            password: this.password
          }).then( (response) => {
            if (response.data.code === 200){
              ElMessage.success('管理者登录成功！')

              localStorage.setItem('token', {
                "token": response.data.data.token,
                "role": "platoon"
              });
              localStorage.setItem('userId', response.data.data.userId);
              localStorage.setItem('username', response.data.data.username);

              this.$router.push('/home-platoon');
            } else {
              ElMessage.error(response.data.msg)
            }
          })
        }
      }
    },
    forgetPD() {
      this.$router.push(`/update/${this.roleRadio}/before`);
    }
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

.btn-login {
  margin-bottom: 5px;
  margin-top: 5px;
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

.btn-login:hover {
  background-color:#f5f7f9;
}

.register {
  position: fixed; /* 固定定位，不随页面滚动 */
  bottom: 20px; /* 距离底部20px */
  left: 50%; /* 居中对齐 */
  transform: translateX(-50%); /* 使用CSS3的transform属性向左移动自身宽度的50%来居中 */
  margin: 0; /* 清除默认边距 */
  z-index: 2;
}

a
{
  position: relative;
  padding: 20px 40px;
  color: #fff;
  text-decoration: none;
  display: inline-block;
  text-transform: uppercase;
  font-size: 1.5em;
  letter-spacing: 0.2em;
  border: 2px solid transparent;
  transition: 0.5s;
}

a:hover
{
  border-color: #fff;
}

a::before
{
  content: '';
  position: absolute;
  inset: 0 8px;
  border-left: 2px solid #fff;
  border-right:  2px solid #fff;
  transition: 1s;
}

a::after
{
  content: '';
  position: absolute;
  inset: 8px 0;
  border-top: 2px solid #fff;
  border-bottom:  2px solid #fff;
  transition: 1s;
}

a.bottom-btn:hover {
  border-color: transparent;
}

a.bottom-btn:hover::before {
  inset: 0 0;
  transform: rotate(-10deg);
}

a.bottom-btn:hover::after {
  inset: 0 -4px;
  transform: rotate(5deg);
}
</style>
