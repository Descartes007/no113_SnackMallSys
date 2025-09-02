<template>
  <!--底部-->
  <div class="mall-footer">
    <div class="container">
      <div class="footer-service">
        <ul class="list-service clearfix">
          <li><i class="el-icon-bell"></i>正规渠道</li>
          <li><i class="el-icon-time"></i>正品保障</li>
          <li><i class="el-icon-position"></i>15天免费换货</li>
          <li><i class="el-icon-present"></i>满99元包邮</li>
          <li><i class="el-icon-location-outline"></i>退换无忧</li>
        </ul>
      </div>

      <!-- 底部导航链接 -->
      <div class="footer-links">
        <div class="footer-links-group">
          <h4>关于我们</h4>
          <ul>
            <li>公司简介</li>
            <li>服务协议</li>
            <li>隐私政策</li>
          </ul>
        </div>
        <div class="footer-links-group">
          <h4>帮助中心</h4>
          <ul>
            <li>购物指南</li>
            <li>支付方式</li>
            <li>配送说明</li>
          </ul>
        </div>
        <div class="footer-links-group">
          <h4>售后服务</h4>
          <ul>
            <li>退换货政策</li>
            <li>退款说明</li>
            <li>投诉与建议</li>
          </ul>
        </div>
        <div class="footer-links-group">
          <h4>联系我们</h4>
          <ul>
            <li>客服电话：400-123-4567</li>
            <li>邮箱：service@arttoy.com</li>
            <li>工作时间：9:00-18:00</li>
          </ul>
        </div>
      </div>

      <div class="footer-copyright">
        <div
          class="clearfix"
          style="
            width: 80%;
            margin: 0 auto;
            padding: 20px 0;
            text-align: center;
          "
        >
          <div class="copyright-main">
            <div class="company-name">零食商城系统</div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MallFooter",
};
</script>

<style>
.mall-footer {
  background: #fff;
  height: auto;
  padding-bottom: 20px;
  border-top: 1px solid #e0e0e0;
}

.mall-footer .container {
  width: 80%;
  margin: 0 auto;
}

.mall-footer .container .footer-service {
  padding: 27px 0 !important;
  min-width: 754px !important;
  border-bottom: 1px solid #e0e0e0 !important;
}

.mall-footer .container .footer-service .list-service {
  margin: 0;
  padding: 0;
  list-style-type: none;
}

.mall-footer .list-service li {
  float: left;
  width: 19.8%;
  height: 25px;
  border-left: 1px solid #e0e0e0;
  font-size: 16px;
  line-height: 25px;
  text-align: center;
  cursor: pointer;
}

.mall-footer .list-service li:first-child {
  border-left: none;
}

.mall-footer .list-service li:hover {
  color: #ff7634;
}

.mall-footer .list-service i {
  font-size: 20px;
  padding-right: 4px;
}

/* 底部链接样式 */
.footer-links {
  display: flex;
  justify-content: space-between;
  padding: 30px 0;
  border-bottom: 1px solid #e0e0e0;
}

.footer-links-group {
  flex: 1;
  padding: 0 15px;
}

.footer-links-group h4 {
  font-size: 16px;
  margin-bottom: 15px;
  color: #424242;
}

.footer-links-group ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links-group li {
  font-size: 14px;
  line-height: 30px;
  color: #757575;
  cursor: pointer;
}

.footer-links-group li:hover {
  color: #ff7634;
}

/* 版权信息样式 */
.footer-copyright {
  margin-top: 20px;
  color: #757575;
}

.company-name {
  font-size: 18px;
  font-weight: bold;
  color: #424242;
  margin-bottom: 10px;
}

.copyright-info {
  font-size: 14px;
  margin-bottom: 5px;
}

.icp-info {
  font-size: 14px;
}

.clearfix:after {
  content: "";
  display: block;
  height: 0;
  clear: both;
  visibility: hidden;
}

.clearfix {
  zoom: 1; /* IE6-7 hack 清除浮动 */
}
</style>
