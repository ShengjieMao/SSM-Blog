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
          <sg-articlelist></sg-articlelist>
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
import articlelist from "../components/articlelist.vue";
import rightlist from "../components/rightlist.vue";
export default {
  name: "Share",
  data() {
    return {};
  },
  methods: {},
  components: {
    "sg-nav": header,
    "sg-articlelist": articlelist,
    "sg-rightlist": rightlist,
  },

  created() {},
};
</script>

<style></style>
