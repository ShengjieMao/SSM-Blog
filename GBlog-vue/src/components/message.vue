<template>
  <div class="tmsgBox" ref="tmsgBox">
    <div class="tmsg-respond" ref="respondBox">
      <h3>
        Leave a comment
        <small v-show="isRespond" class="tcolorm" @click="removeRespond"
          >Cancel</small
        >
      </h3>
      <form class="">
        <el-input type="textarea" :rows="2" placeholder="Hey I like your
        article, I want to say something..."``" v-model="textarea">
        <!-- </el-input> -->
        <div :class="pBody ? 'OwO' : 'OwO OwO-open'">
          <div class="OwO-logo" @click="pBody = !pBody">
            <span>Emoji</span>
          </div>
          <div class="OwO-body">
            <ul class="OwO-items OwO-items-show">
              <li
                class="OwO-item"
                v-for="(oitem, index) in OwOlist"
                :key="'oitem' + index"
                @click="choseEmoji(oitem.title)"
              >
                <img :src="'static/img/emot/image/' + oitem.url" alt="" />
              </li>
            </ul>
            <div class="OwO-bar">
              <ul class="OwO-packages">
                <li class="OwO-package-active">Emoji</li>
              </ul>
            </div>
          </div>
        </div>
        <el-row class="tmsg-r-info">
          <el-col :span="24" class="info-submit">
            <p class="tcolors-bg" @click="sendMsg">{{ sendTip }}</p>
          </el-col>
        </el-row>
      </form>
    </div>
    <div class="tmsg-comments" ref="listDom">
      <a href="#" class="tmsg-comments-tip"
        >Looks like we have
        {{ commentList ? commentList.length : 0 }} comments!</a
      >
      <div class="tmsg-commentshow">
        <ul class="tmsg-commentlist">
          <li
            class="tmsg-c-item"
            v-for="(item, index) in commentList"
            :key="'common' + index"
          >
            <article class="">
              <header>
                <img
                  :src="$store.state.errorImg"
                  :onerror="$store.state.errorImg"
                />
                <div class="i-name">
                  {{ item.username }}
                </div>
                <!-- <div class="i-class">
                                    {{item.label}}
                                </div> -->
                <div class="i-time">
                  <time>{{ item.createTime }}</time>
                </div>
              </header>
              <section>
                <p v-html="analyzeEmoji(item.content)">
                  {{ analyzeEmoji(item.content) }}
                </p>
                <div
                  v-if="haslogin"
                  class="tmsg-replay"
                  @click="respondMsg(item.id, item.id, item.createBy)"
                >
                  Reply
                </div>
              </section>
            </article>
            <ul
              v-show="item.children"
              class="tmsg-commentlist"
              style="padding-left: 60px"
            >
              <li
                class="tmsg-c-item"
                v-for="(citem, cindex) in item.children"
                :key="'citem' + cindex"
              >
                <article class="">
                  <header>
                    <img
                      :src="$store.state.errorImg"
                      :onerror="$store.state.errorImg"
                    />
                    <div class="i-name">
                      {{ citem.username }} <span>Reply</span>
                      {{ citem.toCommentUserName }}
                    </div>
                    <div class="i-time">
                      <time>{{ citem.createTime }}</time>
                    </div>
                  </header>
                  <section>
                    <p v-html="analyzeEmoji(citem.content)">
                      {{ citem.content }}
                    </p>
                    <div
                      v-show="haslogin"
                      class="tmsg-replay"
                      @click="respondMsg(item.id, citem.id, citem.createBy)"
                    >
                      Reply
                    </div>
                  </section>
                </article>
              </li>
            </ul>
          </li>
        </ul>
        <h1 v-show="hasMore" class="tcolors-bg" @click="addMoreFun">
          View more
        </h1>
        <h1 v-show="!hasMore" class="tcolors-bg">End</h1>
      </div>
    </div>
  </div>
</template>

