package org.benben.modules.business.bookrefund.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.bookrefund.entity.BookRefund;
import org.benben.modules.business.bookrefund.service.IBookRefundService;
import org.benben.modules.business.bookrefund.vo.BookRefundVo;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 书籍退款表
* @author： jeecg-boot
* @date：   2019-06-05
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/bookRefund")
@Api(tags = {"订单接口"})
@Slf4j
public class RestBookRefundController {
   @Autowired
   private IBookRefundService bookRefundService;

   @Autowired
   private IBooksService booksService;
   @Autowired
   private IOrderService orderService;

	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 显示退款的书籍名称和金额
	 * @description 显示退款的书籍名称和金额接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/bookRefund/viewBookDetails
	 * @return {"code": 1,"msg": "操作成功","time": "1561169547210","data": {"id": "1","name": "心理学课程","bookUrl": "","money": 300}
	 * @param totalPirce BigDecimal 商品的总价钱
	 * @param bookId String 商品id
	 * @remark 订单接口
	 * @number 99
	 */
	@GetMapping("/viewBookDetails")
	@ApiOperation(value = "显示退款的书籍名称和金额",tags ="订单接口",notes = "显示退款的书籍名称和金额")
	public RestResponseBean viewBookDetails(@RequestParam BigDecimal totalPirce, @RequestParam String bookId) {
		BookRefundVo bookRefund = new BookRefundVo();
		//得到书籍名称和书籍封面
		Books book = booksService.getById(bookId);

		bookRefund.setId(bookId);
		bookRefund.setName(book.getName());
		bookRefund.setBookUrl(book.getBookUrl());
		bookRefund.setMoney(totalPirce);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),bookRefund);
	}

   /**
	 * 分页列表查询
	* @param bookRefund
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<BookRefund>> queryPageList(BookRefund bookRefund,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<BookRefund>> result = new Result<IPage<BookRefund>>();
	   QueryWrapper<BookRefund> queryWrapper = QueryGenerator.initQueryWrapper(bookRefund, req.getParameterMap());
	   Page<BookRefund> page = new Page<BookRefund>(pageNo, pageSize);
	   IPage<BookRefund> pageList = bookRefundService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

	/**
	 * showdoc
	 * @catalog 订单接口
	 * @title 添加书籍退款
	 * @description 添加书籍退款接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/bookRefund/add
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 订单接口
	 * @number 99
	 */
   @PostMapping(value = "/add")
   @ApiOperation(value = "添加书籍退款",tags = "订单接口", notes = "添加书籍退款")
   public RestResponseBean add(@RequestBody BookRefund bookRefund) {
	   Result<BookRefund> result = new Result<BookRefund>();
	   User user = (User) SecurityUtils.getSubject().getPrincipal();


	   //添加退款的时候

	   //添加退款之前查看是否已经添加过退款
	   BookRefund bookRefund1 = bookRefundService.queryByUidAndOrderId(user.getId(),bookRefund.getOrderId());
	   if (bookRefund1 != null) {
	   	return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.desc(),"该订单已经退款过！");
	   }

	   bookRefund.setUid(user.getId());
	   //通过orderId 得到订单详情
	   Order order = orderService.getById(bookRefund.getOrderId());
	   bookRefund.setBookId(order.getBookId());
	   bookRefund.setMoney(order.getTotalprice());
	   bookRefund.setStatus(2);

	   try {
		   bookRefundService.save(bookRefund);
		   //修改订单
		   Order byId = orderService.getById(bookRefund.getOrderId());
		   byId.setState(10);
		   orderService.updateById(byId);
		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	   } catch (Exception e) {
		   e.printStackTrace();
		   log.info(e.getMessage());
		   result.error500("操作失败");
	   }
	   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"添加成功");
   }

   /**
	 *  编辑
	* @param bookRefund
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<BookRefund> edit(@RequestBody BookRefund bookRefund) {
	   Result<BookRefund> result = new Result<BookRefund>();
	   BookRefund bookRefundEntity = bookRefundService.getById(bookRefund.getId());
	   if(bookRefundEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = bookRefundService.updateById(bookRefund);
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
   public Result<BookRefund> delete(@RequestParam(name="id",required=true) String id) {
	   Result<BookRefund> result = new Result<BookRefund>();
	   BookRefund bookRefund = bookRefundService.getById(id);
	   if(bookRefund==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = bookRefundService.removeById(id);
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
   public Result<BookRefund> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<BookRefund> result = new Result<BookRefund>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.bookRefundService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<BookRefund> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<BookRefund> result = new Result<BookRefund>();
	   BookRefund bookRefund = bookRefundService.getById(id);
	   if(bookRefund==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(bookRefund);
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
	 QueryWrapper<BookRefund> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 BookRefund bookRefund = JSON.parseObject(deString, BookRefund.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(bookRefund, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<BookRefund> pageList = bookRefundService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "书籍退款表列表");
	 mv.addObject(NormalExcelConstants.CLASS, BookRefund.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("书籍退款表列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<BookRefund> listBookRefunds = ExcelImportUtil.importExcel(file.getInputStream(), BookRefund.class, params);
			 for (BookRefund bookRefundExcel : listBookRefunds) {
				 bookRefundService.save(bookRefundExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listBookRefunds.size());
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
