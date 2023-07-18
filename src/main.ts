import { createApp } from 'vue'
import App from './App.vue'
import install from './install'
import './permission'
import './styles/index.scss'
import 'virtual:svg-icons-register'
import './static/iconfont/iconfont.css'
import './Mock/Mock.js'
import * as ECharts from 'echarts'

const app = createApp(App)
app.use(install)

app.config.globalProperties.$ECharts = ECharts

app.mount('#app')



