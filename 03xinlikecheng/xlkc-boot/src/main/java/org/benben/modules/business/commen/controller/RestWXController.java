package org.benben.modules.business.commen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.XXPay.PayCommonUtil;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.GetOrder;
import org.benben.common.util.HttpGetUtil;
import org.benben.common.util.MyStringUtils;
import org.benben.common.util.WchatUtils;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.commen.dto.WechatInfo;
import org.benben.modules.business.commen.dto.WechatRefund;
import org.benben.modules.business.commen.dto.WeiXinUtils;
import org.benben.modules.business.commen.service.WechatpayServiceI;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@RequestMapping("/api/v1/WX")
@RestController
@Api(tags = {"通用接口"})
@Slf4j
public class RestWXController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IOrderService orderService;

	@Autowired
	private IBooksService booksService;

	@Autowired
	private ICourseService courseService;

	@Autowired
	private WechatpayServiceI wechatpayService;

	@Autowired
	private ICoursePerInformationService coursePerInformationService;

	@Autowired
	private IUserCourseService userCourseService;

	private static String wxnotify = "http://xlkc.aipython.top/xlkc-boot/api/v1/WX/callBack";
	/**
	 * 微信退款
	 */
	@GetMapping("WXRefund")
	@ApiOperation(value = "微信退款接口",tags = {"通用接口"}, notes = "微信退款接口")
	public RestResponseBean WXRefund (@RequestParam String orderId, @RequestParam BigDecimal refundPrice) {
		WechatRefund info = new WechatRefund();
//		String orderNo = MyStringUtils.getOrderNo();
		//根据orderId得到订单金额
		Order order = orderService.getById(orderId);
		info.setOutRefundNo(orderId);
		info.setOutTradeNo(orderId);
		info.setNotifyUrl("http://xlkc.aipython.top/xlkc-boot/api/v1/WX/callBackRefund");
		info.setRefundFee(refundPrice);//退款金额
		info.setTotalFee(order.getTotalprice());//订单金额
		Map<String, Object> map = wechatpayService.refund(info);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"退款成功");
		//		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"");
	}

	@RequestMapping("/callBackRefund")
	public void callBackRefund(HttpServletRequest request) {
		String return_code = request.getParameter("return_code");

		if (return_code.equals("SUCCESS")) {
			System.out.println(return_code);
			System.out.println("退款成功");
		} else if(return_code.equals("FALl")) {
			String sign = request.getParameter("sign");
			System.out.println("签名sign = "+ sign);
			System.out.println("退款失败");
		}
		System.out.println("return_code = " + return_code);
	}


	/**
	 * 微信支付回调结果
	 */
	@PostMapping("/callBack")
	public void callBack(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {

			InputStream inputStream = request.getInputStream();
			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到xml根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			@SuppressWarnings("unchecked") List<Element> elementList = root.elements();

			// 遍历所有子节点
			for (Element e : elementList)
				map.put(e.getName(), e.getText());
			// 释放资源
			inputStream.close();
			inputStream = null;
		} catch (Exception e) {

		}
		String return_code = map.get("return_code");
		String return_msg = map.get("return_msg");
		System.out.println("return_code = " + return_code);
		System.out.println("return_msg = " + return_msg);

		if (return_code.equals("SUCCESS")) {
			//首先判断是不是已支付
			String orderNumber = map.get("out_trade_no");
			System.out.println("订单号    " + orderNumber);
			//判断是课程的还是书籍的 还有修改已报课程几个人和添加书籍几个人
			Order order = orderService.queryOrderNumber(orderNumber);
			System.out.println("查看order是否为空：" + order);
			if (order != null) {
				if (order.getState() == 1) {
					System.out.println("开始修改");
					order.setState(2);
					Books book = booksService.getById(order.getBookId());
					book.setNum(book.getNum()+1);
					if(book.getBookNum() > 0) {
						book.setBookNum(book.getBookNum() - 1);
					} else {
						System.out.println("库存量不足");
						return;
					}
					boolean b = orderService.updateById(order);
					if (b == true) {
						System.out.println("修改成功");
					}
					System.out.println("修改成功之后的ORder：  " + order);
					booksService.updateById(book);
				}
			} else if (StringUtils.isNotBlank(orderNumber)){
				CoursePerInformation coursePerInformation = coursePerInformationService.queryByOrdeNumber(orderNumber);

				String courseId = coursePerInformation.getCourseId();
				String uid = coursePerInformation.getUid();
				//修改课程的库存量和申请人数
				Course course = courseService.getById(courseId);
				if (course.getNum() > 0){
					course.setNum(course.getNum() - 1);
				} else {
					System.out.println("库存量不足");
					return;
				}
				if (course != null) {
					courseService.updateById(course);
				}
				course.setApplyNum(course.getApplyNum()+1);
				UserCourse userCourse = userCourseService.queryByCourse(uid, courseId);
				if (userCourse != null) {
					userCourse.setCourseVerify(1);
				} else {
					System.out.println("没有该课程记录");
					return;
				}
				if (userCourse != null) {
					userCourseService.updateById(userCourse);
				}

			}


			//获取订单号
//			map.get()
			System.out.println(return_code);
			System.out.println("支付成功");
		} else if(return_code.equals("FALl")) {
			String sign = request.getParameter("sign");
			System.out.println("签名sign = "+ sign);
			System.out.println("");
		}
	}

	@GetMapping("/weixinPay")
	@ApiOperation(value = "微信支付接口", tags = {"通用接口"} , notes = "微信支付接口")
	public RestResponseBean ToPay( @RequestParam java.lang.String openId, @RequestParam java.lang.String orderId, @RequestParam Integer payType, HttpServletRequest request) {
		//        String sym = request.getRequestURL().toString().split("/api/")[0];
		java.lang.String sym = "";

		// 回调地址
		java.lang.String notifyUrl = sym + wxnotify;
		// 自定义参数
		Long userId = 100L; //对应用户id自己修改
		com.alibaba.fastjson.JSONObject jsAtt = new com.alibaba.fastjson.JSONObject();
		jsAtt.put("uid", userId);
		java.lang.String attach = jsAtt.toJSONString();

		//通过orderId 得到 商品的信息
		Order order = orderService.getById(orderId);
		String orderNumber = "";
		Books book = null;
		CoursePerInformation coursePerInformation = new CoursePerInformation();
		if(order != null) {
			orderNumber = GetOrder.getOrderIdByTime("xlkcxlkc");
			order.setOrdernumber(orderNumber);
			orderService.updateById(order);
			book = booksService.getById(order.getBookId());
		}else{
			orderNumber = GetOrder.getOrderIdByTime("xlkcxlkc");
			coursePerInformation.setId(orderId);
			coursePerInformation.setOrdernumber(orderNumber);
			coursePerInformationService.updateById(coursePerInformation);
			//添加进我的课程
			CoursePerInformation information = coursePerInformationService.getById(coursePerInformation.getId());
			UserCourse userCourse = new UserCourse();
			userCourse.setUid(information.getUid());
			userCourse.setCourseId(information.getCourseId());
			userCourse.setCourseVerify(1);
			userCourse.setStatus(0);
			userCourseService.save(userCourse);

		}
		// 订单号
		java.lang.String tradeNo = orderNumber;
		//通过订单得到商品的详情
		BigDecimal b = new BigDecimal(100);
//		BigDecimal totalAmount = order.getTotalprice();
		BigDecimal totalAmount = new BigDecimal(0.01);

		String description = "";
		if (book != null ) {
			description = book.getName();
		} else if (StringUtils.isNotBlank(coursePerInformation.getId())){
			CoursePerInformation information = coursePerInformationService.getById(coursePerInformation.getId());
			Course course = courseService.getById(information.getCourseId());
			description = course.getCourseName();

		}
		// 返回预支付参数
		SortedMap<String, Object> stringObjectSortedMap = PayCommonUtil
				.WxPublicPay(tradeNo, totalAmount, description, attach, openId, notifyUrl, request);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),stringObjectSortedMap);
	}
	/**
	 *得到ticket
	 */
	@GetMapping("/getTicket")
	@ApiOperation(value = "得到ticket", tags = {"通用接口"}, notes = "得到ticket")
	public RestResponseBean getTicket() {

		System.out.println("***********gettoken start***********");
		String url = WchatUtils.GET_TOKEN_URL;
		JSONObject json = WeiXinUtils.httpRequest(url, "GET", null);
		//logger.info("返回token json：" + json.toString());
		if (json.containsKey("errcode")) {
			System.out.println("-------------gettokenerror--------------");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"获取失败") ;
		}

		String token = json.getString("access_token");
		String expires_in = json.getString("expires_in");

		// 获取jsapi_ticket
		String jsapi_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi";
		JSONObject apiJson = null;
		try {
			//System.out.println(jsapi_url);
			apiJson = WeiXinUtils.getRequestParam(jsapi_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String ticket = "";
		if(apiJson.get("errmsg").toString().equals("ok")){
			ticket = apiJson.get("ticket").toString();
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),ticket);
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),ticket);
	}


	/**
	 * 网页授权
	 */
	@GetMapping("/WXAuthorization")
	@ApiOperation(value = "微信授权接口",tags = {"通用接口"}, notes = "微信授权接口")
	public void WXAuthorization(HttpServletResponse response, HttpServletRequest request) {

		String url = "";
		try {
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WchatUtils.APP_ID
					+ "&redirect_uri=" + URLEncoder.encode(WchatUtils.ROLL_BACK_CALLBACK, "UTF-8")
					+ "&response_type=code"
					+ "&scope=snsapi_userinfo"
					+ "&state=" + "111" + "#wechat_redirect";
			log.debug("forward重定向地址{" + url + "}");
			System.out.println(url);
			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 签权回调
	 */
	@GetMapping("/redirectPage")
	public void redirectPage(HttpServletRequest request,
			HttpServletResponse response) {
		//		User user = (User) SecurityUtils.getSubject().getPrincipal();

		String code = request.getParameter("code"); //必须得


		Map<String, String> params = new HashMap<>();
		params.put("secret", WchatUtils.APP_SECRET); //微信公众号的appsecrt
		params.put("appid", WchatUtils.APP_ID);//微信公众好的appid
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		String result = HttpGetUtil.httpRequestToString("https://api.weixin.qq.com/sns/oauth2/access_token", params);
		JSONObject jsonObject = JSONObject.fromObject(result);
		String openid = jsonObject.get("openid").toString();
		System.out.println("***********openid:" + openid);

		if (StringUtils.isNotBlank(openid)) {

			//将open_id添加到用户信息当中
		/*	user.setOpenId(openid);
			userService.updateById(user);*/
			System.out.println(openid);
			try {
				response.sendRedirect("http://xlkc.aipython.top/#/author?openId=" + openid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		} else {
			try {// 反复签权；
				response.sendRedirect(WchatUtils.HOST_NAME + "/weixin/getCode");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 绑定openId
	 */
	@PostMapping("bingdingOpeid")
	@ApiOperation(value = "绑定openid接口", tags = "通用接口", notes = "绑定openid接口")
	public RestResponseBean bingdingOpenId(@RequestParam String openId) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Map<String,String> map = new HashMap<>();
		map.put("sss","ss");
		user.setOpenId(openId);
		boolean b = userService.updateById(user);

		if (b == true) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					"绑定成功");
		}else  {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"绑定失败");
		}
	}

}
