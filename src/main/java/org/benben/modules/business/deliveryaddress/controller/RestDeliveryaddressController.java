package org.benben.modules.business.deliveryaddress.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.benben.modules.business.deliveryaddress.service.IDeliveryaddressService;
import org.benben.modules.business.deliveryaddress.vo.DeliveryaddresVo;
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
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 收获地址管理
* @author： jeecg-boot
* @date：   2019-05-24
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/deliveryaddress")
@Api(tags = {"书店接口"})
@Slf4j
public class RestDeliveryaddressController {
   @Autowired
   private IDeliveryaddressService deliveryaddressService;

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 分页显示全部收货地址
	 * @description 分页显示全部收货地址接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/deliveryaddress/listPage
	 * @param pageNumber Integer 分页从第几个开始
	 * @param pageSize Integer 一页显示几条数据
	 * @return {"code": 1,"msg": "操作成功","time": "1562204862998","data": {"total": 1,"deliveryaddress": [{ 	"id": "1", 	"name": "张三", 	"phone": "1821524545", 	"address": "河南省 郑州市 二七区 大学路", 	"state": 1, 	"createTime": "2019-05-24 10:31:19", 	"createBy": "boot", 	"updateTime": null, 	"updateBy": null}]}}
	 * @remark 书店接口
	 * @number 99
	 **/
   @GetMapping("/listPage")
   @ApiOperation(value = "分页显示全部收货地址", tags = {"书店接口"}, notes = "分页显示全部收货地址")
   public RestResponseBean  listPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

   		if (pageNumber != null && pageSize != null) {
			DeliveryaddresVo deliveryaddresVo = deliveryaddressService.listPage(pageNumber, pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), deliveryaddresVo);
		} else{
   			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
   }

   /**
	 * 分页列表查询
	* @param deliveryaddress
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<Deliveryaddress>> queryPageList(Deliveryaddress deliveryaddress,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<Deliveryaddress>> result = new Result<IPage<Deliveryaddress>>();
	   QueryWrapper<Deliveryaddress> queryWrapper = QueryGenerator.initQueryWrapper(deliveryaddress, req.getParameterMap());
	   Page<Deliveryaddress> page = new Page<Deliveryaddress>(pageNo, pageSize);
	   IPage<Deliveryaddress> pageList = deliveryaddressService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 添加收货地址
	 * @description 添加收货地址接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/deliveryaddress/add
	 * @param address String 地址
	 * @param name String 收货人名称
	 * @param phone String 联系电话
	 * @param state Integer 0/不是默认地址，1/默认地址
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 书店接口
	 * @number 99
	 **/
   @PostMapping(value = "/add")
   @ApiOperation(value = "添加收货地址", tags = {"书店接口"}, notes = "添加收货地址")
   public RestResponseBean add(@RequestBody Deliveryaddress deliveryaddress) {
	   Result<Deliveryaddress> result = new Result<Deliveryaddress>();
	   if (deliveryaddress.getState() == 1) {
		   Deliveryaddress deliveryaddress1 = deliveryaddressService.queryByState();
		   if(deliveryaddress1 != null) {
			   deliveryaddress1.setState(0);
			   deliveryaddressService.updateById(deliveryaddress1);
		   }
	   }
	   try {
		   deliveryaddressService.save(deliveryaddress);
		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	   } catch (Exception e) {
		   e.printStackTrace();
		   log.info(e.getMessage());
		   result.error500("操作失败");
	   }
	   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"添加成功");
   }

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 修改收货地址
	 * @description 修改收货地址接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/deliveryaddress/edit
	 * @param address String 地址
	 * @param name String 收货人名称
	 * @param phone String 联系电话
	 * @param state Integer 0/不是默认地址，1/默认地址
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "修改成功"}
	 * @remark 书店接口
	 * @number 99
	 **/
   @PostMapping(value = "/edit")
   @ApiOperation(value = "修改收货地址", tags = {"书店接口"}, notes = "修改收货地址")
   public RestResponseBean edit(@RequestBody Deliveryaddress deliveryaddress) {
	   Result<Deliveryaddress> result = new Result<Deliveryaddress>();
	   Deliveryaddress deliveryaddressEntity = deliveryaddressService.getById(deliveryaddress.getId());
	   if (deliveryaddress.getState() == 1) {
		   Deliveryaddress deliveryaddress1 = deliveryaddressService.queryByState();
		   if(deliveryaddress1 != null) {
			   deliveryaddress1.setState(0);
			   deliveryaddressService.updateById(deliveryaddress1);
		   }
	   }

	   if(deliveryaddressEntity==null) {
		   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
	   }else {
		   boolean ok = deliveryaddressService.updateById(deliveryaddress);

		   //TODO 返回false说明什么？
		   if(ok) {
			   result.success("修改成功!");
			   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"修改成功");
		   }
	   }
	   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"修改失败");

   }

   /**
	 *   通过id删除
	* @param id
	* @return
	*/
   @PostMapping(value = "/delete")
   @ApiOperation(value = "删除收货地址",tags = {"书店接口"},notes = "删除收货地址")
   public Result<Deliveryaddress> delete(@RequestParam(name="id",required=true) String id) {
	   Result<Deliveryaddress> result = new Result<Deliveryaddress>();
	   Deliveryaddress deliveryaddress = deliveryaddressService.getById(id);
	   if(deliveryaddress==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = deliveryaddressService.removeById(id);
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
   public Result<Deliveryaddress> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<Deliveryaddress> result = new Result<Deliveryaddress>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.deliveryaddressService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<Deliveryaddress> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<Deliveryaddress> result = new Result<Deliveryaddress>();
	   Deliveryaddress deliveryaddress = deliveryaddressService.getById(id);
	   if(deliveryaddress==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(deliveryaddress);
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
	 QueryWrapper<Deliveryaddress> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 Deliveryaddress deliveryaddress = JSON.parseObject(deString, Deliveryaddress.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(deliveryaddress, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<Deliveryaddress> pageList = deliveryaddressService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "收获地址管理列表");
	 mv.addObject(NormalExcelConstants.CLASS, Deliveryaddress.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收获地址管理列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<Deliveryaddress> listDeliveryaddresss = ExcelImportUtil.importExcel(file.getInputStream(), Deliveryaddress.class, params);
			 for (Deliveryaddress deliveryaddressExcel : listDeliveryaddresss) {
				 deliveryaddressService.save(deliveryaddressExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listDeliveryaddresss.size());
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
