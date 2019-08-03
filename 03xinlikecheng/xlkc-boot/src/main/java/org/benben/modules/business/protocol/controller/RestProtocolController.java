package org.benben.modules.business.protocol.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.protocol.entity.Protocol;
import org.benben.modules.business.protocol.service.IProtocolService;
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
* @Description: 协议管理
* @author： jeecg-boot
* @date：   2019-05-25
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/protocol")
@Api(tags = "注册协议接口")
@Slf4j
public class RestProtocolController {
   @Autowired
   private IProtocolService protocolService;


   @PostMapping("/registerProtocol")
   @ApiOperation(value = "显示协议接口", tags = {"注册协议接口"}, notes = "显示注册接口")
   public RestResponseBean registerProtocol(@RequestParam Integer type){
		Protocol protocol = protocolService.queryByType(type);
		if (protocol == null) {
			return new RestResponseBean(ResultEnum.OPERATION_ERROR.getValue(),ResultEnum.OPERATION_ERROR.getDesc(),null);
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),protocol);
		}

   }

   /**
	 * 分页列表查询
	* @param protocol
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<Protocol>> queryPageList(Protocol protocol,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<Protocol>> result = new Result<IPage<Protocol>>();
	   QueryWrapper<Protocol> queryWrapper = QueryGenerator.initQueryWrapper(protocol, req.getParameterMap());
	   Page<Protocol> page = new Page<Protocol>(pageNo, pageSize);
	   IPage<Protocol> pageList = protocolService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

   /**
	 *   添加
	* @param protocol
	* @return
	*/
   @PostMapping(value = "/add")
   public Result<Protocol> add(@RequestBody Protocol protocol) {
	   Result<Protocol> result = new Result<Protocol>();
	   try {
		   protocolService.save(protocol);
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
	* @param protocol
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<Protocol> edit(@RequestBody Protocol protocol) {
	   Result<Protocol> result = new Result<Protocol>();
	   Protocol protocolEntity = protocolService.getById(protocol.getId());
	   if(protocolEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = protocolService.updateById(protocol);
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
   public Result<Protocol> delete(@RequestParam(name="id",required=true) String id) {
	   Result<Protocol> result = new Result<Protocol>();
	   Protocol protocol = protocolService.getById(id);
	   if(protocol==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = protocolService.removeById(id);
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
   public Result<Protocol> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<Protocol> result = new Result<Protocol>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.protocolService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<Protocol> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<Protocol> result = new Result<Protocol>();
	   Protocol protocol = protocolService.getById(id);
	   if(protocol==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(protocol);
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
	 QueryWrapper<Protocol> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 Protocol protocol = JSON.parseObject(deString, Protocol.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(protocol, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<Protocol> pageList = protocolService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "协议管理列表");
	 mv.addObject(NormalExcelConstants.CLASS, Protocol.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("协议管理列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<Protocol> listProtocols = ExcelImportUtil.importExcel(file.getInputStream(), Protocol.class, params);
			 for (Protocol protocolExcel : listProtocols) {
				 protocolService.save(protocolExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listProtocols.size());
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
