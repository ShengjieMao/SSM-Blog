<template>
  <div>
    <div class="container">
      <h1 class="loginTitle"></h1>
      <!-- Login/Signup -->
      <div v-show="!err2005" class="">
        <div v-if="login == 1" class="loginBox">
          <div class="lr-title">
            <h1>Sign in</h1>
            <p>New User<a href="#/Login?login=0" class="tcolors">Sign up</a></p>
          </div>
          <el-alert
            v-show="loginErr"
            :title="loginTitle"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="text"
            placeholder="username"
            v-model="username"
          ></el-input>

          <el-input
            type="password"
            placeholder="password"
            @keyup.enter.native="loginEnterFun"
            v-model="password"
          ></el-input>

          <h3><a href="">Forgot password?</a></h3>
          <div class="lr-btn tcolors-bg" @click="gotoHome">Sign in</div>
          <div class="otherLogin">
            <a href="javascript:void(0)"><i class="fa fa-fw fa-wechat"></i></a>
            <a href="javascript:void(0)"><i class="fa fa-fw fa-qq"></i></a>
            <a href="javascript:void(0)"><i class="fa fa-fw fa-weibo"></i></a>
          </div>
        </div>
        <div v-else class="registerBox">
          <div class="lr-title">
            <h1>Sign up</h1>
            <p>
              Already have an account?<a href="#/Login?login=1" class="tcolors"
                >Sign in</a
              >
            </p>
          </div>
          <el-alert
            v-show="registerErr"
            :title="registerTitle"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="text"
            placeholder="username"
            v-model="nusername"
          ></el-input>
          <el-alert
            v-show="nusernameErr"
            title="Wrong username"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="text"
            placeholder="Nickname"
            v-model="nnickName"
          ></el-input>
          <el-input
            type="text"
            placeholder="phone"
            v-model="phonenumber"
            :class="{ 'input-error': phonenumberErr }"
          ></el-input>
          <el-alert
            v-show="phonenumberErr"
            title="Wrong phone number"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="email"
            placeholder="email"
            v-model="nemail"
          ></el-input>
          <el-alert
            v-show="nemailErr"
            title="Wrong email address"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="password"
            placeholder="Password: Should be at least 6 digits. Letters, numbers, _ accept."
            v-model="npassword"
          ></el-input>
          <el-alert
            v-show="npasswordErr"
            title="Wrong password format"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <el-input
            type="password"
            placeholder="Confirm password"
            @keyup.enter.native="registerEnterFun"
            v-model="npassword2"
          ></el-input>
          <el-alert
            v-show="npassword2Err"
            title="Password not consistent"
            type="error"
            show-icon
            :closable="false"
          ></el-alert>
          <div
            class="lr-btn tcolors-bg"
            @click="newRegister"
            v-loading.fullscreen.lock="fullscreenLoading"
            element-loading-text="Submitting"
          >
            Register
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { userLogin, userRegister } from "../api/user.js";
import { setToken } from "../utils/auth.js";
export default {
  name: "Login",
  data() {
    return {
      username: "",
      email: "",
      password: "",
      nusername: "",
      nemail: "",
      npassword: "",
      npassword2: "",
      phonenumber: "",
      login: 0,
      loginErr: false,
      loginTitle: "Wrong password or username",
      nusernameErr: false,
      nemailErr: false,
      npasswordErr: false,
      npassword2Err: false,
      registerErr: false,
      registerTitle: "The email already exists",
      step: 1,
      fullscreenLoading: false,
      urlstate: 0,
      phonenumberErr: false,
    };
  },
  methods: {
    routeChange: function () {
      var that = this;
      that.login =
        that.$route.query.login == undefined
          ? 1
          : parseInt(that.$route.query.login);
      that.urlstate =
        that.$route.query.urlstate == undefined
          ? 0
          : that.$route.query.urlstate;
    },
    loginEnterFun: function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        this.gotoHome();
      }
    },
    gotoHome: function () {
      userLogin(this.username, this.password).then((response) => {
        setToken(response.token);

        localStorage.setItem("userInfo", JSON.stringify(response.userInfo));
        if (localStorage.getItem("logUrl")) {
          this.$router.push({ path: localStorage.getItem("logUrl") });
        } else {
          this.$router.push({ path: "/" });
        }
      });
    },
    registerEnterFun: function (e) {
      var keyCode = window.event ? e.keyCode : e.which;
      if (keyCode == 13) {
        this.newRegister();
      }
    },
    newRegister: function () {
      var that = this;
      var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
      var preg = /^(\w){6,12}$/;
      var phoneRegex = /^1\d{10}$/; // Regular expression for phone number

      if (that.nusername) {
        that.nusernameErr = false;
      } else {
        that.nusernameErr = true;
      }

      if (reg.test(that.nemail)) {
        that.nemailErr = false;
      } else {
        that.nemailErr = true;
      }

      if (that.npassword && preg.test(that.npassword)) {
        that.npasswordErr = false;
      } else {
        that.npasswordErr = true;
      }

      if (that.npassword === that.npassword2) {
        that.npassword2Err = false;
      } else {
        that.npassword2Err = true;
      }

      // Validate phone number using the regular expression
      if (phoneRegex.test(that.phonenumber)) {
        that.phonenumberErr = false;
      } else {
        that.phonenumberErr = true; // Phone number is invalid, set the error flag
      }

      if (
        !that.nusernameErr &&
        !that.nemailErr &&
        !that.npasswordErr &&
        !that.npassword2Err &&
        !that.phonenumberErr // Check for phone number validation
      ) {
        that.fullscreenLoading = true;
        userRegister(
          that.nusername,
          that.nnickName,
          that.phonenumber,
          that.nemail,
          that.npassword
        )
          .then((response) => {
            that.goLogin();
          })
          .catch((error) => {
            that.fullscreenLoading = false;
          });
      }
    },
    goLogin: function () {
      this.$router.push({ path: "/Login?login=1" });
    },
    goRegister: function () {
      this.$router.push({ path: "/Login?login=0" });
    },
  },
  components: {},
  watch: {
    $route: "routeChange",
  },
  created() {
    var that = this;
    that.routeChange();
  },
};
</script>

