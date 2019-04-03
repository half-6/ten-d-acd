import axios from 'axios'
import qs from 'qs';
import { Message } from 'element-ui'
import { cacheAdapterEnhancer } from 'axios-extensions';

const service = axios.create({
  baseURL: "/", // api 的 base_url
  timeout: 500000, // 请求超时时间
  adapter: cacheAdapterEnhancer(axios.defaults.adapter, { enabledByDefault: false, cacheFlag: 'useCache'})
})

// request拦截器
service.interceptors.request.use(
  config => {
    config.headers['X-Token'] = "TEN-D-ACD"
    if (config.data && config.headers['Content-Type'] === 'application/x-www-form-urlencoded') {
        config.data = qs.stringify(config.data);
    }
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if(res.code === 200)
    {
        return res.response;
    }
    Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
    })
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: "Unknown error, please try again, Contact admin for more information.",
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
export default service
