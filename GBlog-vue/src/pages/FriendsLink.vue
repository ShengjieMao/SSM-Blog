<template>
  <div>
    <sg-nav></sg-nav>
    <div class="container">
      <el-row :gutter="30">
        <el-col
          :sm="24"
          :md="16"
          style="transition: all 0.5s ease-out; margin-bottom: 30px"
        >
          <sg-friends></sg-friends>
          <sg-message></sg-message>
        </el-col>
        <el-col :sm="24" :md="8">
          <sg-rightlist></sg-rightlist>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import header from "../components/header.vue";
import friends from "../components/friends.vue";
import rightlist from "../components/rightlist.vue";
import message from "../components/message.vue";
export default {
  name: "FriendsLink",
  data() {
    return {};
  },
  methods: {},
  components: {
    "sg-nav": header,
    "sg-message": message,
    "sg-friends": friends,
    "sg-rightlist": rightlist,
  },
  created() {},
};
</script>

<style></style>
