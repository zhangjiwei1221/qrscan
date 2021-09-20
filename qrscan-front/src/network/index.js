import {
  API,
  TOKEN,
  TIME_OUT
} from '@/assets/js/const'
import axios from 'axios'
import store from '@/store'
import router from '@/router'

export function request(config) {

  const req = axios.create({
    baseURL: API,
    timeout: TIME_OUT
  })

  req.interceptors.request.use(config => {
    const token = localStorage.getItem(TOKEN)
    token && (config.headers.authorization = token)
    return config
  })

  req.interceptors.response.use(response => {
    const token = response.headers.authorization
    if (token) {
      store.commit('setToken', token)
    } else {
      store.commit('removeToken')
      const history = router.history
      if (history && history.current) {
        !['/login'].includes(history.current.path) && router.push('/login')
      }
    }
    return response.data
  }, () => {
    router.push('/home')
  })

  return req(config)
}
