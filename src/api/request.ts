import axios from "axios";

const _axios = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_API_BASE_URL,
})


_axios.interceptors.request.use(
    (config)=>{
        const token = localStorage.getItem('token');
        if ( token ) {
            // config.headers['Authorization'] = `Bearer ${token}`;
            // config.headers['user'] = 'admin';
            config.headers = {
                Authorization : `Bearer ${token}`,
                user : 'admin'
            }
            return config
        }
        config.headers = {

        }
        return config
    },
    (error)=>{
        return Promise.reject(error)
    }
)
_axios.interceptors.response.use(
    (response)=>{
        return response;
    },
    (error)=>{ // 失败，如果 >200, 400请求参数有问题, 401认证没有通过, 403权限没有通过, 500服务器出现异常

        console.error(error)
        if(error.response.status === 50000){
            // 情况1
        } else if(error.response.status === 401){
            // 情况2
        }

        // return Promise.reject(error)
        return Promise.resolve({})
    }
)

export default _axios
