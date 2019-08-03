package org.benben.modules.business.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.bookrefund.entity.BookRefund;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.benben.modules.business.deliveryaddress.service.IDeliveryaddressService;
import org.benben.modules.business.logistics.entity.Logistics;
import org.benben.modules.business.logistics.service.ILogisticsService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.order.vo.OrderBookVo;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Title: Controller
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
@RestController
@RequestMapping("/order/order")
@Slf4j
public class OrderController {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IDeliveryaddressService addressService;

	@Autowired
	private IBooksService bookService;

	@Autowired
	private ILogisticsService logisticsService;

	 /**
	  * 得到全部订单信息
	  * @return
	  */
	@PostMapping("/getOrders")
	@ApiOperation(value = "显示",tags = "显示接口")
	public RestResponseBean getOrders() {
		List<OrderBookVo> orders = orderService.getOrders();
		return new RestResponseBean(ResultEnum.OPERATION_CORRECT.getValue(),ResultEnum.OPERATION_CORRECT.getDesc(),orders);
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
		List<Order> records = pageList.getRecords();
		for (Order record : records) {
			//得到用户昵称
			User user = userService.getById(record.getUid());
			record.setNickname(user.getNickname());
			//得到地址详情
			Deliveryaddress address = addressService.getById(record.getAddressId());
			record.setAddress(address.getAddress());
			//得到书籍名称
			Books books = bookService.getById(record.getBookId());
			record.setBookName(books.getName());
			//得到物流信息
			Logistics logistics = logisticsService.queryByLogistics(record.getId());
			record.setLogistics(logistics);

		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param order
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Order> add(@RequestBody Order order) {
		Result<Order> result = new Result<Order>();
		try {
			orderService.save(order);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param order
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Order> edit(@RequestBody Order order) {
		Result<Order> result = new Result<Order>();
		Order orderEntity = orderService.getById(order.getId());
		//当|订单状态修改的时候添加进我的退款当中
		BookRefund bookRefund = new BookRefund();
		if (order.getLogistics() != null) {
			//先判断物流表是否有信息
			Logistics logistics = logisticsService.queryByLogistics(order.getId());
			if (logistics == null) {
				//			if ()
				//添加进物流管理
				Logistics logistics1 = order.getLogistics();
				logistics1.setBookId(order.getBookId());
				logistics1.setOrderId(order.getId());
				logisticsService.save(logistics1);
			} else {
				//修改物流管理
				logistics.setBookId(order.getBookId());
				Logistics logistics1 = order.getLogistics();
				logistics.setExpress(logistics1.getExpress());
				logistics.setWaybill(logistics1.getWaybill());
				logisticsService.updateById(logistics);
			}
		}
		if(orderEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderService.updateById(order);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<Order> delete(@RequestParam(name="id",required=true) String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderService.getById(id);
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
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
	public Result<Order> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderService.getById(id);
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(order);
			result.setSuccess(true);
		}
		return result;
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
