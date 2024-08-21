<template>
  <transition name="fade">
    <router-view></router-view>
  </transition>

  <ul class="bg-squares">
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
  </ul>
  
</template>

<script>
export default {
  name: 'App'
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Source Sans Pro", sans-serif;
}

body {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  /* background: linear-gradient(to top left, #ffe29f, #ffa99f, #ff719a); */
  background: #222;
  overflow: hidden;
}

ul {
    list-style: none;
}

/* 定义淡入淡出的动画效果 */
@keyframes fadeInOut {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 定义动画的持续时间和延迟 */
.fade-enter-active {
  animation: fadeInOut 0.7s;
}

.fade-leave-active {
  animation: fadeInOut 0.5s reverse;
}

.bg-squares {
  list-style: none;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index : 1;
}

.bg-squares li{
  width: 40px;
  height: 40px;
  background-color: rgba(255,255,255,0.15);
  position: absolute;
  bottom: -160px;
  animation: square 20s linear infinite;
}

.bg-squares li:nth-child(1) {
  left: 20%;
}

.bg-squares li:nth-child(2) {
  left: 20%;
  width: 80px;
  height: 80px;
  animation-delay: 2s;
  animation-duration: 17s;
}

.bg-squares li:nth-child(3) {
  left: 25%;
  animation-delay: 4s;
}

.bg-squares li:nth-child(4) {
  left: 40%;
  width: 60px;
  height: 60px;
  background-color: rgba(255,255,255,0.25);
  animation-duration: 22s;
}

.bg-squares li:nth-child(5) {
  left: 70%;
}

.bg-squares li:nth-child(6) {
  left: 80%;
  width: 120px;
  height: 120px;
  background-color: rgba(255,255,255,0.2);
  animation-delay: 3s;
}

.bg-squares li:nth-child(7) {
  left: 32%;
  width: 160px;
  height: 160px;
  animation-delay: 7s;
}

.bg-squares li:nth-child(8) {
  left: 55%;
  width: 20px;
  height: 20px;
  animation-delay: 15s;
  animation-duration: 40s;
}

.bg-squares li:nth-child(9) {
  left: 25%;
  width: 10px;
  height:10px;
  background-color: rgba(255,255,255,0.3);
  animation-delay: 2s;
  animation-duration: 40s;
}

.bg-squares li:nth-child(10) {
  left: 90%;
  width: 160px;
  height: 160px;
  animation-delay: 11s;
}

@keyframes square {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-120vh) rotate(600deg);
  }
}
</style>
