package org.benben.modules.business.userbuke.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.buketime.entity.BukeTime;
import org.benben.modules.business.buketime.service.IBukeTimeService;
import org.benben.modules.business.buketime.vo.UserBukeDetailsVo;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseassistanttime.vo.TimeVo;
import org.benben.modules.business.leavetime.entity.LeaveTime;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userbuke.entity.UserBuke;
import org.benben.modules.business.userbuke.service.IUserBukeService;
import org.benben.modules.business.userbuke.vo.UserBukeTimeVo;
import org.benben.modules.business.userbuke.vo.UserBukesVo;
import org.benben.modules.business.userleave.service.IUserLeaveService;
import org.benben.modules.business.userleave.vo.UserLeaveDetailsVo;
import org.benben.modules.business.userleave.vo.UserLeavesVo;
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

import javax.naming.InsufficientResourcesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Security;
import java.util.*;

/**
* @Title: Controller
* @Description: 用户补课的管理
* @author： jeecg-boot
* @date：   2019-06-05
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/userBuke")
@Api(tags = {"课程接口"})
@Slf4j
public class RestUserBukeController {
   @Autowired
   private IUserBukeService userBukeService;

   @Autowired
   private IUserLeaveService userLeaveService;

   @Autowired
   private ICourseService courseService;

   @Autowired
   private IBukeTimeService bukeTimeService;

 /*  @PostMapping("/queryByName")
   @ApiOperation(value = "模糊查询补课列表",tags = {"课程接口"},notes = "模糊查询补课列表")
   public  RestResponseBean queryByName(@RequestParam Integer pageNumber,@RequestParam Integer pageSize,@RequestParam String name){
	   User user = (User)SecurityUtils.getSubject().getPrincipal();
	   UserBukesVo userBukesVo = userBukeService.queryByName(user.getId(), pageNumber, pageSize,name);
	   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userBukesVo);
   }*/

	@GetMapping("/userCourseList")
	@ApiOperation(value = "显示补课(模糊查询)列表",tags = {"课程接口"},notes = "显示补课列表")
	public RestResponseBean listPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name){
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (StringUtils.isNotBlank(name)) {
			UserBukesVo userBukesVo = userBukeService.queryByName(user.getId(), pageNumber, pageSize,name);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userBukesVo);
		} else {
			UserBukesVo userBukesVo = userBukeService.listBukkePage(user.getId(), pageNumber, pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userBukesVo);
		}
	}
	@PostMapping("/leaveDetails")
	@ApiOperation(value = "显示补课的详情",tags = {"课程接口"},notes = "显示补课的详情")
	public RestResponseBean leaveDetails(@RequestParam String id, @RequestParam String bukeId){
	/*	//得到课程的可以请假的时间
		UserLeaveDetailsVo userLeaveDetailsVo = userLeaveService.getUserLeaveDetails(id,bukeId);*/
		//得到课程的补课时间
		UserBukeDetailsVo userBukeDetailsVo = bukeTimeService.getUserBukeDetails(id,bukeId);

		//得到补课的内容
		UserBuke userBuke = userBukeService.getById(bukeId);
		userBukeDetailsVo.setComment(userBuke.getComment());

		//得到课程的详情
		Course course =courseService.getById(id);
		userBukeDetailsVo.setCourseName(course.getCourseName());
		userBukeDetailsVo.setStarttime(course.getStarttime());
		userBukeDetailsVo.setEndtime(course.getEndtime());
		userBukeDetailsVo.setCourseImg(course.getCourseImg());
		userBukeDetailsVo.setBukeNum(course.getBukeNum());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userBukeDetailsVo);
	}
   /**
	 * 分页列表查询
	* @param userBuke
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<UserBuke>> queryPageList(UserBuke userBuke,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<UserBuke>> result = new Result<IPage<UserBuke>>();
	   QueryWrapper<UserBuke> queryWrapper = QueryGenerator.initQueryWrapper(userBuke, req.getParameterMap());
	   Page<UserBuke> page = new Page<UserBuke>(pageNo, pageSize);
	   IPage<UserBuke> pageList = userBukeService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

   /**
	 *   添加
	* @param userBuke
	* @return
	*/
   @PostMapping(value = "/add")
   @ApiOperation(value = "添加补课信息", tags = {"课程接口"}, notes = "添加补课信息")
   public RestResponseBean add(@RequestBody UserBukeTimeVo userBukeTimeVo) {
	   Result<UserBuke> result = new Result<UserBuke>();
	   UserBuke userBuke = new UserBuke();
	   User user = (User) SecurityUtils.getSubject().getPrincipal();
	   userBuke.setUid(user.getId());
	   userBuke.setStatus(1);
	   userBuke.setCourseId(userBukeTimeVo.getCourseId());
	   userBuke.setComment(userBukeTimeVo.getComment());
	   userBukeService.save(userBuke);

	   //添加请假的时间
	   Map maps = new HashMap();
	   List<TimeVo> timeVoList = userBukeTimeVo.getTimeVoList();
	   for (TimeVo timeVo : timeVoList) {
		   maps.put(timeVo.getDate(),timeVo.getDays());
	   }
	   for (Object o : maps.keySet()) {
		   BukeTime bukeTime = new BukeTime();
		   bukeTime.setDate((Date) o);
		   bukeTime.setUid(user.getId());
		   bukeTime.setUserBukeId(userBuke.getId());
		   bukeTimeService.save(bukeTime);
		   List<Integer> dayList = (List<Integer>) maps.get(o);
		   for (Integer integer : dayList) {
			   BukeTime bukeTime1 = new BukeTime();
			   bukeTime1.setUid(user.getId());
			   bukeTime1.setParentId(bukeTime.getId());
			   bukeTime1.setDay(integer);
			   bukeTimeService.save(bukeTime1);
		   }
	   }


	   try {
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
	* @param userBuke
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<UserBuke> edit(@RequestBody UserBuke userBuke) {
	   Result<UserBuke> result = new Result<UserBuke>();
	   UserBuke userBukeEntity = userBukeService.getById(userBuke.getId());
	   if(userBukeEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userBukeService.updateById(userBuke);
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
   public Result<UserBuke> delete(@RequestParam(name="id",required=true) String id) {
	   Result<UserBuke> result = new Result<UserBuke>();
	   UserBuke userBuke = userBukeService.getById(id);
	   if(userBuke==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = userBukeService.removeById(id);
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
   public Result<UserBuke> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<UserBuke> result = new Result<UserBuke>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.userBukeService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<UserBuke> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<UserBuke> result = new Result<UserBuke>();
	   UserBuke userBuke = userBukeService.getById(id);
	   if(userBuke==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(userBuke);
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
	 QueryWrapper<UserBuke> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 UserBuke userBuke = JSON.parseObject(deString, UserBuke.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(userBuke, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<UserBuke> pageList = userBukeService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "用户补课的管理列表");
	 mv.addObject(NormalExcelConstants.CLASS, UserBuke.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户补课的管理列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<UserBuke> listUserBukes = ExcelImportUtil.importExcel(file.getInputStream(), UserBuke.class, params);
			 for (UserBuke userBukeExcel : listUserBukes) {
				 userBukeService.save(userBukeExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listUserBukes.size());
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
