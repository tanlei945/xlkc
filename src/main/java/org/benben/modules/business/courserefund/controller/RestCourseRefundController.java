package org.benben.modules.business.courserefund.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.course.vo.CoursesConfirmVo;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.courserefund.entity.CourseRefund;
import org.benben.modules.business.courserefund.service.ICourseRefundService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;
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
* @Description: 课程退款
* @author： jeecg-boot
* @date：   2019-06-03
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/courseRefund")
@Api(tags = {"课程接口"})
@Slf4j
public class RestCourseRefundController {
   @Autowired
   private ICourseRefundService courseRefundService;

	@Autowired
	private ICourseService courseService;

	@Autowired
	private IUserCourseService userCourseService;
	@Autowired
	private ICoursePerInformationService perInformationService;

/*	*//**
	 * showdoc
	 * @catalog 课程接口
	 * @title 已确认我的课程模糊查询
	 * @description 已确认我的课程模糊查询
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseRefund/queryByName
	 * @param name String 课程名字
	 * @param pageNumber int 分页从第几个开始
	 * @param pageSize int 一页显示几条数据
	 * @return {"code": 1,"msg": "操作成功","time": "1562202821635","data": {"total": 1,"userCourseVos": [{ 	"id": "297e017e6ac9c038016ac9c038ef0000", 	"courseName": "NLPP", 	"starttime": "2019-05-05 ", 	"endtime": "2019-05-17 ", 	"status": 1}]}}
	 * @remark 课程接口
	 * @number 99
	 **//*
	@PostMapping("/queryByName")
	@ApiOperation(value = "已确认我的课程模糊查询", tags = {"课程接口"}, notes = "已确认我的课程模糊查询")
	public RestResponseBean queryByName(@RequestParam String name,@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		UserCoursesVo userCoursesVo = userCourseService.queryByName(user.getId(), pageNumber, pageSize,name);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userCoursesVo);
	}*/

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 已完成退款列表
	 * @description 已完成退款列表
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseRefund/achieveRefund
	 * @param pageNumber int 分页从第几个开始
	 * @param pageSize int 一页显示几条数据
	 * @return  {"code": 1,"msg": "操作成功","time": "1562203188086","data": {"total": 2,"courseConfirmVos": [{ 	"id": "1", 	"courseName": "MMLP", 	"comment": "关于NLPP的课程简介", 	"starttime": "2019-02-04", 	"endtime": "2019-02-12", 	"address": "大唐西市"}]}}
	 * @return_param id Intger 课程id
	 * @return_param courseName String 课程名字
	 * @return_param comment String 课程介绍
	 * @return_param starttime String 课程开始时间
	 * @return_param endtime String 课程结束时间
	 * @return_param address String 上课地址
	 * @remark 课程接口
	 * @number 99
	 **/
	@GetMapping("/achieveRefund")
	@ApiOperation(value = "已完成退款（模糊查询）列表",tags = {"课程接口"},notes = "已完成退款（模糊查询）列表")
	public RestResponseBean lisPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name ) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (name == null) {
			name = "";
		}

		CoursesConfirmVo coourses = courseService.achieveRefund(name,user.getId(),pageNumber,pageSize);
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coourses);
	}

   /**
	 * 分页列表查询
	* @param courseRefund
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<CourseRefund>> queryPageList(CourseRefund courseRefund,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<CourseRefund>> result = new Result<IPage<CourseRefund>>();
	   QueryWrapper<CourseRefund> queryWrapper = QueryGenerator.initQueryWrapper(courseRefund, req.getParameterMap());
	   Page<CourseRefund> page = new Page<CourseRefund>(pageNo, pageSize);
	   IPage<CourseRefund> pageList = courseRefundService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 添加课程退款
	 * @description 添加课程退款接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseRefund/add
	 * @param courseId String 课程id
	 * @param reason String 退款理由
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 课程接口
	 * @number 99
	 **/
   @PostMapping(value = "/add")
   @ApiOperation(value = "添加课程退款", tags = {"课程接口"}, notes = "添加课程退款")
   public RestResponseBean add(@RequestBody CourseRefund courseRefund) {
	   Result<CourseRefund> result = new Result<CourseRefund>();

	   //得到课程的金额
	   String courseId = courseRefund.getCourseId();
	   Course course = courseService.getById(courseId);
	   courseRefund.setMoney(course.getChinaPrice());
	   User user = (User) SecurityUtils.getSubject().getPrincipal();
	   courseRefund.setUid(user.getId());

	   //修改我的课程中的退款状态
	   UserCourse userCourse = userCourseService.queryByCourse(user.getId(),course.getId());
	   if (userCourse != null) {
		   //改为退款中
		   userCourse.setStatus(1);
		   userCourseService.updateById(userCourse);
	   } else{
		   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"请购买该课程");
	   }

	   try {
		   courseRefundService.save(courseRefund);
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
	* @param courseRefund
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<CourseRefund> edit(@RequestBody CourseRefund courseRefund) {
	   Result<CourseRefund> result = new Result<CourseRefund>();
	   CourseRefund courseRefundEntity = courseRefundService.getById(courseRefund.getId());
	   if(courseRefundEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = courseRefundService.updateById(courseRefund);
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
   public Result<CourseRefund> delete(@RequestParam(name="id",required=true) String id) {
	   Result<CourseRefund> result = new Result<CourseRefund>();
	   CourseRefund courseRefund = courseRefundService.getById(id);
	   if(courseRefund==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = courseRefundService.removeById(id);
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
   public Result<CourseRefund> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<CourseRefund> result = new Result<CourseRefund>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.courseRefundService.removeByIds(Arrays.asList(ids.split(",")));
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
   @ApiOperation(value = "显示退款详情", tags = {"课程接口"}, notes = "显示退款详情")
   public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
	   Result<CourseRefund> result = new Result<CourseRefund>();

	   User user = (User) SecurityUtils.getSubject().getPrincipal();
	   //通过课程订单得到 课程id
	   CoursePerInformation information = perInformationService.getById(id);

	   CourseRefund courseRefund = courseRefundService.getCourseRefund(information.getCourseId(),user.getId());
	   if(courseRefund==null) {
		   result.error500("未找到对应实体");
		   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
	   }else {
		   result.setResult(courseRefund);
		   result.setSuccess(true);
		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),courseRefund);
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
	 QueryWrapper<CourseRefund> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 CourseRefund courseRefund = JSON.parseObject(deString, CourseRefund.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(courseRefund, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<CourseRefund> pageList = courseRefundService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "课程退款列表");
	 mv.addObject(NormalExcelConstants.CLASS, CourseRefund.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("课程退款列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<CourseRefund> listCourseRefunds = ExcelImportUtil.importExcel(file.getInputStream(), CourseRefund.class, params);
			 for (CourseRefund courseRefundExcel : listCourseRefunds) {
				 courseRefundService.save(courseRefundExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listCourseRefunds.size());
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
