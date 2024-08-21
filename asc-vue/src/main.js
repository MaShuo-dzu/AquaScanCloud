const debounce = (fn, delay) => {
let timer
    return (...args) => {
    if (timer) {
        clearTimeout(timer)
    }
    timer = setTimeout(() => {
        fn(...args)
    }, delay)
    }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
    constructor(callback) {
    callback = debounce(callback, 200);
    super(callback);
    }
}

import { createApp } from 'vue';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue';
import router from './router';
import dayjs from 'dayjs';


const Vue = createApp(App);

Vue.config.globalProperties.$dayjs = dayjs;

Vue.use(ElementPlus);
Vue.use(router);
Vue.mount('#app');