<style>
.loginTitle {
  text-align: center;
  font-size: 26px;
  padding-top: 50px;
  margin-bottom: 20px;
}
.loginBox,
.registerBox {
  background: #fff;
  padding: 40px;
  max-width: 320px;
  margin: 0 auto;
}
.loginBox {
  padding-bottom: 0;
}
.lr-title {
  position: relative;
  height: 32px;
  line-height: 32px;
  margin-bottom: 20px;
}
.lr-title h1 {
  font-size: 24px;
  color: #666;
  font-weight: bold;
  /*width:50%;*/
}
.lr-title p {
  font-size: 12px;
  color: #999;
  position: absolute;
  right: 0;
  top: 0;
}
.lr-btn {
  color: #fff;
  text-align: center;
  letter-spacing: 5px;
  padding: 8px;
  border-radius: 5px;
  cursor: pointer;
  margin-bottom: 30px;
}
.loginBox .el-input,
.registerBox .el-input {
  margin-bottom: 20px;
}
.loginBox .el-alert,
.registerBox .el-alert {
  top: -18px;
  background-color: #888;
}
.loginBox .el-input input,
.registerBox .el-input input {
  border-radius: 4px;
}
.loginBox h3,
.registerBox h3 {
  text-align: right;
  margin-bottom: 20px;
}
.loginBox h3 a,
.registerBox h3 a {
  font-size: 13px;
  color: #999;
}
.loginBox .otherLogin {
  max-width: 320px;
  padding: 30px 40px;
  background: #ddd;
  text-align: center;
  margin-left: -40px;
  margin-right: -40px;
  visibility: hidden;
}
.loginBox .otherLogin p {
  margin-bottom: 20px;
  font-size: 16px;
}
.loginBox .otherLogin a i {
  display: inline-block;
  width: 42px;
  height: 42px;
  line-height: 42px;
  font-size: 18px;
  border-radius: 50%;
  color: #fff;
  margin: 0 10px;
}
.loginBox .otherLogin a i.fa-wechat {
  background: #7bc549;
}
.loginBox .otherLogin a i.fa-qq {
  background: #56b6e7;
}
.loginBox .otherLogin a i.fa-weibo {
  background: #ff763b;
}

.registerSuc {
  padding: 40px;
  margin: 0 auto;
}
.registerSuc .sucIcon {
  text-align: center;
  margin-bottom: 30px;
  padding-left: 60px;
}
.registerSuc .sucContent {
  line-height: 1.5;
  font-size: 15px;
  text-align: center;
}
.registerSuc .sucContent p {
  margin-top: 10px;
  font-size: 13px;
  color: #999;
}
.registerSuc .sucContent .lastbtn {
  display: inline-block;
  font-size: 14px;
  padding: 3px 10px;
  border-radius: 5px;
  color: #fff;
  cursor: pointer;
}
.registerSuc .sucContent .el-icon-close {
  font-size: 13px;
}
</style>
