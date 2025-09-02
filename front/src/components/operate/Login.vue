<template>
  <div class="login-page">
    <div class="login-box">
      <div class="left-half">
        <div class="logo-image">
          <img src="../../assets/img/login.jpg" alt="零食插图">
        </div>
      </div>
      <div class="right-half">
        <div class="title-spacer"></div>
        <div class="system-title">
          <h1>零食商城系统</h1>
        </div>
        <div class="form-box">
          <router-view/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      // 不需要变量
    }
  },
  created() {
    // 可以在此处设置初始背景颜色
  }
}
</script>

<style scoped>
.login-page {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #FFE4E1 0%, #FFB6C1 100%); /* 零食商城风格的粉色渐变背景 */
}

.login-page .login-box {
  margin: 0;
  display: grid;
  grid-template-columns: 360px 1fr;
  width: 850px;
  height: 550px;
  padding: 0;
  background: rgba(255, 255, 255, 0.9); /* 使用半透明白色，让渐变背景透过来 */
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  border-radius: 12px;
  overflow: hidden;
}

.login-page .login-box .left-half {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ffd66b;
  position: relative;
  overflow: hidden;
}

.login-page .login-box .left-half .logo-image {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-page .login-box .left-half .logo-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.login-page .login-box .right-half {
  padding: 40px 40px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.login-page .login-box .right-half .title-spacer {
  height: 60px;
}

.login-page .login-box .right-half .system-title {
  text-align: center;
  margin-bottom: 50px;
}

.login-page .login-box .right-half .system-title h1 {
  color: #333;
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 1px;
}

/*右侧登录盒子*/
.login-page .login-box .right-half .form-box {
  width: 100%;
  height: auto;
  margin: 0 auto;
}
</style>
.login-page .login-box {
  margin: 0;
  display: grid;
  grid-template-columns: 360px 1fr;
  width: 850px;
  height: 550px;
  padding: 0;
  background: rgba(255, 255, 255, 0.9); /* 使用半透明白色，让渐变背景透过来 */
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  border-radius: 12px;
  overflow: hidden;
}
