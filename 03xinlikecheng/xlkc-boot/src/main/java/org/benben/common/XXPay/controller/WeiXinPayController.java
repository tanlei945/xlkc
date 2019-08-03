package org.benben.common.XXPay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.benben.common.XXPay.PayCommonUtil;
import org.benben.common.util.GetOrder;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/weixin")
@Api(tags = {"通用接口"})
public class WeiXinPayController {
    private static String wxnotify = "http://xlkc.aipython.top/xlkc-boot/api/v1/WX/callBack";

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IBooksService booksService;

    /**
     * @param totalAmount    支付金额
     * @param description    描述
     * @param openId         微信公众号openId   （可以前端传code,然后后台再通过微信对应接口换取openId）
     * @param request -
     * @return -
     */
//    @PostMapping("/weixinPay")
//    @ApiOperation(value = "微信支付接口", tags = {"通用接口"} , notes = "微信支付接口")
    public SortedMap<String, Object> ToPay( @RequestParam String openId, @RequestParam String orderId, @RequestParam Integer payType, HttpServletRequest request) {
//        String sym = request.getRequestURL().toString().split("/api/")[0];
        String sym = "";

        // 回调地址
        String notifyUrl = sym + wxnotify;
        // 自定义参数
        Long userId = 100L; //对应用户id自己修改
        JSONObject jsAtt = new JSONObject();
        jsAtt.put("uid", userId);
        String attach = jsAtt.toJSONString();

        //通过orderId 得到 商品的信息
        Order order = orderService.getById(orderId);
        String orderNumber = GetOrder.getOrderIdByTime("xlkcxlkc");
        order.setOrdernumber(orderNumber);
        orderService.updateById(order);
        // 订单号
        String tradeNo = orderNumber;
        //通过订单得到商品的详情
        Books book = booksService.getById(order.getBookId());
        BigDecimal b = new BigDecimal(100);
        BigDecimal totalAmount = order.getTotalprice();
        String description = book.getName();
        // 返回预支付参数
        return PayCommonUtil.WxPublicPay(tradeNo, totalAmount, description, attach, openId, notifyUrl, request);
    }

    /**
     * 支付回调地址
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/json/money/wxpay/succ",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String wxpaySucc(HttpServletRequest request) throws IOException {
        System.out.println("微信支付回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = null;
        try {
            params = PayCommonUtil.doXMLParse(resultxml);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        outSteam.close();
        inStream.close();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return "fail";
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------

            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            // 取出用户id
            String attach = params.get("attach");
            JSONObject jsonObject = JSON.parseObject(attach);
            Long userId = Long.parseLong(jsonObject.get("uid").toString());

            //更新
            //updateUserPay(userId, String.valueOf(v));

            // 处理业务完毕
            // ------------------------------
            return "success";
        }
    }

    @Autowired
	WxPayService wxPayService;
    @GetMapping("/wxnotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            // 结果正确
            String orderId = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            String s = BaseWxPayResult.feeToYuan(result.getTotalFee());
            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            //log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

}
