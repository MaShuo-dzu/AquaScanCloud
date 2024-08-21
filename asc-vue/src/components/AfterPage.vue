<template>
  <div>
      <div class="container">
          <h1>修改密码</h1>
          <div class="form">
          <input type="password" placeholder="新的密码" v-model="password">
          <input type="password" placeholder="确认密码" v-model="checkPWD">
          </div>
      </div>

      <!-- 确认按钮 -->
      <div class="confirm">
          <a class="bottom-btn" @click.prevent="goToLogin">确认</a>
      </div>
  </div>
</template>

<script>
import { userUpdatePassword, userLogout } from '@/api/userMG.js';
import { ElMessage } from 'element-plus'

export default {
  name: 'AfterPage',
  data() {
    return {
      password: "",
      checkPWD: ""
    }
  },
  methods: {
    goToLogin() {
      if (this.password === "") {
        ElMessage.error("请设置密码！")

        return
      } 

      if (this.password != this.checkPWD) {
        ElMessage.error("两次密码不一致！")

        return
      }

      // 更新密码到后端
      userUpdatePassword({
        password: this.password,
        mail: localStorage.getItem('mail')
      }).then( (response) => {
        if (response.data.code === 200){
            ElMessage.success("修改成功！")
        userLogout({
        }).then( (response) => {
          if (response.data.code === 200){
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            localStorage.removeItem('mail');
            localStorage.removeItem('invitationNum');
            localStorage.removeItem('role');
            ElMessage.success("密码修改完成，请重新登录！")
            this.$router.push('/login');
          } else {
            ElMessage.error(response.data.msg)
          }
        })
        } else {
          ElMessage.error(response.data.msg)
        }
      })
    }
  },
}
</script>

<style scoped>
.container {
  position: fixed; /* 固定定位，不随页面滚动 */
  top: 20%; /* 距离底部20px */
  left: 50%; /* 居中对齐 */
  transform: translateX(-50%); /* 使用CSS3的transform属性向左移动自身宽度的50%来居中 */
  text-align: center;
  color: #fff;
  z-index: 2;
}

.container h1 {
  font-size: 60px;    
  font-weight: bold;
  letter-spacing: 2px;
  margin-bottom: 70px;
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
  align-items: center;
  position: relative;
  display: flex;
  flex-direction: column;
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

.form input:hover {
  background-color: rgba(255, 255, 255, 0.4);
}

.form input:focus {
  background-color: #fff;
  width: 300px;
  color: #222;
}

.confirm {
  position: fixed; /* 固定定位，不随页面滚动 */
  bottom: 25px; /* 距离底部20px */
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
