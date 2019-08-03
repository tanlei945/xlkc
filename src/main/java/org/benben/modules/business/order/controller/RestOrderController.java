package org.benben.modules.business.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.GetOrder;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.courseperinformation.vo.CoursePerInformationVo;
import org.benben.modules.business.courseperinformation.vo.Video;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.benben.modules.business.deliveryaddress.service.IDeliveryaddressService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderBooksVo;
import org.benben.modules.business.order.vo.Orders;
import org.benben.modules.business.user.entity.User;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/order")
@Api(tags = "订单接口")
@Slf4j
public class RestOrderController {
	@Autowired
	private IOrderService orderService;

	@Autowired
	private ICourseService courseService;

	@Autowired
	private IDeliveryaddressService deliveryaddressService;

	@Autowired
	private IBooksService booksService;

	@Autowired
	private ICoursePerInformationService coursePerInformationService;

	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 显示正在退款和退款完成的列表
	 * @description 显示正在退款和退款完成的列表接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/viewRefund
	 * @param pageNumber Integer 从第几个数据开始
	 * @param pageSize String 一页显示几条数据
	 * @return {"code": 1,"msg": "操作成功","time": "1562223502097","data": {"total": 1,"orderBooksVos": [ 	{ 	"id": "1", 	"ordernumber": "201905301615226672", 	"createTime": "2019-05-30 16:16:17", 	"name": "心理学课程", 	"bookId": "1", 	"price": 100, 	"totalprice": 600, 	"booknum": 50, 	"status": 2 	}]}}
	 * @remark 订单接口
	 * @number 99
	 **/
	@GetMapping("/viewRefund")
	@ApiOperation(value = "显示正在退款和退款完成的列表",tags = "订单接口",notes = "显示正在退款和退款完成的列表")
	public RestResponseBean viewRefund(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		OrderBooksVo orderBooksVo = orderService.viewRefund(user.getId(),pageNumber,pageSize);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderBooksVo);
	}

	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 显示未付款的列表
	 * @description 显示未付款的列表接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/viewNonPayment
	 * @param pageNumber Integer 从第几个数据开始
	 * @param pageSize String 一页显示几条数据
	 * @return {"code": 1,"msg": "操作成功","time": "1562224104717","data": {"total": 1,"orderBooksVos": [{ 	"id": "2", 	"ordernumber": "201905301615226672", 	"createTime": "2019-05-30 16:16:17", 	"name": "NLPP书", 	"bookId": "a54918d9a40f566fb540fccd1b368277", 	"price": 50, 	"totalprice": 600, 	"booknum": 50, 	"status": null}]}}
	 * @remark 订单接口
	 * @number 99
	 **/
	@GetMapping("/viewNonPayment")
	@ApiOperation(value = "显示未付款的列表",tags = "订单接口",notes = "显示未付款的列表")
	public RestResponseBean viewNonPayment(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		OrderBooksVo orderBooksVo = orderService.viewNonPayment(user.getId(),pageNumber,pageSize);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderBooksVo);
	}

	/** showdoc
	 * @catalog 订单接口
	 * @title 显示已付款的列表
	 * @description 显示已付款的列表接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/viewPayment
	 * @param pageNumber Integer 从第几个数据开始
	 * @param pageSize String 一页显示几条数据
	 * @return {"code": 1,"msg": "操作成功","time": "1562224418674","data": {"total": 1,"orderBooksVos": [ 	{ 	"id": "cf68b80e42108ebc23e0f792638bac9f", 	"ordernumber": "201906221028050682", 	"createTime": "2019-06-22 10:28:17", 	"name": "心理学课程", 	"bookId": "1", 	"price": 100, 	"totalprice": 600, 	"booknum": 50, 	"status": 1 	}]}}
	 * @remark 订单接口
	 * @number 99
			**/
	@GetMapping("/viewPayment")
	@ApiOperation(value = "显示已付款的列表",tags = "订单接口",notes = "显示已付款的列表")
	public RestResponseBean viewPayment(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		OrderBooksVo orderBooksVo = orderService.viewPayment(user.getId(),pageNumber,pageSize);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderBooksVo);
	}

	/** showdoc
	 * @catalog 订单接口
	 * @title 显示默认地址
	 * @description 显示默认地址接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/showAddress
	 * @return {"code": 1,"msg": "操作成功","time": "1562225619305","data": {"id": "e6ceae55ad482d19fe5b78f1ac44fa55","name": "不才","phone": "18547841548","address": "河南开封","state": 1,"createTime": "2019-07-04 10:01:39","createBy": null,"updateTime": "2019-07-04 10:13:32","updateBy": null}}
	 * @remark 订单接口
	 * @number 99
	 **/
	@PostMapping("showAddress")
	@ApiOperation(value = "显示默认地址", tags = "订单接口", notes = "显示默认地址")
	public RestResponseBean showAddress() {

		Deliveryaddress deliveryaddress = deliveryaddressService.queryByState();
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),deliveryaddress);
	}

	/** showdoc
	 * @catalog 订单接口
	 * @title 显示商品名称和价格
	 * @description 显示商品名称和价格接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/showBook
	 * @return {"code": 1,"msg": "操作成功","time": "1562225770408","data": {"id": "1","name": "心理学课程","bookintro": "教会你催眠","price": 100,"num": 6,"bookComment": "%3Cp%3E%E5%AE%89%E9%A1%BF%E5%A5%BD%E5%8D%A1%E6%97%B6%E9%97%B4%E6%AE%B5%E5%92%8Ckasdsa%3C%2Fp%3E","bookContent": "大富科技纳斯达克建立符合实际可怜","bookNum": 50,"bookUrl": "","bookImg": "{\"asda\",\"da\"}","createTime": "2019-05-23 20:13:49","createBy": "boot","updateTime": "2019-06-13 18:31:45","updateBy": "superadmin"}}
	 * @remark 订单接口
	 * @number 99
	 **/
	@PostMapping("showBook")
	@ApiOperation(value = "显示商品名称和价格",tags = "订单接口", notes = "显示商品和价格")
	public RestResponseBean showBook(@RequestParam String id ){
		Books book = booksService.getById(id);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),book);
	}
