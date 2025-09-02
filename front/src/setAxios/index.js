// Vue-Mall/src/setAxios/index.js
import axios from 'axios'
import store from '../../src/store'
import router from '../../src/router'
import {Notification} from "element-ui";

//http全局拦截
//token要放在我们请求的header上面带回给后端
export default function index(){
  //请求拦截
  axios.interceptors.request.use(
    config=>{
      if(store.state.token){
        config.headers.token=store.state.token
      }
      return config
    }
  )
  //每次请求有返回的，都是先警告拦截器最先的
  axios.interceptors.response.use(
    response=>{
      if(response.status===200){
        const data=response.data
        if(data.code === 401){
          // 清除登录信息
          store.commit('setToken',null)
          store.commit('setRole',null)
          store.commit('setUser',null)
          store.commit('setRoleInfo',null)
          localStorage.clear()
          
          // 根据消息类型显示不同提示
          if(data.message === "账号已被顶下线"){
            Notification({
              title: '警告',
              message: '您的账号已在其他地方登录',
              type: 'warning'
            });
          } else {
            Notification({
              title: '警告',
              message: data.message,
              type: 'warning'
            });
          }
          
          // 使用 router.push 而不是 router.replace
          router.push('/loginForm')
          
          // 阻止后续的 then 处理
          return Promise.reject(data)
        }
        else if(data.code === 403){
          //暂无权限
          Notification({
            title: '警告',
            message: data.message,
            type: 'warning'
          });
        }
      }
      return response
    },
    error => {
      // 处理请求错误
      return Promise.reject(error)
    }
  )
}