package org.benben.modules.business.userleave.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.helpers.DateTimeDateFormat;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseassistanttime.vo.TimeVo;
import org.benben.modules.business.coursetime.entity.CourseTime;
import org.benben.modules.business.coursetime.service.ICourseTimeService;
import org.benben.modules.business.leavetime.entity.LeaveTime;
import org.benben.modules.business.leavetime.service.ILeaveTimeService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userbuke.vo.UserBukesVo;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;
import org.benben.modules.business.userleave.entity.UserLeave;
import org.benben.modules.business.userleave.service.IUserLeaveService;
import org.benben.modules.business.userleave.vo.UserLeaveDetailsVo;
import org.benben.modules.business.userleave.vo.UserLeaveTimeVo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: Controller
 * @Description: 用户请假管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userLeave")
@Api(tags = "请假接口")
@Slf4j
public class RestUserLeaveController {
	@Autowired
	private IUserLeaveService userLeaveService;

	@Autowired
	private ICourseService courseService;
	@Autowired
	private ILeaveTimeService leaveTimeService;


/*
   @PostMapping("/addLeave")
   @ApiOperation(value = "添加请假的")
*/

	@PostMapping("/leaveDetails")
	@ApiOperation(value = "显示请假的详情",tags = {"课程接口"},notes = "显示请假的详情")
	public RestResponseBean leaveDetails(@RequestParam String id,@RequestParam String leaveId){
		//得到课程的可以请假的时间
		UserLeaveDetailsVo userLeaveDetailsVo = userLeaveService.getUserLeaveDetails(id,leaveId);
		UserLeave userLeave = userLeaveService.getById(leaveId);
		userLeaveDetailsVo.setComment(userLeave.getComment());
		//得到课程的详情
		Course course =courseService.getById(id);
		userLeaveDetailsVo.setCourseName(course.getCourseName());
		userLeaveDetailsVo.setStarttime(course.getStarttime());
		userLeaveDetailsVo.setEndtime(course.getEndtime());
		userLeaveDetailsVo.setCourseImg(course.getCourseImg());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userLeaveDetailsVo);
	}

	/*@PostMapping("/queryByName")
	@ApiOperation(value = "模糊查询请假列表",tags = {"课程接口"},notes = "模糊查询请假列表")
	public  RestResponseBean queryByName(@RequestParam Integer pageNumber,@RequestParam Integer pageSize,@RequestParam String name){
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		UserLeavesVo userLeavesVos = userLeaveService.queryByName(user.getId(), pageNumber, pageSize,name);
	}*/

	@GetMapping("/userCourseList")
	@ApiOperation(value = "显示请假课程(模糊查询)列表",tags = {"课程接口"},notes = "显示请假课程(模糊查询)列表")
	public RestResponseBean listPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name){
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (StringUtils.isNotBlank(name)) {
			UserLeavesVo userLeavesVos = userLeaveService.queryByName(user.getId(), pageNumber, pageSize,name);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userLeavesVos);
		} else {
			UserLeavesVo userLeavesVos = userLeaveService.listPage(user.getId(), pageNumber, pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userLeavesVos);
		}
	}

	/**
	 * 分页列表查询
	 * @param userLeave
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserLeave>> queryPageList(UserLeave userLeave,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserLeave>> result = new Result<IPage<UserLeave>>();
		QueryWrapper<UserLeave> queryWrapper = QueryGenerator.initQueryWrapper(userLeave, req.getParameterMap());
		Page<UserLeave> page = new Page<UserLeave>(pageNo, pageSize);
		IPage<UserLeave> pageList = userLeaveService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加请假数据",tags = {"课程接口"}, notes = "添加请假数据")
	public RestResponseBean add(@RequestBody UserLeaveTimeVo userLeaveTimeVo) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeave = new UserLeave();
		//得到用户id
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		//添加请假数据
		userLeave.setUserId(user.getId());
		userLeave.setStatus(1);
		userLeave.setCourseId(userLeaveTimeVo.getCourseId());
		userLeave.setComment(userLeaveTimeVo.getComment());
		userLeaveService.save(userLeave);

		//添加请假的时间
		Map maps = new HashMap();
		List<TimeVo> timeVoList = userLeaveTimeVo.getTimeVoList();
		for (TimeVo timeVo : timeVoList) {
			maps.put(timeVo.getDate(),timeVo.getDays());
		}
		for (Object o : maps.keySet()) {
			LeaveTime leaveTime = new LeaveTime();
			leaveTime.setDate((Date) o);
			leaveTime.setUid(user.getId());
			leaveTime.setUserLeaveId(userLeave.getId());
			leaveTimeService.save(leaveTime);
			List<Integer> dayList = (List<Integer>) maps.get(o);
			for (Integer integer : dayList) {
				LeaveTime leaveTime1 = new LeaveTime();
				leaveTime1.setUid(user.getId());
				leaveTime1.setParentId(leaveTime.getId());
				leaveTime1.setDay(integer);
				leaveTimeService.save(leaveTime1);
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	}

	/**
	 *  编辑
	 * @param userLeave
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserLeave> edit(@RequestBody UserLeave userLeave) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeaveEntity = userLeaveService.getById(userLeave.getId());
		if(userLeaveEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userLeaveService.updateById(userLeave);
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
	public Result<UserLeave> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeave = userLeaveService.getById(id);
		if(userLeave==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userLeaveService.removeById(id);
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
	public Result<UserLeave> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserLeave> result = new Result<UserLeave>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userLeaveService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserLeave> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeave = userLeaveService.getById(id);
		if(userLeave==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userLeave);
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
		QueryWrapper<UserLeave> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserLeave userLeave = JSON.parseObject(deString, UserLeave.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userLeave, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserLeave> pageList = userLeaveService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户请假管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserLeave.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户请假管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserLeave> listUserLeaves = ExcelImportUtil.importExcel(file.getInputStream(), UserLeave.class, params);
				for (UserLeave userLeaveExcel : listUserLeaves) {
					userLeaveService.save(userLeaveExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserLeaves.size());
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
