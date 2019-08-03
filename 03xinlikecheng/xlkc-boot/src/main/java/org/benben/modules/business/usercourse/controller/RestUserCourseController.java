package org.benben.modules.business.usercourse.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.course.vo.CoursesConfirmVo;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;
import org.benben.modules.system.entity.SysUser;
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
* @Description: 用户课程
* @author： jeecg-boot
* @date：   2019-06-01
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/userCourse")
@Api(tags = {"课程接口"})
@Slf4j
public class RestUserCourseController {
   @Autowired
   private IUserCourseService userCourseService;

   @Autowired
   private ICourseService courseService;

   @GetMapping("/userCourseList")
   @ApiOperation(value = "显示我的课程是否可以退款（模糊查询）列表",tags = {"课程接口"},notes = "显示我的课程是否可以退款（模糊查询）列表")
   public RestResponseBean listPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name){
	   User user = (User) SecurityUtils.getSubject().getPrincipal();

	   UserCoursesVo userCoursesVo = userCourseService.listPage(name, user.getId(), pageNumber, pageSize);
	   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userCoursesVo);
   }

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 课程已完成列表
	 * @description 课程已完成列表接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/userCourse/courseAchieve
	 * @return {"code": 1,"msg": "操作成功","time": "1562309700510","data": {"total": 2,"courseConfirmVos": [{ 	"id": "297e017e6ac9c038016ac9c038ef0000", 	"courseName": "NLPP", 	"comment": "关于NLPP的课程简介", 	"starttime": "2019-05-05", 	"endtime": "2019-05-17", 	"address": "大唐西市"},{ 	"id": "1", 	"courseName": "MMLP", 	"comment": "关于NLPP的课程简介", 	"starttime": "2019-02-04", 	"endtime": "2019-02-12", 	"address": "大唐西市"}]}}
	 * @param pageNumber  Integer 从第几条数据开始
	 * @param pageSize  Integer 一页显示几条数据
	 * @return_param id Intger 课程id
	 * @return_param courseName String 课程名字
	 * @return_param comment String 课程介绍
	 * @return_param starttime String 课程开始时间
	 * @return_param endtime String 课程结束时间
	 * @return_param address String 上课地址
	 * @remark 课程接口
	 * @number 99
	 */
	@PostMapping("/courseAchieve")
	@ApiOperation(value = "课程已完成(模糊查询)列表",tags = {"课程接口"}, notes = "课程已完成列表")
	public RestResponseBean coureseAchieve(@RequestParam Integer pageNumber,@RequestParam Integer pageSize,String name ){
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (name == null) {
			name = "";
		}
		CoursesConfirmVo coourses = courseService.courseAchieve(name, user.getId(),pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coourses);
	}

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 课程是否确认列表
	 * @description 课程是否确认列表接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/userCourse/courseConfirm
	 * @return {"code": 1,"msg": "操作成功","time": "1562308907114","data": {"total": 1,"courseConfirmVos": [{ 	"id": "297e017e6ac9c038016ac9c038ef0000", 	"courseName": "NLPP", 	"comment": "关于NLPP的课程简介", 	"starttime": "2019-05-05", 	"endtime": "2019-05-17", 	"address": "大唐西市"}]}}
	 * @param courseVerify 是 Integer 1/课程付款已确认，0/课程付款待定
	 * @param pageNumber  Integer 从第几条数据开始
	 * @param pageSize  Integer 一页显示几条数据
	 * @return_param id Intger 课程id
	 * @return_param courseName String 课程名字
	 * @return_param comment String 课程介绍
	 * @return_param starttime String 课程开始时间
	 * @return_param endtime String 课程结束时间
	 * @return_param address String 上课地址
	 * @remark 课程接口
	 * @number 99
	 */
	@PostMapping("/courseConfirm")
	@ApiOperation(value = "课程是否确认(模糊查询)列表", tags = {"课程接口"}, notes = "课程是否确认")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "1/课程付款已确认 0/课程付款待定",name = "courseVerify")
	})
	public RestResponseBean verifyCourse(@RequestParam Integer courseVerify,@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name){
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (name == null) {
			name = "";
		}
		CoursesConfirmVo coursesConfirmVo = courseService.verifyCourse(user.getId(),name,courseVerify,pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coursesConfirmVo);
	}
   /**
	 * 分页列表查询
	* @param userCourse
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<UserCourse>> queryPageList(UserCourse userCourse,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<UserCourse>> result = new Result<IPage<UserCourse>>();
	   QueryWrapper<UserCourse> queryWrapper = QueryGenerator.initQueryWrapper(userCourse, req.getParameterMap());
	   Page<UserCourse> page = new Page<UserCourse>(pageNo, pageSize);
	   IPage<UserCourse> pageList = userCourseService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

   /**
	 *   添加
	* @param userCourse
	* @return
	*/
   @PostMapping(value = "/add")
//   @ApiOperation(value = "添加到我的课程",tags = {"课程接口"}, notes = "添加到我的课程")
   public Result<UserCourse> add(@RequestBody UserCourse userCourse) {
	   Result<UserCourse> result = new Result<UserCourse>();
	  User user = (User)SecurityUtils.getSubject().getPrincipal();
	  userCourse.setUid(user.getId());
	   try {
		   userCourseService.save(userCourse);
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
	* @param userCourse
	* @return
	*/
   @PutMapping(value = "/edit")
//   @ApiOperation(value = "修改退款状态", tags = {"课程接口"}, notes = "修改退款状态")
//   @ApiImplicitParam(value = "stats 0/可以退款 1/退款中 2/退款我完成  3/ 不可退款")
   public Result<UserCourse> edit(@RequestBody UserCourse userCourse) {
	   Result<UserCourse> result = new Result<UserCourse>();
	   UserCourse userCourseEntity = userCourseService.getById(userCourse.getId());
	   if(userCourseEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userCourseService.updateById(userCourse);
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
   public Result<UserCourse> delete(@RequestParam(name="id",required=true) String id) {
	   Result<UserCourse> result = new Result<UserCourse>();
	   UserCourse userCourse = userCourseService.getById(id);
	   if(userCourse==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userCourseService.removeById(id);
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
   public Result<UserCourse> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<UserCourse> result = new Result<UserCourse>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.userCourseService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<UserCourse> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<UserCourse> result = new Result<UserCourse>();
	   UserCourse userCourse = userCourseService.getById(id);
	   if(userCourse==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(userCourse);
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
	 QueryWrapper<UserCourse> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 UserCourse userCourse = JSON.parseObject(deString, UserCourse.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(userCourse, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<UserCourse> pageList = userCourseService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "用户课程列表");
	 mv.addObject(NormalExcelConstants.CLASS, UserCourse.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户课程列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<UserCourse> listUserCourses = ExcelImportUtil.importExcel(file.getInputStream(), UserCourse.class, params);
			 for (UserCourse userCourseExcel : listUserCourses) {
				 userCourseService.save(userCourseExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listUserCourses.size());
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
