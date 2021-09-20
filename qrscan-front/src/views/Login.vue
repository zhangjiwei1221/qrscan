<template>
  <div class="main">
    <div v-if="qrMethod" class="login-form">
      <div class="login-pc" @click="qrMethod = !qrMethod"></div>
      <QrLogin/>
    </div>
    <div class="login-form" v-else>
      <div class="login-qr" @click="qrMethod = !qrMethod"></div>
      <el-form :model="loginForm" :rules="rules" status-icon ref="loginForm" >
        <el-form-item prop="username">
          <el-input
            style="margin-top: 60px;"
            v-model.trim="loginForm.username"
            autocomplete="off"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            v-model.trim="loginForm.password"
            autocomplete="off"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit" class="login-btn">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  Form,
  Input,
  Button,
  Loading,
  Message,
  FormItem
} from 'element-ui'
import store from '@/store'
import { request } from '@/network'
import QrLogin from "@/components/qr-login";

export default {
  name: 'Login',
  components: {
    QrLogin,
    'el-form': Form,
    'el-input': Input,
    'el-button': Button,
    'el-form-item': FormItem
  },
  data() {
    return {
      qrMethod: false,
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.loginForm.validate(valid => {
        if (!valid) {
          return false
        }
        const loading = Loading.service({ fullscreen: true })
        const form = this.loginForm
        request({
          method: 'post',
          url: '/login',
          data: {
            'username': form.username,
            'password': form.password
          }
        }).then(res => {
          if (!res || !res.data) {
            Message.error('用户名或密码错误')
          }
          store.commit('setToken', res.data)
          this.$router.push('/home')
          Message.success('登录成功')
        }).catch(() => {
          Message.error('未知错误')
        }).finally(() => {
          loading.close()
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.main {
  width: 100%;
  height: calc(50vh + 240px);
  background-size: cover;
  padding-top: calc(50vh - 240px);
  background-image: url(../assets/img/background.jpg);
}

.login-qr {
  width: 64px;
  height: 64px;
  float: right;
  background: url(../assets/img/qrcode.png) no-repeat;
  border-top-right-radius: 5px;
  cursor: pointer;
}

.login-pc {
  width: 64px;
  height: 64px;
  float: right;
  background: url(../assets/img/pcinput.png) no-repeat;
  border-top-right-radius: 5px;
  cursor: pointer;
}

.login-form {
  width: 370px;
  height: 450px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 5px;
  background-color: #ffffff;
}

.el-form-item {
  width: 80%;
  margin-left: auto;
  margin-right: auto;
}

.el-button {
  width: 100%;
}
</style>
