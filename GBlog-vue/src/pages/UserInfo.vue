<template>
  <div>
    <wbc-nav></wbc-nav>
    <div class="container">
      <div v-show="isEdit" class="tcommonBox">
        <header>
          <h1>Edit your bio</h1>
        </header>
        <section>
          <ul class="userInfoBox">
            <li class="avatarlist">
              <span class="leftTitle">Avatar</span>
              <el-upload
                class="avatar-uploader"
                name="img"
                :action="uploadURL"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="userInfoObj.avatar"
                  :src="
                    userInfoObj.avatar
                      ? userInfoObj.avatar
                      : 'static/img/tou.jpg'
                  "
                  :onerror="$store.state.errorImg"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <div slot="tip" class="el-upload__tip">
                  Click the image to upload your avatar
                </div>
              </el-upload>
            </li>
            <li class="username">
              <span class="leftTitle">Username</span>
              <el-input
                v-model="userInfoObj.nickName"
                placeholder="username"
              ></el-input>
              <i class="fa fa-wa fa-asterisk"></i>
            </li>
            <li>
              <span class="leftTitle">email</span>
              <span>{{ userInfoObj.email }}</span>
            </li>
            <!-- <li class="username">
                            <span class="leftTitle">Password</span>
                            <el-input v-model="userInfoObj.password" placeholder="password"></el-input> <i  class="fa fa-wa fa-asterisk"></i>
                        </li> -->
            <li>
              <span class="leftTitle">Gender</span>
              <template>
                <el-radio class="radio" v-model="userInfoObj.sex" label="0"
                  >Male</el-radio
                >
                <el-radio class="radio" v-model="userInfoObj.sex" label="1"
                  >Female</el-radio
                >
                <!-- <el-radio class="radio" v-model="userInfoObj.sex" label="1">Other</el-radio> -->
              </template>
            </li>
          </ul>
          <div class="saveInfobtn">
            <a
              class="tcolors-bg"
              href="javascript:void(0);"
              @click="isEdit = !isEdit"
              >Go back</a
            >
            <a
              class="tcolors-bg"
              href="javascript:void(0);"
              @click="saveInfoFun"
              >Save</a
            >
          </div>
        </section>
      </div>
      <div v-show="!isEdit" class="tcommonBox">
        <header>
          <h1>
            My Profile
            <span class="gotoEdit" @click="isEdit = !isEdit"
              ><i class="fa fa-wa fa-edit"></i>Edit</span
            >
          </h1>
        </header>
        <section>
          <ul class="userInfoBox">
            <li class="avatarlist">
              <span class="leftTitle">Avatar</span>
              <div class="avatar-uploader">
                <img
                  :src="
                    userInfoObj.avatar
                      ? userInfoObj.avatar
                      : 'static/img/tou.jpg'
                  "
                  :onerror="$store.state.errorImg"
                  class="avatar"
                />
              </div>
            </li>
            <li class="username">
              <span class="leftTitle">Username</span>
              <span>{{
                userInfoObj.nickName ? userInfoObj.nickName : "Nan"
              }}</span>
            </li>
            <li>
              <span class="leftTitle">Email</span>
              <span>{{ userInfoObj.email ? userInfoObj.email : "Nan" }}</span>
            </li>
            <!-- <li class="username">
                            <span class="leftTitle">Password</span>
                            <span>{{userInfoObj.password?userInfoObj.password:"Nan"}}</span>

                        </li> -->
            <li>
              <span class="leftTitle">Gender</span>
              <span>{{ userInfoObj.sex == 0 ? "Female" : "Male" }}</span>
            </li>
          </ul>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import header from "../components/header.vue";
import { getUserInfo, savaUserInfo } from "../api/user.js";
import store from "../store";
export default {
  name: "UserInfo",
  data() {
    return {
      uploadURL: "",
      isEdit: false,
      userInfo: {},
      userInfoObj: "",
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      if (res.code == 200) {
        this.userInfoObj.avatar = res.data;
        this.userInfoObj.head_start = 1;
      } else {
        this.$message.error("Failed to upload your avatar");
      }
    },
    beforeAvatarUpload(file) {
      // Check if the image size is less than 2MB
      const isJPG =
        file.type == "image/png" ||
        file.type == "image/jpg" ||
        file.type == "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      // console.log(file);
      if (!isJPG) {
        this.$message.error(
          "Please only upload your avatar in JPG/PNG format."
        );
      }
      if (!isLt2M) {
        this.$message.error("The size of your image cannot succeed 2MB.");
      }
      return isJPG && isLt2M;
    },

    saveInfoFun: function () {
      var that = this;

      if (!that.userInfoObj.nickName) {
        that.$message.error("Please write your username");
        return;
      }

      if (!that.userInfoObj.password) {
        that.$message.error("Please write your password");
        return;
      }

      savaUserInfo(that.userInfoObj).then((response) => {
        that.$message.success("Saved successfully");
        that.isEdit = false;
        that.routeChange();
      });
    },
    routeChange: function () {
      var that = this;
      if (localStorage.getItem("userInfo")) {
        that.haslogin = true;
        that.userInfo = JSON.parse(localStorage.getItem("userInfo"));
        that.userId = that.userInfo.id;
        getUserInfo(that.userId).then((response) => {
          that.userInfoObj = response;
          that.userInfoObj.head_start = 0;
        });
      } else {
        that.haslogin = false;
      }
    },
  },
  components: {
    "wbc-nav": header,
  },
  watch: {
    $route: "routeChange",
  },
  created() {
    this.routeChange();
    this.uploadURL = store.state.baseURL + "upload";
  },
};
</script>

<style>
.userInfoBox .avatarlist {
  position: relative;
}

.avatar-uploader {
  display: inline-block;
  vertical-align: top;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  position: absolute;
  top: 0;
  left: 0;
}
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: block;
  object-fit: cover;
}
.gotoEdit {
  font-size: 15px;
  float: right;
  cursor: pointer;
  color: #999;
}
.gotoEdit:hover {
  color: #333;
}

.userInfoBox .leftTitle {
  display: inline-block;
  width: 100px;
  padding: 10px 0;
}
.userInfoBox .rightInner {
  display: inline-block;
  max-width: calc(100% - 140px);
  vertical-align: top;
}
.userInfoBox li {
  padding: 20px;
  border-bottom: 1px solid #ddd;
}
.userInfoBox li:last-child {
  border-bottom: 1px solid transparent;
}
.userInfoBox .el-input,
.userInfoBox .el-textarea {
  max-width: 300px;
  min-width: 100px;
}

.userInfoBox .el-input__inner {
  border-radius: 4px;
}
.userInfoBox .el-textarea {
  vertical-align: top;
}
.saveInfobtn {
  margin: 20px 0;
  text-align: center;
}
.saveInfobtn a {
  color: #fff;
  padding: 6px 20px;
  margin: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
}
.userInfoBox .fa-asterisk {
  color: #df2050;
}
</style>
