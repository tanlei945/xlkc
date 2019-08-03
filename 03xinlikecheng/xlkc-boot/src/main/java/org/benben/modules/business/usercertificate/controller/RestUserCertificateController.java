package org.benben.modules.business.usercertificate.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercertificate.entity.UserCertificate;
import org.benben.modules.business.usercertificate.service.IUserCertificateService;
import org.benben.modules.business.usercertificate.vo.CourseNameVo;
import org.benben.modules.business.usercertificate.vo.UserCertificatesVo;
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
* @Description: 我的证书管理
* @author： jeecg-boot
* @date：   2019-06-10
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/userCertificate")
@Api(tags = {"证书接口"})
@Slf4j
public class RestUserCertificateController {
   @Autowired
   private IUserCertificateService userCertificateService;

   @PostMapping("/getCourseName")
   @ApiOperation(value = "展示所有的课程名称", tags = "证书接口", notes = "展示所有的课程名称")
   public RestResponseBean getCourseName() {
   		List<CourseNameVo> courseNames = userCertificateService.getCourseName();
   		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(), courseNames);
   }

   @GetMapping("/listCertificate")
   @ApiOperation(value = "分页展示我的证书", tags = {"证书接口"}, notes = "分页展示我的证书")
   public RestResponseBean  listCertificate(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
   		User user = (User) SecurityUtils.getSubject().getPrincipal();
	   	UserCertificatesVo certificatesVo = userCertificateService.listCertificate(user.getId(),pageNumber,pageSize);
   		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),certificatesVo);
   }

   /**
	 * 分页列表查询
	* @param userCertificate
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<UserCertificate>> queryPageList(UserCertificate userCertificate,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<UserCertificate>> result = new Result<IPage<UserCertificate>>();
	   QueryWrapper<UserCertificate> queryWrapper = QueryGenerator.initQueryWrapper(userCertificate, req.getParameterMap());
	   Page<UserCertificate> page = new Page<UserCertificate>(pageNo, pageSize);
	   IPage<UserCertificate> pageList = userCertificateService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

   /**
	 *   添加
	* @param userCertificate
	* @return
	*/
   @PostMapping(value = "/add")
   @ApiOperation(value = "添加证书信息", tags = {"证书接口"}, notes = "添加证书信息")
   public RestResponseBean add(@RequestBody UserCertificate userCertificate) {
	   Result<UserCertificate> result = new Result<UserCertificate>();
	   User user = (User) SecurityUtils.getSubject().getPrincipal();
	   userCertificate.setUid(user.getId());
	   try {
		   userCertificateService.save(userCertificate);
		   result.success("添加成功！");
		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	   } catch (Exception e) {
		   e.printStackTrace();
		   log.info(e.getMessage());
		   result.error500("操作失败");
		   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"添加失败");

	   }
   }

   /**
	 *  编辑
	* @param userCertificate
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<UserCertificate> edit(@RequestBody UserCertificate userCertificate) {
	   Result<UserCertificate> result = new Result<UserCertificate>();
	   UserCertificate userCertificateEntity = userCertificateService.getById(userCertificate.getId());
	   if(userCertificateEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userCertificateService.updateById(userCertificate);
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
   public Result<UserCertificate> delete(@RequestParam(name="id",required=true) String id) {
	   Result<UserCertificate> result = new Result<UserCertificate>();
	   UserCertificate userCertificate = userCertificateService.getById(id);
	   if(userCertificate==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userCertificateService.removeById(id);
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
   public Result<UserCertificate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<UserCertificate> result = new Result<UserCertificate>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.userCertificateService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<UserCertificate> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<UserCertificate> result = new Result<UserCertificate>();
	   UserCertificate userCertificate = userCertificateService.getById(id);
	   if(userCertificate==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(userCertificate);
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
	 QueryWrapper<UserCertificate> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 UserCertificate userCertificate = JSON.parseObject(deString, UserCertificate.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(userCertificate, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<UserCertificate> pageList = userCertificateService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "我的证书管理列表");
	 mv.addObject(NormalExcelConstants.CLASS, UserCertificate.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("我的证书管理列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<UserCertificate> listUserCertificates = ExcelImportUtil.importExcel(file.getInputStream(), UserCertificate.class, params);
			 for (UserCertificate userCertificateExcel : listUserCertificates) {
				 userCertificateService.save(userCertificateExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listUserCertificates.size());
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
