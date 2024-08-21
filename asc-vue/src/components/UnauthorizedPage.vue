<template>
    <div class="unauthorized-page">
      <div class="unauthorized-content">
        <h1 class="error-code">出错啦</h1>
        <p>您没有足够的权限访问此界面哦。</p>
        <router-link to="/" class="back-home-btn">~返回~</router-link>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "UnauthorizedPage"
  };
  </script>
  
  <style scoped>
  .unauthorized-page {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200vh;
    background-size: cover;
    z-index: 2;
  }
  
  .unauthorized-content {
    text-align: center;
    background: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    padding: 40px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .error-code {
    font-size: 150px;
    font-weight: bold;
    color: #ff6b6b;
    text-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    margin: 0;
    animation: breathing 3s ease-in-out infinite;
    font-family: 'Brush Script MT', cursive;
  }
  
  @keyframes breathing {
    0%, 100% {
      text-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
      transform: scale(1);
    }
    50% {
      text-shadow: 0 0 20px rgba(0, 0, 0, 1);
      transform: scale(1.05);
    }
  }
  
  .unauthorized-content h2 {
    font-size: 32px;
    color: #333;
    margin-bottom: 20px;
  }
  
  .unauthorized-content p {
    font-size: 18px;
    color: #666;
    margin-bottom: 30px;
  }
  
  .back-home-btn {
    display: inline-block;
    padding: 10px 20px;
    font-size: 18px;
    color: white;
    background-color: #007bff;
    border-radius: 5px;
    text-decoration: none;
    transition: background-color 0.3s ease;
  }
  
  .back-home-btn:hover {
    background-color: #0056b3;
  }
  </style>
  