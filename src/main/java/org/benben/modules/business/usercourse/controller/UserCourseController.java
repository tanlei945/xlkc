package org.benben.modules.business.usercourse.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.usercourse.vo.UserCourseVo;
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
 * @Description: 用户课程
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@RestController
@RequestMapping("/usercourse/userCourse")
@Slf4j
public class UserCourseController {
	@Autowired
	private IUserCourseService userCourseService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICourseService courseService;

	@PostMapping("/getUserCourseList")
	public RestResponseBean getUserCourseList() {
		List<UserCourseVo> userCourseVos = userCourseService.getUserCourseList();
		return  new RestResponseBean(ResultEnum.OPERATION_CORRECT.getValue(),ResultEnum.OPERATION_CORRECT.getDesc(),userCourseVos);
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
		List<UserCourse> list = pageList.getRecords();
		for (UserCourse course : list) {
			User user = userService.getById(course.getUid());
			course.setNickname(user.getNickname());
			Course course1 = courseService.getById(course.getCourseId());
			course.setCourseName(course1.getCourseName());
		}
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
	public Result<UserCourse> add(@RequestBody UserCourse userCourse) {
		Result<UserCourse> result = new Result<UserCourse>();
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
	public Result<UserCourse> edit(@RequestBody UserCourse userCourse) {
		Result<UserCourse> result = new Result<UserCourse>();
		UserCourse userCourseEntity = userCourseService.getById(userCourse.getId());
		//修改预约课程状态
		String uid = userCourse.getUid();
		User user = userService.getById(uid);
		user.setReserveCourse(1);
		userService.updateById(user);
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
