import Vue from "vue";
import Vuex from "vuex";
// import * as getters from './getters.js'

Vue.use(Vuex);

export const state = {
  loading: false,
  themeObj: 0,
  keywords: "",
  errorImg:
    'this.onerror=null;this.src="' + require("../../static/img/tou.jpg") + '"',
  baseURL: "http://localhost:8080/",
};

export default new Vuex.Store({
  state,
});