<script>
import {
  sendComment,
  getArticleComment,
  getLinkComment,
} from "../api/comment.js";
import { getToken } from "../utils/auth.js";
export default {
  data() {
    return {
      respondBox: "",
      listDom: "",
      tmsgBox: "",

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        articleId: 0,
      },
      isRespond: false,
      textarea: "",
      pBody: true,
      commentList: [],
      aid: 0,
      hasMore: false,
      haslogin: false,
      userId: "",
      type: 0,
      leavePid: "",
      pid: "",
      rootId: -1,
      toCommentId: -1,
      toCommentUserId: -1,
      sendTip: "Send",
      OwOlist: [
        { title: "Smil", url: "weixiao.gif" },
        { title: "Yo", url: "xixi.gif" },
        { title: "Lol", url: "haha.gif" },
        { title: "Cute", url: "keai.gif" },
        { title: "OHHH", url: "kelian.gif" },
        { title: "EMMM", url: "wabi.gif" },
        { title: "OH!", url: "chijing.gif" },
        { title: "Shy", url: "haixiu.gif" },
        { title: "Wink", url: "jiyan.gif" },
        { title: "Silent", url: "bizui.gif" },
        { title: "Despise", url: "bishi.gif" },
        { title: "Love", url: "aini.gif" },
        { title: "Cry", url: "lei.gif" },
        { title: "I got you", url: "touxiao.gif" },
        { title: "Kiss", url: "qinqin.gif" },
        { title: "Sick", url: "shengbing.gif" },
        { title: "Excited", url: "taikaixin.gif" },
        { title: "Eyes roll", url: "baiyan.gif" },
        { title: "Well...", url: "youhengheng.gif" },
        { title: "Shush", url: "xu.gif" },
        { title: "Feel bad", url: "shuai.gif" },
        { title: "EWWWW", url: "tu.gif" },
        { title: "Boring", url: "haqian.gif" },
        { title: "Hug", url: "baobao.gif" },
        { title: "ANGRY", url: "nu.gif" },
        { title: "Concern", url: "yiwen.gif" },
        { title: "Desire", url: "chanzui.gif" },
        { title: "Bye", url: "baibai.gif" },
        { title: "Pondering", url: "sikao.gif" },
        { title: "Embarassed", url: "han.gif" },
        { title: "Sleepy", url: "kun.gif" },
        { title: "Money", url: "qian.gif" },
        { title: "Disappointed", url: "shiwang.gif" },
        { title: "Cool", url: "ku.gif" },
        { title: "Clap", url: "guzhang.gif" },
        { title: "Dizzy", url: "yun.gif" },
        { title: "Sad", url: "beishang.gif" },
        { title: "Crazy", url: "zhuakuang.gif" },
        { title: "...", url: "heixian.gif" },
        { title: "A bad idea...", url: "yinxian.gif" },
        { title: "Sick", url: "ganmao.gif" },
        { title: "Heart", url: "xin.gif" },
        { title: "Heart broken", url: "shangxin.gif" },
        { title: "Pig", url: "zhu.gif" },
        { title: "OK", url: "ok.gif" },
        { title: "Yeah", url: "ye.gif" },
        { title: "Good", url: "good.gif" },
        { title: "No", url: "no.gif" },
        { title: "Great", url: "zan.gif" },
        { title: "Come", url: "lai.gif" },
        { title: "Just have a look", url: "weiguan.gif" },
        { title: "Mic", url: "huatong.gif" },
        { title: "Candle", url: "lazhu.gif" },
        { title: "Cake", url: "dangao.gif" },
      ],
    };
  },
  methods: {
    setData(initData, result) {
      var msg = result.rows;
      if (initData) {
        this.commentList = msg;
      } else {
        this.commentList = this.commentList.concat(msg);
      }

      this.hasMore = result.total > this.commentList.length;
    },
    choseEmoji: function (inner) {
      this.textarea += "[" + inner + "]";
    },
    analyzeEmoji: function (cont) {
      var pattern1 = /\[[\u4e00-\u9fa5]+\]/g;
      var pattern2 = /\[[\u4e00-\u9fa5]+\]/;
      var content = cont.match(pattern1);
      var str = cont;
      if (content) {
        for (var i = 0; i < content.length; i++) {
          for (var j = 0; j < this.OwOlist.length; j++) {
            if ("[" + this.OwOlist[j].title + "]" == content[i]) {
              var src = this.OwOlist[j].url;
              break;
            }
          }
          str = str.replace(
            pattern2,
            '<img src="static/img/emot/image/' + src + '"/>'
          );
        }
        // console.log(str);
      }
      return str;
    },
    sendMsg: function () {
      var that = this;
      if (that.textarea) {
        that.sendTip = "GO!";
        sendComment(
          that.type,
          that.aid,
          that.rootId,
          that.toCommentId,
          that.toCommentUserId,
          that.textarea
        ).then((response) => {
          that.textarea = "";
          that.rootId = -1;
          that.toCommentId = -1;
          that.toCommentUserId = -1;

          that.routeChange();
          that.removeRespond();
          var timer02 = setTimeout(function () {
            that.sendTip = "Send!";
            clearTimeout(timer02);
          }, 1000);
        });
      } else {
        that.sendTip = "Please write something...";
        var timer = setTimeout(function () {
          that.sendTip = "Send!";
          clearTimeout(timer);
        }, 3000);
      }
    },
    respondMsg: function (rootId, toCommentId, toCommentUserId) {
      // console.log(leavePid,pid);
      var that = this;
      if (localStorage.getItem("userInfo")) {
        var dom = event.currentTarget;
        dom = dom.parentNode;
        this.isRespond = true;
        //   this.leavePid = leavePid;
        this.rootId = rootId;
        this.toCommentId = toCommentId;
        this.toCommentUserId = toCommentUserId;
        dom.appendChild(this.$refs.respondBox);
      } else {
        that
          .$confirm(
            "Please login to access the functions. To Login page?",
            "Alert",
            {
              confirmButtonText: "Yes",
              cancelButtonText: "No",
              type: "warning",
            }
          )
          .then(() => {
            localStorage.setItem("logUrl", that.$route.fullPath);
            that.$router.push({ path: "/Login?login=1" });
          })
          .catch(() => {});
      }
    },
    removeRespond: function () {
      this.isRespond = false;
      this.rootId = -1;
      this.toCommentId = -1;
      this.toCommentUserId = -1;
      this.$refs.tmsgBox.insertBefore(
        this.$refs.respondBox,
        this.$refs.listDom
      );
    },
    showCommentList: function (initData) {
      var that = this;
      that.aid =
        that.$route.query.aid == undefined
          ? 1
          : parseInt(that.$route.query.aid);
      that.queryParams.articleId = that.aid;
      var token = getToken();
      if (token) {
        that.haslogin = true;
      } else {
        that.haslogin = false;
      }

      if (that.$route.name == "DetailArticle") {
        that.type = 0;
        getArticleComment(that.queryParams).then((response) => {
          that.setData(initData, response);
        });
      } else {
        //其他评论
        if (that.$route.name == "FriendsLink") {
          that.type = 1;
          getLinkComment(that.queryParams).then((response) => {
            that.setData(initData, response);
          });
        }
      }
    },
    addMoreFun: function () {
      this.queryParams.pageNum++;
      this.showCommentList(false);
    },
    routeChange: function () {
      var that = this;
      this.queryParams.pageNum = 1;
      this.showCommentList(true);
    },
  },
  components: {},
  watch: {},
  created() {
    // console.log(this.$route);
    var that = this;
    that.routeChange();
  },
  mounted() {},
};
</script>

