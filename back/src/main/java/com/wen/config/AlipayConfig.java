package com.wen.config;


import com.alipay.api.request.AlipayTradePagePayRequest;

import com.wen.util.general.PropertiesUtil;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
public class AlipayConfig {

    /**
     * 沙箱appId
     */
    public static final String APPID = "";

    /**
     * 请求网关  固定
     */
    public static final String URL = "";

    /**
     * 编码
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 返回格式
     */
    public static final String FORMAT = "json";

    /**
     * RSA2
     */
    public static final String SIGNTYPE = "RSA2";

    /**
     * 异步通知地址
     */
    public static final String NOTIFY_URL = "http://" + PropertiesUtil.getDomain() + "/alipay/notify";

    /**
     * 同步地址
     */
    public static final String RETURN_URL = "http://" + PropertiesUtil.getDomain() + "/alipay" +
          "/success";

    /**
     * 应用私钥 pkcs8格式
     */
    public static final String RSA_PRIVATE_KEY = "";

    /**
     * 沙箱支付宝公钥
     */
    public static final String ALIPAY_PUBLIC_KEY ="";
    private AlipayConfig() {
    }


    //多列模式的 aliPayRequest
    @Bean
    public AlipayTradePagePayRequest alipayTradePagePayRequest(){
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        return alipayRequest;
    }

}
