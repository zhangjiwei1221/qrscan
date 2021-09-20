import Vue from 'vue'
import Vuex from 'vuex'
import { TOKEN } from '@/assets/js/const'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    token: localStorage.getItem(TOKEN)
  },
  mutations: {
    setToken(state, token) {
      state.token = token
      localStorage.setItem(TOKEN, token)
    },
    removeToken(state) {
      localStorage.removeItem(TOKEN)
      state.token = null
    }
  }
})

export default store
