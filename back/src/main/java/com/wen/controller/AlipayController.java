package com.wen.controller;

import com.alipay.api.AlipayApiException;
import com.wen.entity.Order;
import com.wen.service.AlipayService;
import com.wen.service.OrderService;
import com.wen.service.ShoppingCartService;
import com.wen.util.general.CommonResult;
import com.wen.util.general.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 
 */
@Slf4j
@Controller
@CrossOrigin
@RequestMapping("/alipay")
public class AlipayController {
    private static final String VIP = "Vip";
    private static final String OPEN_SUCCESS = "开通成功";
    private static final String WAIT_SEND = "待发货";
    private static final String CART_ID = "cartId";
    private static final String OUT_TRADE_NO = "out_trade_no";
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    private static final String TRADE_STATUS = "trade_status";
    private static final String TRADE_TIME = "gmt_payment";
    private static final String TRADE_NAME = "subject";
    private static final String TRADE_AMOUNT = "buyer_pay_amount";

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String hostAddress;

    public AlipayController() {
        this.hostAddress = PropertiesUtil.getCallback();
    }

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @ResponseBody
    @PostMapping(value = "/create", produces = "text/html;charset=utf-8")
    public String create(@RequestParam("orderNo") String orderNo,
                         @RequestParam("orderName") String orderName,
                         @RequestParam("payPrice") String payPrice) {
        return alipayService.create(orderNo, orderName, payPrice);
    }

    @ResponseBody
    @PostMapping(value = "/vip", produces = "text/html;charset=utf-8")
    public String create(@RequestParam("orderNo") String orderNo,
                         @RequestParam("orderName") String orderName,
                         @RequestParam("payPrice") String payPrice,
                         @RequestParam("serialNumber") String serialNumber) {
        //开通vip的序列号暂时存入redis中
        redisTemplate.opsForValue().set(orderNo, serialNumber, 2, TimeUnit.HOURS);
        return alipayService.create(orderNo, orderName, payPrice);
    }


    @ResponseBody
    @RequestMapping(value = "/refund")
    public CommonResult refund(@RequestParam("orderNo") String orderNo, @RequestParam("payPrice") String payPrice) {
        try {
            alipayService.refund(orderNo, payPrice);
        } catch (AlipayApiException e) {
            log.error("【支付宝支付】退款失败", e);
            return CommonResult.success("退款失败");
        }
        return CommonResult.success("退款成功");
    }