<style>
.tmsgBox {
  position: relative;
  background: #fff;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 5px;
}
.tmsg-respond h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}
.tmsg-respond h3 small {
  font-size: smaller;
  cursor: pointer;
}
.tmsg-respond textarea {
  background: #f4f6f7;
  height: 100px;
  margin-bottom: 10px;
}
.OwO {
  position: relative;
  z-index: 1;
}
.OwO .OwO-logo {
  position: relative;
  border-radius: 4px;
  color: #444;
  display: inline-block;
  background: #fff;
  border: 1px solid #ddd;
  font-size: 13px;
  padding: 0 6px;
  cursor: pointer;
  height: 30px;
  box-sizing: border-box;
  z-index: 2;
  line-height: 30px;
}
.OwO .OwO-logo:hover {
  animation: a 5s infinite ease-in-out;
  -webkit-animation: a 5s infinite ease-in-out;
}
.OwO .OwO-body {
  position: absolute;
  background: #fff;
  border: 1px solid #ddd;
  z-index: 1;
  top: 29px;
  border-radius: 0 4px 4px 4px;
  display: none;
}
.OwO-open .OwO-body {
  display: block;
}
.OwO-open .OwO-logo {
  border-radius: 4px 4px 0 0;
  border-bottom: none;
}
.OwO-open .OwO-logo:hover {
  animation: none;
  -webkit-animation: none;
}
.OwO .OwO-items {
  max-height: 197px;
  overflow: scroll;
  font-size: 0;
  padding: 10px;
  z-index: 1;
}
.OwO .OwO-items .OwO-item {
  background: #f7f7f7;
  padding: 5px 10px;
  border-radius: 5px;
  display: inline-block;
  margin: 0 10px 12px 0;
  transition: 0.3s;
  line-height: 19px;
  font-size: 20px;
  cursor: pointer;
}
.OwO .OwO-items .OwO-item:hover {
  background: #eee;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2),
    0 1px 5px 0 rgba(0, 0, 0, 0.12);
  animation: a 5s infinite ease-in-out;
  -webkit-animation: a 5s infinite ease-in-out;
}
.OwO .OwO-body .OwO-bar {
  width: 100%;
  height: 30px;
  border-top: 1px solid #ddd;
  background: #fff;
  border-radius: 0 0 4px 4px;
  color: #444;
}
.OwO .OwO-body .OwO-bar .OwO-packages li {
  display: inline-block;
  line-height: 30px;
  font-size: 14px;
  padding: 0 10px;
  cursor: pointer;
  margin-right: 3px;
  text-align: center;
}
.OwO .OwO-body .OwO-bar .OwO-packages li:first-of-type {
  border-radius: 0 0 0 3px;
}
@-webkit-keyframes a {
  2% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  4% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  6% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg);
  }
  8% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg);
  }
  10% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg);
  }
  12% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  14% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg);
  }
  16% {
    -webkit-transform: translateY(-0.5px) rotate(-1.5deg);
    transform: translateY(-0.5px) rotate(-1.5deg);
  }
  18% {
    -webkit-transform: translateY(0.5px) rotate(-1.5deg);
    transform: translateY(0.5px) rotate(-1.5deg);
  }
  20% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  22% {
    -webkit-transform: translateY(0.5px) rotate(-1.5deg);
    transform: translateY(0.5px) rotate(-1.5deg);
  }
  24% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  26% {
    -webkit-transform: translateY(0.5px) rotate(0.5deg);
    transform: translateY(0.5px) rotate(0.5deg);
  }
  28% {
    -webkit-transform: translateY(0.5px) rotate(1.5deg);
    transform: translateY(0.5px) rotate(1.5deg);
  }
  30% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  32%,
  34% {
    -webkit-transform: translateY(1.5px) rotate(-0.5deg);
    transform: translateY(1.5px) rotate(-0.5deg);
  }
  36% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  38% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg);
  }
  40% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  42% {
    -webkit-transform: translateY(2.5px) rotate(-1.5deg);
    transform: translateY(2.5px) rotate(-1.5deg);
  }
  44% {
    -webkit-transform: translateY(1.5px) rotate(0.5deg);
    transform: translateY(1.5px) rotate(0.5deg);
  }
  46% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  48% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  50% {
    -webkit-transform: translateY(0.5px) rotate(0.5deg);
    transform: translateY(0.5px) rotate(0.5deg);
  }
  52% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  54% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg);
  }
  56% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  58% {
    -webkit-transform: translateY(0.5px) rotate(2.5deg);
    transform: translateY(0.5px) rotate(2.5deg);
  }
  60% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  62% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  64% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  66% {
    -webkit-transform: translateY(1.5px) rotate(-0.5deg);
    transform: translateY(1.5px) rotate(-0.5deg);
  }
  68% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  70% {
    -webkit-transform: translateY(1.5px) rotate(0.5deg);
    transform: translateY(1.5px) rotate(0.5deg);
  }
  72% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg);
  }
  74% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  76% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  78% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  80% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  82% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  84% {
    -webkit-transform: translateY(1.5px) rotate(2.5deg);
    transform: translateY(1.5px) rotate(2.5deg);
  }
  86% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg);
  }
  88% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  90% {
    -webkit-transform: translateY(2.5px) rotate(-0.5deg);
    transform: translateY(2.5px) rotate(-0.5deg);
  }
  92% {
    -webkit-transform: translateY(0.5px) rotate(-0.5deg);
    transform: translateY(0.5px) rotate(-0.5deg);
  }
  94% {
    -webkit-transform: translateY(2.5px) rotate(0.5deg);
    transform: translateY(2.5px) rotate(0.5deg);
  }
  96% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  98% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  0%,
  to {
    -webkit-transform: translate(0) rotate(0deg);
    transform: translate(0) rotate(0deg);
  }
}
@keyframes a {
  2% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  4% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  6% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg);
  }
  8% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg);
  }
  10% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg);
  }
  12% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  14% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg);
  }
  16% {
    -webkit-transform: translateY(-0.5px) rotate(-1.5deg);
    transform: translateY(-0.5px) rotate(-1.5deg);
  }
  18% {
    -webkit-transform: translateY(0.5px) rotate(-1.5deg);
    transform: translateY(0.5px) rotate(-1.5deg);
  }
  20% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  22% {
    -webkit-transform: translateY(0.5px) rotate(-1.5deg);
    transform: translateY(0.5px) rotate(-1.5deg);
  }
  24% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  26% {
    -webkit-transform: translateY(0.5px) rotate(0.5deg);
    transform: translateY(0.5px) rotate(0.5deg);
  }
  28% {
    -webkit-transform: translateY(0.5px) rotate(1.5deg);
    transform: translateY(0.5px) rotate(1.5deg);
  }
  30% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  32%,
  34% {
    -webkit-transform: translateY(1.5px) rotate(-0.5deg);
    transform: translateY(1.5px) rotate(-0.5deg);
  }
  36% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  38% {
    -webkit-transform: translateY(1.5px) rotate(-1.5deg);
    transform: translateY(1.5px) rotate(-1.5deg);
  }
  40% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  42% {
    -webkit-transform: translateY(2.5px) rotate(-1.5deg);
    transform: translateY(2.5px) rotate(-1.5deg);
  }
  44% {
    -webkit-transform: translateY(1.5px) rotate(0.5deg);
    transform: translateY(1.5px) rotate(0.5deg);
  }
  46% {
    -webkit-transform: translateY(-1.5px) rotate(2.5deg);
    transform: translateY(-1.5px) rotate(2.5deg);
  }
  48% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  50% {
    -webkit-transform: translateY(0.5px) rotate(0.5deg);
    transform: translateY(0.5px) rotate(0.5deg);
  }
  52% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  54% {
    -webkit-transform: translateY(-1.5px) rotate(1.5deg);
    transform: translateY(-1.5px) rotate(1.5deg);
  }
  56% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  58% {
    -webkit-transform: translateY(0.5px) rotate(2.5deg);
    transform: translateY(0.5px) rotate(2.5deg);
  }
  60% {
    -webkit-transform: translateY(2.5px) rotate(2.5deg);
    transform: translateY(2.5px) rotate(2.5deg);
  }
  62% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  64% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  66% {
    -webkit-transform: translateY(1.5px) rotate(-0.5deg);
    transform: translateY(1.5px) rotate(-0.5deg);
  }
  68% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  70% {
    -webkit-transform: translateY(1.5px) rotate(0.5deg);
    transform: translateY(1.5px) rotate(0.5deg);
  }
  72% {
    -webkit-transform: translateY(2.5px) rotate(1.5deg);
    transform: translateY(2.5px) rotate(1.5deg);
  }
  74% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  76% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  78% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  80% {
    -webkit-transform: translateY(1.5px) rotate(1.5deg);
    transform: translateY(1.5px) rotate(1.5deg);
  }
  82% {
    -webkit-transform: translateY(-0.5px) rotate(0.5deg);
    transform: translateY(-0.5px) rotate(0.5deg);
  }
  84% {
    -webkit-transform: translateY(1.5px) rotate(2.5deg);
    transform: translateY(1.5px) rotate(2.5deg);
  }
  86% {
    -webkit-transform: translateY(-1.5px) rotate(-1.5deg);
    transform: translateY(-1.5px) rotate(-1.5deg);
  }
  88% {
    -webkit-transform: translateY(-0.5px) rotate(2.5deg);
    transform: translateY(-0.5px) rotate(2.5deg);
  }
  90% {
    -webkit-transform: translateY(2.5px) rotate(-0.5deg);
    transform: translateY(2.5px) rotate(-0.5deg);
  }
  92% {
    -webkit-transform: translateY(0.5px) rotate(-0.5deg);
    transform: translateY(0.5px) rotate(-0.5deg);
  }
  94% {
    -webkit-transform: translateY(2.5px) rotate(0.5deg);
    transform: translateY(2.5px) rotate(0.5deg);
  }
  96% {
    -webkit-transform: translateY(-0.5px) rotate(1.5deg);
    transform: translateY(-0.5px) rotate(1.5deg);
  }
  98% {
    -webkit-transform: translateY(-1.5px) rotate(-0.5deg);
    transform: translateY(-1.5px) rotate(-0.5deg);
  }
  0%,
  to {
    -webkit-transform: translate(0) rotate(0deg);
    transform: translate(0) rotate(0deg);
  }
}

