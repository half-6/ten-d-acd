import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    info: null
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INFO: (state,user)=>{
      state.info = user
    }
  },

  actions: {
    // 登录
    async Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const data = await login(username, userInfo.password)
      setToken(data)
      commit('SET_TOKEN', data)
      return data
    },

    // 获取用户信息
    async GetInfo({ commit, state }) {
      const data = await getInfo()
      if (!data.roles || data.roles.length === 0){
        throw new Error('getInfo: roles must be a non-null array !')
      }
      commit('SET_INFO',data)
      return data
    },

    // 登出
    async LogOut({ commit, state }) {
      await logout(state.token)
      commit('SET_INFO', null)
      commit('SET_TOKEN', null)
      removeToken()
    },

    // 前端 登出
    async FedLogOut({ commit }) {
      commit('SET_INFO', null)
      commit('SET_TOKEN', null)
      removeToken()
    }
  }
}

export default user
