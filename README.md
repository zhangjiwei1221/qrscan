## 前言

本文将介绍基于`SpringBoot + Vue + Android`实现的扫码登录`demo`的总体思路，完整代码已上传到[GitHub](https://github.com/zhangjiwei1221/qrscan)。`Web`端体验地址：http://42.192.64.144/qr， `apk`下载地址：https://github.com/zhangjiwei1221/qrscan/releases/tag/0.0.2 。用户名：非空即可，密码：123456，效果见文末，整体实现如有不妥之处，欢迎交流讨论，实现部分参考[二维码扫码登录是什么原理](https://juejin.cn/post/6940976355097985032)。



## 项目简介

后端：`SpringBoot`，`Redis`。
前端：`Vue`，`Vue Router`、`VueX`、`Axios`、`vue-qr`、`ElemntUI`。
安卓：`ZXing`、`XUI`、`YHttp`。



## 实现思路

总体的扫码登录和`OAuth2.0`的验证逻辑相似，如下所示：

![image-20210921205657426](https://img-blog.csdnimg.cn/img_convert/a091f34279d9dea7271e2bceab22b647.png)

用户选择扫码登录可以看作是`A`：前端发授权请求，等待`app`扫码。
用户使用`app`进行扫码可以看作是`B`：扫码进行授权，返回一个临时`Token`供二次认证。
用户在`app`进行确认登录可以看作是`C`：进行登录确认，授权用户在`Web`端登录。
后端在用户确认登录后返回一个正式`Token`即可看作是步骤`D`。
后续前端根据正式`Token`访问后台接口，正式在`Web`端进行操作即可看作是`E`和`F`。

**二次认证的原因**

之所以在用户扫码之后还需要进行再一次的确认登录，而不是直接就登录的原因，则是为了用户安全考虑，避免用户扫了其他人需要登录的二维码，在未经确认就直接登录了，导致他人可能会在我们不知道的情况下访问我们的信息。



## 实现步骤

1. 用户访问网页端，选择扫码登录

   用户在选择扫码登录时，会向后端发送一个二维码的生成请求，后端生成`UUID`，并保存到`Redis`（固定有效时间），状态设置为`UNUSED`（未使用）状态，如果`Redis`缓存过期，则为`EXPIRE`（过期）状态，前端根据后端返回的内容生成二维码，并设置一个定时器，每隔一段时间根据二维码的内容中的`UUID`，向后端发送请求，获取二维码的状态，更新界面展示的内容。

   生成二维码后端接口：

   ```java
   /**
    * 生成二维码内容
    *
    * @return 结果
    */
   @GetMapping("/generate")
   public BaseResult generate() {
       String code = IdUtil.simpleUUID();
       redisCache.setCacheObject(code, CodeUtils.getUnusedCodeInfo(), 
                                 DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
       return BaseResult.success(GENERATE_SUCCESS, code);
   }
   ```

   前端获取内容，生成二维码：

   ```javascript
   getToken() {
       this.codeStatus = 'EMPTY'
       this.tip = '正在获取登录码，请稍等'
       // 有效时间 60 秒
       this.effectiveSeconds = 60
       clearInterval(this.timer)
       request({
           method: 'get',
           url: '/code/generate'
       }).then((response) => {
           // 请求成功, 设置二维码内容, 并更新相关信息
           this.code = `${HOST}/code/scan?code=${response.data}`
           this.codeStatus = 'UNUSED'
           this.tip = '请使用手机扫码登录'
           this.timer = setInterval(this.getTokenInfo, 2000)
       }).catch(() => {
           this.getToken()
       })
   }
   ```

   后端返回二维码状态信息的接口：

   ```java
   /**
    * 获取二维码状态信息
    *
    * @param code 二维码
    * @return 结果
    */
   @GetMapping("/info")
   public BaseResult info(String code) {
       CodeVO codeVO = redisCache.getCacheObject(code);
       if (codeVO == null) {
           return BaseResult.success(INVALID_CODE, StringUtils.EMPTY);
       }
       return BaseResult.success(GET_SUCCESS, codeVO);
   }
   ```

   

   前端轮询获取二维码状态：

   ```javascript
   getTokenInfo() {
       this.effectiveSeconds--
       // 二维码过期
       if (this.effectiveSeconds <= 0) {
           this.codeStatus = 'EXPIRE'
           this.tip = '二维码已过期，请刷新'
           return
       }
       // 轮询查询二维码状态
       request({
           method: 'get',
           url: '/code/info',
           params: {
               code: this.code.substr(this.code.indexOf('=') + 1)
           }
       }).then(response => {
           const codeVO = response.data
           // 二维码过期
           if (!codeVO || !codeVO.codeStatus) {
               this.codeStatus = 'EXPIRE'
               this.tip = '二维码已过期，请刷新'
               return
           }
           // 二维码状态为为正在登录
           if (codeVO.codeStatus === 'CONFIRMING') {
               this.username = codeVO.username
               this.avatar = codeVO.avatar
               this.codeStatus = 'CONFIRMING'
               this.tip = '扫码成功，请在手机上确认'
               return
           }
           // 二维码状态为确认登录
           if (codeVO.codeStatus === 'CONFIRMED') {
               clearInterval(this.timer)
               const token = codeVO.token
               store.commit('setToken', token)
               this.$router.push('/home')
               Message.success('登录成功')
               return
           }
       })
   }
   ```

2. 使用手机扫码，二维码状态改变

   当用户使用手机扫码时（已登录并且为正确的`app`，否则扫码会跳转到自定义的宣传页），会更新二维码的状态为`CONFIRMING`（待确认）状态，并在`Redis`缓存中新增用户名及头像信息的保存供前端使用展示，此外还会返回用户的登录信息（登录地址、浏览器、操作系统）给`app`展示，同时生成一个临时`Token`给`app`（固定有效时间）。

   用户扫码时的后台处理：

   ```java
   /**
    * 处理未使用状态的二维码
    *
    * @param code 二维码
    * @param token token
    * @return 结果
    */
   private BaseResult handleUnusedQr(String code, String token) {
       // 校验 app 端访问传递的 token
       boolean isLegal = JwtUtils.verify(token);
       if (!isLegal) {
           return BaseResult.error(AUTHENTICATION_FAILED);
       }
       // 保存用户名、头像信息, 供前端展示
       String username = JwtUtils.getUsername(token);
       CodeVO codeVO = CodeUtils.getConfirmingCodeInfo(username, DEFAULT_AVATAR_URL);
       redisCache.setCacheObject(code, codeVO, DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
       // 返回登录地址、浏览器、操作系统以及一个临时 token 给 app
       String address = HttpUtils.getRealAddressByIp();
       String browser = HttpUtils.getBrowserName();
       String os = HttpUtils.getOsName();
       String tmpToken = JwtUtils.sign(username);
       // 将临时 token 作为键, 用户名为内容存储在 redis 中
       redisCache.setCacheObject(tmpToken, username, DEFAULT_TEMP_TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);
       LoginInfoVO loginInfoVO = new LoginInfoVO(address, browser, os, tmpToken);
       return BaseResult.success(SCAN_SUCCESS, loginInfoVO);
   }
   ```

3. 手机确认登录

   当用户在`app`中点击确认登录时，就会携带生成的临时`Token`发送更新状态的请求，二维码的状态会被更新为`CONFIRMED`（已确认登录）状态，同时后端会生成一个正式`Token`保存在`Redis`中，前端在轮询更新状态时获取这个`Token`，然后使用这个`Token`进行登录。

   后端处理确认登录的代码：

   ```java
   /**
    * 处理未待确认状态的二维码
    *
    * @param code 二维码
    * @param token token
    * @return 结果
    */
   private BaseResult handleConfirmingQr(String code, String token) {
       // 使用临时 token 获取用户名, 并从 redis 中删除临时 token
       String username = redisCache.getCacheObject(token);
       if (StringUtils.isBlank(username)) {
           return BaseResult.error(AUTHENTICATION_FAILED);
       }
       redisCache.deleteObject(token);
       // 根据用户名生成正式 token并保存在 redis 中供前端使用
       String formalToken = JwtUtils.sign(username);
       CodeVO codeVO = CodeUtils.getConfirmedCodeInfo(username, DEFAULT_AVATAR_URL, formalToken);
       redisCache.setCacheObject(code, codeVO, DEFAULT_QR_EXPIRE_SECONDS, TimeUnit.SECONDS);
       return BaseResult.success(CONFIRM_SUCCESS);
   }
   ```



## 效果演示
![在这里插入图片描述](https://img-blog.csdnimg.cn/9336ffbcb73f4fa791228279e660507a.gif#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/a3f87ea99cd24f5ab088871c70fb37e6.gif#pic_center)

