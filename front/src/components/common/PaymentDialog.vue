<template>
  <el-dialog
    title="请选择支付方式"
    :visible.sync="dialogVisible"
    width="450px"
    :before-close="handleClose"
  >
    <div class="payment-options">
      <div
        class="payment-option"
        :class="{ selected: selectedPayment === 'alipay' }"
        @click="selectPayment('alipay')"
      >
        <div class="payment-icon">
          <img src="@/assets/img/alipay.png" alt="支付宝" class="payment-img" />
        </div>
        <div class="payment-name">支付宝支付</div>
      </div>
      <div
        class="payment-option"
        :class="{ selected: selectedPayment === 'wechat' }"
        @click="selectPayment('wechat')"
      >
        <div class="payment-icon">
          <img src="@/assets/img/wechat.png" alt="微信" class="payment-img" />
        </div>
        <div class="payment-name">微信支付</div>
      </div>
    </div>
    <div class="payment-amount">
      <span class="amount-label">支付金额：</span>
      <span class="payment-amount-value">¥ {{ payPrice }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button
        type="primary"
        @click="confirmPayment"
        :disabled="!selectedPayment"
        >确认支付</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "PaymentDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    orderNo: {
      type: String,
      default: "",
    },
    orderName: {
      type: String,
      default: "",
    },
    payPrice: {
      type: [Number, String],
      default: 0,
    },
    serialNumber: {
      type: String,
      default: "",
    },
    isVip: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dialogVisible: false,
      selectedPayment: "alipay", // 默认选择支付宝
      paymentLoading: false,
    };
  },
  watch: {
    visible(val) {
      this.dialogVisible = val;
    },
    dialogVisible(val) {
      if (!val) {
        this.$emit("update:visible", false);
      }
    },
  },
  methods: {
    handleClose() {
      this.dialogVisible = false;
    },
    selectPayment(type) {
      this.selectedPayment = type;
    },
    confirmPayment() {
      // 显示加载
      this.paymentLoading = true;
      const loading = this.$loading({
        lock: true,
        text: "处理支付请求中...",
        background: "rgba(255,255,255,0.1)",
      });

      let url = "";
      let params = {
        orderNo: this.orderNo,
        orderName: this.orderName,
        payPrice: this.payPrice,
        payType: this.selectedPayment === "alipay" ? "支付宝" : "微信",
      };

      // 根据不同类型的支付请求选择不同的API
      if (this.isVip) {
        url = "/alipay/vip";
        params.serialNumber = this.serialNumber;
      } else {
        url = "/alipay/create";
      }

      // 使用模拟支付，不跳转到第三方页面
      this.$http
        .post("/alipay/api/simulate-payment", this.$qs.stringify(params))
        .then((response) => {
          loading.close();
          this.paymentLoading = false;

          // 检查响应状态
          if (response.data && response.data.code === 200) {
            // 显示支付成功提示
            this.$notify({
              title: "支付成功",
              message: "您的订单已支付成功！",
              type: "success",
            });

            // 关闭弹窗
            this.dialogVisible = false;

            // 触发支付成功事件
            this.$emit("payment-success", {
              orderNo: this.orderNo,
              payType: params.payType,
            });

            // 根据支付类型跳转到相应页面
            if (this.isVip) {
              this.$router.push({
                path: "/personalCenter",
                query: { serialNumber: this.serialNumber },
              });
            } else {
              this.$router.push({ path: "/myOrder" });
            }
          } else {
            // 支付失败处理
            this.$notify({
              title: "支付失败",
              message: response.data.message || "支付处理失败，请稍后重试",
              type: "error",
            });
          }
        })
        .catch((err) => {
          loading.close();
          this.paymentLoading = false;
          this.$notify({
            title: "支付失败",
            message: "支付处理失败，请稍后重试",
            type: "error",
          });
          console.error("支付处理错误:", err);
        });
    },
  },
};
</script>

<style scoped>
.payment-options {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.payment-option {
  width: 180px;
  height: 60px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.payment-option:hover {
  border-color: #409eff;
}

.payment-option.selected {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.payment-icon {
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 10px;
}

.payment-img {
  width: 30px;
  height: 30px;
  object-fit: contain;
}

.payment-name {
  font-size: 14px;
}

.payment-amount {
  text-align: center;
  margin-bottom: 20px;
  font-size: 16px;
}

.payment-amount-value {
  color: #ff6700;
  font-size: 20px;
  font-weight: bold;
}

.payment-actions {
  display: flex;
  justify-content: flex-end;
}

.payment-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}

.payment-result {
  margin-top: 10px;
  font-size: 16px;
}

.success {
  color: #67c23a;
}

.error {
  color: #f56c6c;
}
</style> 