import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';

import RegisterFishermanView from '../views/fishermen/RegisterFishermanView.vue';
import RegisterplatoonView from '../views/platoon/RegisterplatoonView.vue';

import HomeFishermanView from '../views/fishermen/HomeFishermanView.vue';
import DashBoardFishermenView from '../views/fishermen/DashBoardFishermenView.vue'
import PersonalInfoEditFishermenView from '../views/fishermen/PersonalInfoEditFishermenView.vue'
import AlgorithmMonitorFishermenView from '../views/fishermen/AlgorithmMonitorFishermenView.vue'
import WeatherMonitorFishermenView from '../views/fishermen/WeatherMonitorFishermenView.vue'
import FishPointManageFishermenView from "../views/fishermen/FishPointManageFishermenView.vue"

import HomePlatoonView from '../views/platoon/HomePlatoonView.vue';

import UpdateView from '../views/UpdateView.vue';
import BeforeView from '../views/BeforeView.vue';
import AfterView from '../views/AfterView.vue';

import IdBindingView from '../views/IdBindingView.vue';
import EmailBindingView from '../views/EmailBindingView.vue';
import UnauthorizedView from '../views/UnauthorizedView.vue';

import MapCoordinatePickerView from '../views/MapCoordinatePickerView.vue';

// 管理员不在这里面
const routes = [
  { path: '/', redirect: '/login' },

  { path: '/login', 
    component: LoginView ,
  },
  { path: '/register-fisherman', component: RegisterFishermanView },
  { path: '/register-platoon', component: RegisterplatoonView },

  { path: '/home-fisherman', component: HomeFishermanView, meta: { requiresAuth: true, role: 'fisherman' },
    children: [
      {
        path: 'dashboard',
        name: 'DashBoardFishermen',
        component: DashBoardFishermenView
      },
      {
        path: 'id-binding',
        name: 'IdBinding',
        component: IdBindingView
      },
      {
        path: 'email-binding',
        name: 'EmailBinding',
        component: EmailBindingView
      },
      {
        path: 'personal-info-edit',
        name: 'PersonalInfoEditFishermen',
        component: PersonalInfoEditFishermenView
      },
      {
        path: 'algorithm-monitor',
        name: 'AlgorithmMonitorFishermen',
        component: AlgorithmMonitorFishermenView
      },
      {
        path: 'weather-monitor',
        name: 'WeatherMonitorFishermen',
        component: WeatherMonitorFishermenView
      },
      {
        path: 'fish-point-add',
        name: 'MapCoordinatePicker',
        component: MapCoordinatePickerView  // 这个地方是百度地图的vue模板
      },
      {
        path: 'fish-point-manage',
        name: 'FishPointManageFishermen',
        component: FishPointManageFishermenView 
      },
    ]
  },

  { path: '/home-platoon', component: HomePlatoonView, meta: { requiresAuth: true , role: 'platoon'} },

  { path: '/update/:role', 
    component: UpdateView, 
    children: [
      {
        path: 'before',
        name: 'Before',
        component: BeforeView
      },
      {
        path: 'after',
        name: 'After',
        component: AfterView
      }
    ]
  },

  { path: '/unauthorized', component: UnauthorizedView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach(async (to, from, next) => {

  let tokenObj;
  
  try{
    tokenObj = JSON.parse(localStorage.getItem('token'));
  }

  catch{
    tokenObj = null
  }

  const isAuthenticated = tokenObj?.token;
  const userRole = tokenObj?.role;

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/login');
    } else {
      const authorized = to.matched.some(record => {
        if (record.meta.role && record.meta.role !== userRole) {
          return false;
        }
        return true;
      });
  
      if (!authorized) {
        next('/unauthorized'); // 确保 '/unauthorized' 是一个有效的路由
      } else {
        next();
      }
    }
  } else {
    next();
  }
  
});

export default router;