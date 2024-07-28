<template>
  <div class="tRewardBox tcommonBox">
    <header>
      <h1>
        <a href="#/DetailShare" target="_blank"> Tip </a>
      </h1>
    </header>
    <section>
      <div>
        <img
          src="static/img/coffee.jpg"
          alt=""
          style="max-width: 20%"
          class="coffee"
        />
      </div>
      <h1>Tipping Instruction:</h1>
      <p>
        If you find this blog helpful, you can leave tip to the author to
        encourage them.
      </p>
      <hr />
      <h1>Tipping methods:</h1>
      <el-row :gutter="30">
        <el-col :span="12" class="donate-item">
          <div class="donate-tip">
            <img
              :src="
                rewardData.wechat_image
                  ? rewardData.wechat_image
                  : 'static/img/wx_pay.png'
              "
              :onerror="$store.state.errorImg"
            />
            <span>Wechat</span>
          </div>
        </el-col>
        <!-- <el-col :span="12" class="donate-item">
          <div class="donate-tip">
            <img
              :src="
                rewardData.alipay_image
                  ? rewardData.alipay_image
                  : 'static/img/ali_pay.jpg'
              "
              :onerror="$store.state.errorImg"
            />
            <span style="padding-top: 14px">Alipay</span>
          </div>
        </el-col> -->
      </el-row>
      <h1>@Tipping records:</h1>
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          prop="payTime"
          label="date"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="name"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="money"
          label="money"
          align="center"
        ></el-table-column>
      </el-table>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rewardData: "",
      tableData: [
        {
          payTime: "2023-10-23",
          name: "Test01",
          money: "10",
        },
        {
          payTime: "2023-10-28",
          name: "Test02",
          money: "10",
        },
        {
          payTime: "2023-11-03",
          name: "Test03",
          money: "10",
        },
      ],
    };
  },
  methods: {
    showInitDate: function (date, full) {
      return initDate(date, full);
    },
  },
  components: {},
  created() {
    var that = this;
  },
};
</script>

<style>
.tRewardBox section {
  padding-bottom: 20px;
}
.tRewardBox section h1 {
  margin: 10px 0;
  font-size: 25px;
  font-weight: 700;
  /*text-align: center;*/
  line-height: 30px;
}
.tRewardBox section p {
  margin: 10px 0;
  line-height: 24px;
}
.tRewardBox section hr {
  background: #ccc;
  margin-bottom: 30px;
}

.tRewardBox .donate-item {
  text-align: right;
  color: #44b549;
}
.tRewardBox .donate-item:last-child {
  text-align: left;
  color: #00a0e9;
}
.tRewardBox .donate-item img {
  width: 100%;
  display: block;
  height: auto;
}
.tRewardBox .donate-item div {
  display: inline-block;
  width: 150px;
  padding: 0 6px;
  text-align: center;
  box-sizing: border-box;
}
.tRewardBox .donate-item div span {
  display: inline-block;
  width: 100%;
  margin: 10px 0;
  text-align: center;
}
.tRewardBox .el-table__body-wrapper {
  overflow: hidden;
}
.el-table--enable-row-hover .el-table__body tr:hover > td {
  background: transparent;
}
</style>
