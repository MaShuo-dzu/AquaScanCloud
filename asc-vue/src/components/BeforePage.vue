<template>
    <div>
        <div class="container">
            <h1>邮箱验证</h1>
            <div class="form">
            <input type="text" placeholder="您的邮箱" v-model="mail">
            <button class="btn-seed" :disabled="isDisabled" @click="seedMailF">{{ buttonText }}</button>
            </div>
        </div>

        <div class="ipt-wrap">
            <input type="text" class="ipt-hidden">
            <p  class='ipt-item'></p>
            <p  class='ipt-item'></p>
            <p  class='ipt-item'></p>
            <p  class='ipt-item'></p>
            <p  class='ipt-item'></p>
            <p  class='ipt-item'></p>
        </div>

  
        <!-- 确认按钮 -->
        <div class="confirm">
            <a href="#" class="bottom-btn" @click.prevent="goToAfter">确认</a>
        </div>
    </div>
  </template>
  
<script setup>
import { ref, onBeforeUnmount, onMounted } from 'vue';
import { getCode, seedCode, verifyCode } from '@/api/mailMG.js';
import { checkMailFisherman, checkMailPlatoon } from '@/api/userMG.js';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'
import checkEmailType from '../utils/mailUtil';

const router = useRouter();
const reg = /^[0-9]{0,6}$/;
let temp_val='';

const isDisabled = ref(false);
const countdown = ref(60);
const interval = ref(null);
const buttonText = ref('发送验证码');
const mail = ref('');
const roleRadio = ref('');

const goToAfter = () => {
    var pElements = document.querySelectorAll('.ipt-item');
    var pValues = [];

    pElements.forEach(function(p) {
        pValues.push(p.textContent);
    });

    var s = pValues.join('')

    if (s.length != 6) {
        ElMessage.error("请输入发送邮箱的6位数验证码！")
    } else {
        // 验证邮箱验证码 0验证码错误、1验证码正确、2验证码过期 （前端处理）
        verifyCode({
            mail: mail.value,
            code: s
            }).then( (response) => {
            console.log(response.data)
            if (response.data.code === 200){
                if (response.data.data === 1) {
                    ElMessage.success('验证码成功！')
                    router.push(`/update/${roleRadio.value}/after`);
                } else if (response.data.data === 0) {
                    ElMessage.error('验证失败！')
                } else {
                    ElMessage.warning('验证码已经过期！')
                }

            } else {
                ElMessage.error(response.data.msg)
            }
        })


    }
};

const codeUtil = () => {
    // 获取验证码（这里这么写是因为来不及了）
    getCode({
        mail: mail.value,
        length: 6
    }).then( (response) => {
        console.log(response.data)
        if (response.data.code != 200){
            ElMessage.error("发送失败！服务器可能睡觉了！");

            return
        } else {
            // 发送验证码（检验是否发送、发送验证码）
            seedCode({
                to: mail.value,
                subject: "验证码->遥感渔排监测系统",
                code: response.data.data
                }).then( (response) => {
                if (response.data.code === 200){
                    ElMessage.success("发送成功！")
                    isDisabled.value = true;
                    buttonText.value = `${countdown.value}秒后重试`;

                    interval.value = setInterval(() => {
                        if (countdown.value > 0) {
                        countdown.value--;
                        buttonText.value = `${countdown.value}秒后重试`;
                        } else {
                        resetButton();
                        }
                    }, 1000);
                } else {
                    ElMessage.error(response.data.msg)
                }
            })
        }
    })
}

const seedMailF = () => {
    if (mail.value === "") {
        ElMessage.warning('请填写邮箱！')

        return
    }

    if (! checkEmailType(mail.value)) {
        ElMessage.warning('邮箱格式不正确！')

        return
    }
    
    // 判断邮箱是否绑定用户
    if (roleRadio.value === "fisherman") {
        checkMailFisherman({
            mail: mail.value
            }).then( (response) => {
            if (response.data.data != null){
                codeUtil()
            } else {
                ElMessage.error("当前邮箱未注册！")
            }
        })
    } else {
        checkMailPlatoon({
            mail: mail.value
            }).then( (response) => {
            if (response.data.data != null){
                codeUtil()
            } else {
                ElMessage.error("当前邮箱未注册！")
            }
        })
    }
};