    @GetMapping(value = "/success")
    public void success(@RequestParam Map<String, String> map, HttpServletResponse response) {
        try {
            String tradeNo = map.get(OUT_TRADE_NO);
            if (tradeNo.contains(VIP)) {
                openMember(response, tradeNo);
            } else {
                updateProductStatus(response, tradeNo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/notify")
    public void payNotify(@RequestParam Map<String, String> map) {
        if (TRADE_SUCCESS.equals(map.get(TRADE_STATUS))) {
            String payTime = map.get(TRADE_TIME);
            String tradeNo = map.get(OUT_TRADE_NO);
            String tradeName = map.get(TRADE_NAME);
            String payAmount = map.get(TRADE_AMOUNT);
            log.info("[支付成功] {交易时间：{},订单号：{},订单名称：{},交易金额：{}}", payTime, tradeNo, tradeName, payAmount);
        }
    }

    /**
     * 支付成功，更新商品状态为待发货
     *
     * @param response HTTP响应
     * @param tradeNo  订单编号
     * @throws IOException IO异常信息
     */
    private void updateProductStatus(HttpServletResponse response, String tradeNo) throws IOException {
        String tradeNos = redisTemplate.opsForValue().get(tradeNo);
        if (StringUtils.isNotBlank(tradeNos)) {
            redisTemplate.delete(tradeNo);
            String[] ordersNo = tradeNos.split(",");
            for (String orderNo : ordersNo) {
                Integer orderId = orderService.selectIdByKey(orderNo);
                Order order = new Order();
                order.setOrderId(orderId);
                order.setOrderState(WAIT_SEND);
                orderService.updateById(order);
            }
        }
        String cartIdInfos = redisTemplate.opsForValue().get(CART_ID + tradeNo);
        if (StringUtils.isNotBlank(cartIdInfos)) {
            redisTemplate.delete(cartIdInfos);
            String[] cartIds = cartIdInfos.split(",");
            for (String cartId : cartIds) {
                if (StringUtils.isNotBlank(cartId)) {
                    shoppingCartService.deleteById(Integer.parseInt(cartId));
                }
            }
        }
        response.sendRedirect("http://" + hostAddress + "/#/myOrder");
    }

    /**
     * 开通会员
     *
     * @param response HTTP响应
     * @param tradeNo  订单编号
     * @throws IOException IO异常信息
     */
    private void openMember(HttpServletResponse response, String tradeNo) throws IOException {
        Integer orderId = orderService.selectIdByKey(tradeNo);
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderState(OPEN_SUCCESS);
        String serialNumber = redisTemplate.opsForValue().get(tradeNo);
        if (serialNumber != null) {
            response.sendRedirect("http://" + hostAddress + "/#/personalCenter?serialNumber=" + serialNumber);
            redisTemplate.delete(tradeNo);
        } else {
            response.sendRedirect("http://" + hostAddress + "/#/personalCenter?serialNumber=" + "ERROR");
        }
        orderService.updateById(order);
    }

    @ResponseBody
    @PostMapping(value = "/api/simulate-payment")
    public CommonResult simulatePayment(@RequestParam("orderNo") String orderNo,
                     @RequestParam("orderName") String orderName,
                     @RequestParam("payPrice") String payPrice,
                     @RequestParam("payType") String payType,
                     @RequestParam(value = "serialNumber", required = false) String serialNumber) {
        try {
            // 记录请求参数
            log.info("[模拟支付请求] 订单号: {}, 订单名称: {}, 支付金额: {}, 支付方式: {}, 序列号: {}", 
                    orderNo, orderName, payPrice, payType, serialNumber);
            
            // 如果是vip订单，保存序列号到Redis
            if (orderNo.contains(VIP) && StringUtils.isNotBlank(serialNumber)) {
                redisTemplate.opsForValue().set(orderNo, serialNumber, 2, TimeUnit.HOURS);
                log.info("[模拟支付] VIP序列号已保存到Redis, 订单号: {}, 序列号: {}", orderNo, serialNumber);
            }
            
            // 模拟支付流程，处理订单状态
            if (orderNo.contains(VIP)) {
                // 处理VIP订单
                Integer orderId = orderService.selectIdByKey(orderNo);
                if (orderId == null) {
                    log.error("[模拟支付] 找不到VIP订单, 订单号: {}", orderNo);
                    return CommonResult.error("找不到对应订单");
                }
                
                Order order = new Order();
                order.setOrderId(orderId);
                order.setOrderState(OPEN_SUCCESS);
                order.setPayType(payType); // 更新实际使用的支付方式
                boolean result = orderService.updateById(order);
                if (!result) {
                    log.error("[模拟支付] 更新VIP订单状态失败, 订单号: {}", orderNo);
                    return CommonResult.error("更新订单状态失败");
                }
                log.info("[模拟支付] VIP订单状态已更新为: {}, 订单号: {}, 支付方式: {}", OPEN_SUCCESS, orderNo, payType);
            } else {
                // 处理普通订单
                String tradeNos = redisTemplate.opsForValue().get(orderNo);
                if (StringUtils.isNotBlank(tradeNos)) {
                    log.info("[模拟支付] 处理购物车订单组, 订单号: {}, 关联订单: {}", orderNo, tradeNos);
                    String[] ordersNo = tradeNos.split(",");
                    for (String orderNoItem : ordersNo) {
                        Integer orderId = orderService.selectIdByKey(orderNoItem);
                        if (orderId == null) {
                            log.warn("[模拟支付] 找不到关联订单, 订单号: {}", orderNoItem);
                            continue;
                        }
                        
                        Order order = new Order();
                        order.setOrderId(orderId);
                        order.setOrderState(WAIT_SEND);
                        order.setPayType(payType); // 更新实际使用的支付方式
                        boolean result = orderService.updateById(order);
                        if (!result) {
                            log.warn("[模拟支付] 更新关联订单状态失败, 订单号: {}", orderNoItem);
                        } else {
                            log.info("[模拟支付] 关联订单状态已更新为: {}, 订单号: {}, 支付方式: {}", WAIT_SEND, orderNoItem, payType);
                        }
                    }
                } else {
                    // 单个订单的情况
                    log.info("[模拟支付] 处理单个订单, 订单号: {}", orderNo);
                    Integer orderId = orderService.selectIdByKey(orderNo);
                    if (orderId == null) {
                        log.error("[模拟支付] 找不到订单, 订单号: {}", orderNo);
                        return CommonResult.error("找不到对应订单");
                    }
                    
                    Order order = new Order();
                    order.setOrderId(orderId);
                    order.setOrderState(WAIT_SEND);
                    order.setPayType(payType); // 更新实际使用的支付方式
                    boolean result = orderService.updateById(order);
                    if (!result) {
                        log.error("[模拟支付] 更新订单状态失败, 订单号: {}", orderNo);
                        return CommonResult.error("更新订单状态失败");
                    }
                    log.info("[模拟支付] 订单状态已更新为: {}, 订单号: {}, 支付方式: {}", WAIT_SEND, orderNo, payType);
                }
                
                // 清理购物车
                String cartIdInfos = redisTemplate.opsForValue().get(CART_ID + orderNo);
                if (StringUtils.isNotBlank(cartIdInfos)) {
                    log.info("[模拟支付] 清理购物车, 订单号: {}, 购物车ID: {}", orderNo, cartIdInfos);
                    String[] cartIds = cartIdInfos.split(",");
                    for (String cartId : cartIds) {
                        if (StringUtils.isNotBlank(cartId)) {
                            boolean result = shoppingCartService.deleteById(Integer.parseInt(cartId));
                            if (!result) {
                                log.warn("[模拟支付] 清理购物车项失败, 购物车ID: {}", cartId);
                            }
                        }
                    }
                }
            }
            
            // 记录支付信息
            log.info("[模拟支付成功] 订单号: {}, 订单名称: {}, 交易金额: {}, 支付方式: {}", 
                    orderNo, orderName, payPrice, payType);
                    
            return CommonResult.success("支付成功");
        } catch (Exception e) {
            log.error("[模拟支付] 处理失败, 订单号: {}, 异常信息: {}", orderNo, e.getMessage(), e);
            return CommonResult.error("支付处理失败: " + e.getMessage());
        }
    }
}
