<template>
    <div>
         <!-- 弹出框 -->
         <el-dialog
            title="上传营业执照照片"
            v-model="dialogVisible"
            width="50%"
            :modal-append-to-body="true"
            :close-on-click-modal="false"
        >
            <div>
                <div v-if="base64Image">
                    <img :src="base64Image" alt="Preview" style="max-width: 300px; max-height: 300px;">
                </div>
                <input id="upload-img" type="file" @change="handleFileChange" placeholder="请上传图片" accept="image/*" />
            </div>

            <!-- 确认和取消按钮 -->
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelUpload">取消</el-button>
                <el-button type="primary" @click="confirmUpload">确认</el-button>
            </span>
        </el-dialog>

        <div class="container">
            <h1>Platoon Register</h1>
            <div class="form">
                <input id="form-username" type="text" placeholder="设置账号" v-model="username">
                <input id="form-password" type="password" placeholder="设置密码" v-model="password">
                <input id="form-check-password" type="password" placeholder="确认密码" v-model="checkPassword">
                <input id="form-mail" type="mail" placeholder="邮箱" v-model="mail">
                <input id="form-phone" type="text" placeholder="手机号" v-model="phone">
                <input id="form-province" type="text" placeholder="省" v-model="province">
                <input id="form-city" type="text" placeholder="市" v-model="city">
                <input id="form-town" type="text" placeholder="区" v-model="town">
                <br>
                <br>
                <button class="btn-register" @click="openDialog">注册</button>
            </div>
        </div>
    </div>
</template>

<script>
import { ElMessage } from 'element-plus'
import checkEmailType from '../utils/mailUtil';
import { userRegisterPlatoon } from '../api/userMG';

export default {
    name: "RegisterplatoonPage",
    data() {
        return {
            username: '',
            password: '',
            checkPassword: '',
            mail: '',
            phone: '',
            province: '',
            city: '',
            town: '',
            dialogVisible: false,
            base64Image: '',
        };
    },
    methods: {
        openDialog() {
            if (this.username != "" && this.password != "") {
                if (this.checkPassword != "") {
                    if (this.password === this.checkPassword) {
                        if (this.mail != "") {
                            if (checkEmailType(this.mail)) {
                                this.dialogVisible = true;
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
        handleFileChange(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    this.base64Image = e.target.result; // 读取图片
                    // console.log(this.base64Image)
                };
                reader.readAsDataURL(file);
            }
        },
        confirmUpload() {
            if (!this.base64Image) {
                this.$message.error("请上传营业执照照片");
                return;
            }
            userRegisterPlatoon({
                username: this.username,
                password: this.password,
                email: this.mail,
                phone: this.phone,
                province: this.province,
                city: this.city,
                town: this.town,
                blImageBase: this.base64Image.substring(this.base64Image.indexOf("base64,") + 7)
            }).then( (response) => {
                console.log(response.data)
                if (response.data.code === 200){
                    this.$message.success("请耐心等待，注册结果会在10个工作日内通过邮箱反馈！");
                    this.dialogVisible = false; // 关闭对话框
                    this.base64Image = '';
                    this.$router.push('/login');
                } else {
                    this.$message.error(response.data.msg)
                }
            })
        },
        cancelUpload() {
            this.dialogVisible = false;
            this.base64Image = '';
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

/* 必须输入 */
.form #form-username, #form-password, #form-check-password, #form-mail {
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

.dialog-footer {
    text-align: right;
}

.upload-demo {
    width: 100%;
}
</style>