//	@PostMapping("getOrder")
//	@ApiOperation(value = "根据订单编号获取订单详情",tags = "订单接口", notes = "根据订单编号获取订单详情")
	public RestResponseBean getOrder(@RequestParam String ordernumber ){
		Order order = booksService.getOrder(ordernumber);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
	}

	/**
	 * 分页列表查询
	 * @param order
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Order>> queryPageList(Order order,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<Order>> result = new Result<IPage<Order>>();
		QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
		Page<Order> page = new Page<Order>(pageNo, pageSize);
		IPage<Order> pageList = orderService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}


	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 添加订单
	 * @description 添加订单接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/add
	 * @return {"success": true,"message": "添加成功！","code": 200,"result": null,"timestamp": 1561170485067}
	 * @return_param addressId String 地址相关联id
	 * @return_param bookId String 书籍相关联id
	 * @return_param booknum int 书籍数量
	 * @return_param ordernumber int 书籍数量
	 * @return_param state int 1/未付款,2/已付款,3/已发货,4/已收货
	 * @return_param totalprice BigDecimal 商品总价
	 * @remark 订单接口
	 * @number 99
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加订单",tags = "订单接口", notes = "添加订单")
	public RestResponseBean add(@RequestBody Order order) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		Result<Order> result = new Result<Order>();
		//得到订单编号
		String orderNumber = GetOrder.getOrderIdByTime(user.getNumberid().toString());
		order.setOrdernumber(orderNumber);
		order.setPaymode(1);
		order.setState(1);
		order.setUid(user.getId())	;
		try {
			orderService.save(order);
			result.success("添加成功！");
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order.getId());
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加失败");
		}
	}

	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 修改订单状态
	 * @description 修改订单状态
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/order/edit
	 * @param id String 订单id
	 * @param state int 1/未付款,2/已付款,3/已发货,4/已收货
	 * @return {"success": true,"message": "添加成功！","code": 200,"result": null,"timestamp": 1561170485067}
	 * @remark 订单接口
	 * @number 99
	 */
	@PostMapping(value = "/edit")
	@ApiOperation(value = "修改支付方式 ",tags = "订单接口",notes = "修改支付方式")
	public RestResponseBean edit(@RequestParam String id, @RequestParam Integer paymode ) {
		Order order = new Order();
		order.setId(id);
		order.setPaymode(paymode);
		Result<Order> result = new Result<Order>();

		Order orderEntity = orderService.getById(order.getId());
		if(orderEntity==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");

		}else {
			boolean ok = orderService.updateById(order);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"修改成功");
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"修改成功");
	}

	/**
	 *   通过id删除
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/delete")
	@ApiOperation(value = "取消订单", tags = {"订单接口"}, notes = "取消订单")
	public RestResponseBean delete(@RequestParam String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderService.getById(id);
		if(order==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			boolean ok = orderService.removeById(id);
			if(ok) {
				result.success("删除成功!");
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"取消成功");
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"取消失败");
	}

	/**
	 *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<Order> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Order> result = new Result<Order>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	@ApiOperation(value = "显示订单详情", tags = "订单接口", notes = "显示订单详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderService.getById(id);
		if(order == null) {
			CoursePerInformation coursePerInformation =  coursePerInformationService.getById(id);
			if (coursePerInformation == null) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应的地址的相关实体");
			} else {
				//通过课程id 得到课程详情
				Course course = courseService.getById(coursePerInformation.getCourseId());
				CoursePerInformationVo coursePerInformationVo = new CoursePerInformationVo();
				//得到课程的视频地址和图片地址
				String videoUrl = course.getVideoUrl();
				String pictureUrl = course.getPictureUrl();
				String courseImg = course.getCourseImg();
				String videoName = course.getVideoName();
				String[] vName = videoName.split(",");
				String[] vUrl = videoUrl.split(",");

				Video video = new Video();
				video.setVideoUrl(Arrays.asList(vUrl));
				video.setVideoImg(courseImg);
				video.setVideoName(Arrays.asList(vName));

				coursePerInformationVo.setVideoUrl(video);
				String[] pUrl = pictureUrl.split(",");
				coursePerInformationVo.setImgUrl(Arrays.asList(pUrl));
				coursePerInformationVo.setTotalprice(course.getChinaPrice());
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coursePerInformationVo);
			}
		}
		Orders orders = new Orders();
		BeanUtils.copyProperties(order,orders);

		//获取地址详情
		Deliveryaddress deliveryaddress = deliveryaddressService.getById(order.getAddressId());
		if (deliveryaddress == null) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应的地址的相关实体");
		}
		//获取书籍详情
		Books book = booksService.getById(order.getBookId());
		orders.setBookUrl(book.getBookUrl());
		orders.setName(book.getName());
		orders.setPrice(book.getPrice());

		orders.setAddress(deliveryaddress);

		if(order==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			result.setResult(order);
			result.setSuccess(true);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orders);
		}
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
		// Step.1 组装查询条件
		QueryWrapper<Order> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Order order = JSON.parseObject(deString, Order.class);
				queryWrapper = QueryGenerator.initQueryWrapper(order, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Order> pageList = orderService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "订单表管理列表");
		mv.addObject(NormalExcelConstants.CLASS, Order.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单表管理列表数据", "导出人:Jeecg", "导出信息"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<Order> listOrders = ExcelImportUtil.importExcel(file.getInputStream(), Order.class, params);
				for (Order orderExcel : listOrders) {
					orderService.save(orderExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listOrders.size());
			} catch (Exception e) {
				log.error(e.getMessage());
				return Result.error("文件导入失败！");
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.ok("文件导入失败！");
	}

}