const resetButton = () => {
  clearInterval(interval.value);
  isDisabled.value = false;
  countdown.value = 60;
  buttonText.value = '发送验证码';
};

onBeforeUnmount(() => {
  if (interval.value) {
    clearInterval(interval.value);
  }
});

// 在组件挂载后运行的代码
onMounted(() => {
    roleRadio.value = router.currentRoute._rawValue.params.role;
    // 要操作的元素
    const input=document.querySelector('.ipt-hidden');
    const items=document.querySelectorAll('.ipt-item');

    // 输入框获得焦点事件
    input.addEventListener('focus',()=>{
        // 光标移到最后
        input.setSelectionRange(input.value.length-1,input.value.length);
        const val=input.value;
        if(!val){
            // 未输入时，默认第一格为激活态
            items[0].classList.add('active');
            return;
        }
        // 判断哪一个格子需要设置激活态样式
        if(val.length<items.length){
            items[val.length].classList.add('active');
        }
        // 最后一格
        if(val.length==items.length){
            items[val.length-1].classList.add('active');
        }
    });
    // 输入框失去焦点事件
    input.addEventListener('blur',()=>{
        // 移除激活态样式
        items.forEach(item=>{
            item.classList.remove('active');
        })
    });
    // 输入框输入事件
    input.addEventListener('input',(e)=>{
        // 先清空文本并移除激活态样式
        items.forEach(item=>{
            item.textContent='';
            item.classList.remove('active');
        })
        // 获取输入的值
        const val=e.target.value;
        // 正则验证
        if(reg.test(val)){
            temp_val=val;
        }else{
            input.value=temp_val;
        }
        // 将输入的值拆分为数组
        const arr=input.value.split('');
        if(!arr.length){
            // 没输入，默认第一格为激活态
            items[0].classList.add('active');
        }
        // 循环设置每一格的文本和样式
        arr.forEach((item,index)=>{
            items[index].textContent=item;
            items[index].classList.remove('active');
            if(items[index+1]){
                items[index+1].classList.add('active');
            }
            if(index==items.length-1){
                items[index].classList.add('active');
            }
        })
    });
});
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

.btn-seed {
  margin-top: 5px;
  outline: none;
  background-color: rgba(255, 255, 255, 0.8);
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

.btn-seed:hover {
    background-color:#f5f7f9;
}

.btn-seed:disabled {
  background-color: rgba(255, 255, 255, 0.5);
  cursor: not-allowed;
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

.ipt-wrap {
    position: fixed; /* 固定定位，不随页面滚动 */
    bottom: 28%; /* 距离底部20px */
    left: 50%; /* 居中对齐 */
    transform: translateX(-50%); /* 使用CSS3的transform属性向左移动自身宽度的50%来居中 */
    display: flex;
    justify-content: center;
    z-index: 2;
}

.ipt-hidden {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 2;
    opacity: 0;
}

.ipt-content {
    display: flex;
}

.ipt-item {
    width: 50px;
    height: 65px;
    border-radius: 6px;
    background-color: rgba(22, 22, 22, 0.5);
    border: 1px solid #3f566e;
    margin: 0 10px;
    color: #fff;
    font-size: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 激活态 */
.ipt-item.active{
border-color: #fff;
}
/* 光标 */
.ipt-item.active::after{
content: '';
width: 1px;
height: 30px;
background-color: #fff;
/* 执行动画：动画名 时长 线性的 无限播放 来回轮流播放 */
animation: flicker 0.5s linear infinite alternate;
}

/* 定义动画 */
@keyframes flicker {
0%{
    opacity: 0;
}
100%{
    opacity: 1;
}
}
</style>