.tmsg-r-info {
  margin: 10px 0;
}
.tmsg-r-info input {
  height: 30px;
  border-radius: 4px;
  background: #f4f6f7;
}
.tmsg-r-info .info-submit {
  margin: 10px 0;
  text-align: center;
}
.tmsg-r-info .info-submit p,
.tmsg-commentshow h1 {
  /*background: #97dffd;*/
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
  /*transition: all .3s ease-in-out;*/
  height: 30px;
  line-height: 30px;
  text-align: center;
}
/*.tmsg-r-info .info-submit p:hover{
    background: #47456d;
}*/

.tmsg-comments .tmsg-comments-tip {
  display: block;
  border-left: 2px solid #363d4c;
  padding: 0 10px;
  margin: 40px 0;
  font-size: 20px;
}
.tmsg-commentlist {
  margin-bottom: 20px;
}
.tmsg-commentshow > .tmsg-commentlist {
  border-bottom: 1px solid #e5eaed;
}
.tmsg-c-item {
  border-top: 1px solid #e5eaed;
}
.tmsg-c-item article {
  margin: 20px 0;
}
.tmsg-c-item article header {
  margin-bottom: 10px;
}
.tmsg-c-item article header img {
  width: 65px;
  height: 65px;
  border-radius: 50%;
  float: left;
  transition: all 0.4s ease-in-out;
  -webkit-transition: all 0.4s ease-in-out;
  margin-right: 15px;
  object-fit: cover;
}
.tmsg-c-item article header img:hover {
  transform: rotate(360deg);
  -webkit-transform: rotate(360deg);
}
.tmsg-c-item article header .i-name {
  font-size: 14px;
  margin: 5px 8px 7px 0;
  color: #444;
  font-weight: bold;
  display: inline-block;
}
.tmsg-c-item article header .i-class {
  display: inline-block;
  margin-left: 10px;
  background: #dff0d8;
  color: #3c763d;
  border-radius: 5px;
  padding: 3px 6px;
  font-size: 12px;
  font-weight: 400;
}
.tmsg-c-item article header .i-time {
  color: #aaa;
  font-size: 12px;
}
.tmsg-c-item article section {
  margin-left: 80px;
}
.tmsg-c-item article section p img {
  vertical-align: middle;
}
.tmsg-c-item article section .tmsg-replay {
  margin: 10px 0;
  font-size: 12px;
  color: #64609e;
  cursor: pointer;
}
</style>
