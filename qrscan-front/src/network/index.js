import {
  HOST,
  TOKEN,
  TIME_OUT
} from '@/assets/js/const'
import axios from 'axios'

export function request(config) {

  const req = axios.create({
    baseURL: HOST,
    timeout: TIME_OUT
  })

  req.interceptors.request.use(config => {
    const token = localStorage.getItem(TOKEN)
    token && (config.headers.authorization = token)
    return config
  })

  req.interceptors.response.use(response => response.data)

  return req(config)
}
