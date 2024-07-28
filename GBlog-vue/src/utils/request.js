import axios from "axios";
import { Notification, MessageBox, Message } from "element-ui";
import router from "@/router";
import store from "../store";
import { getToken } from "@/utils/auth";
import errorCode from "@/utils/errorCode";

axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";

const service = axios.create({
  baseURL: store.state.baseURL,
  timeout: 10000,
});
service.interceptors.request.use(
  (config) => {
    const isToken = (config.headers || {}).isToken === false;
    if (getToken() && !isToken) {
      config.headers["token"] = getToken();
    }
    if (config.method === "get" && config.params) {
      let url = config.url + "?";
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName];
        var part = encodeURIComponent(propName) + "=";
        if (value !== null && typeof value !== "undefined") {
          if (typeof value === "object") {
            for (const key of Object.keys(value)) {
              if (value[key] !== null && typeof value[key] !== "undefined") {
                const params = propName + "[" + key + "]";
                const subPart = encodeURIComponent(params) + "=";
                url += subPart + encodeURIComponent(value[key]) + "&";
              }
            }
          } else {
            url += part + encodeURIComponent(value) + "&";
          }
        }
      }
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => {
    console.log(error);
    Promise.reject(error);
  }
);

service.interceptors.response.use(
  (res) => {
    const code = res.data.code || 200;
    const msg = errorCode[code] || res.data.msg || errorCode["default"];
    if (code === 401) {
      MessageBox.confirm("Please login to view", "OK", {
        confirmButtonText: "Login Again",
        type: "warning",
      })
        .then(() => {
          localStorage.setItem("logUrl", router.currentRoute.fullPath);
          router.push({
            path: "/Login?login=1",
          });
        })
        .catch(() => {});
      return Promise.reject("Session expired. Please login again");
    } else if (code === 500) {
      Message({
        message: msg,
        type: "error",
      });
      return Promise.reject(new Error(msg));
    } else if (code !== 200) {
      Notification.error({
        title: msg,
      });
      return Promise.reject("error");
    } else {
      if (res.data.data && res.data.data.total) {
        res.data.data.total = parseInt(res.data.data.total);
      }
      return res.data.data;
    }
  },
  (error) => {
    console.log("err" + error);
    let { message } = error;
    if (message === "Network Error") {
      message = "Network Error";
    } else if (message.includes("timeout")) {
      message = "Timeout";
    } else if (message.includes("failed with status code 500")) {
      message = "Server Error";
    } else if (message.includes("400")) {
      message = "Parameter error";
    } else if (message.includes("405")) {
      message = "REQUEST_METHOD error";
    } else if (message.includes("401")) {
      message = "Unauthorized, please login again";
    }
    Message({
      message: message,
      type: "error",
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);

export default service;
