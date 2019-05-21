import axios from 'axios'
import _ from 'lodash'
import qs from 'qs';
import { Message } from 'element-ui'
import { cacheAdapterEnhancer } from 'axios-extensions';
import { getToken } from '@/utils/auth'

const service = axios.create({
  baseURL: "/api/", // api base_url
  timeout: 500000, // request timeout
  adapter: cacheAdapterEnhancer(axios.defaults.adapter, { enabledByDefault: false, cacheFlag: 'useCache'})
})

// request拦截器
service.interceptors.request.use(
  config => {
    config.headers['X-Token'] = "TEN-D-ACD";
    const token = getToken();
    token && (config.headers['Authorization'] = 'Bearer ' + token);
    if (config.data && config.headers['Content-Type'] === 'application/x-www-form-urlencoded') {
        config.data = qs.stringify(config.data);
    }
    console.log(`${config.method}:${config.url}`)
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
    const statusCode = _.get(error,"response.status")
    const message = _.get(error,"response.data.message")
    Message({
      message: message || "Unknown error, please try again, Contact admin for more information.",
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
export default service
