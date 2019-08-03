package org.benben.modules.business.courserefund.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courserefund.entity.CourseRefund;
import org.benben.modules.business.courserefund.service.ICourseRefundService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
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
 * @Description: 课程退款
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@RestController
@RequestMapping("/courserefund/courseRefund")
@Slf4j
public class CourseRefundController {
	@Autowired
	private ICourseRefundService courseRefundService;
	@Autowired
	private ICourseService courseService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserCourseService userCourseervice;

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
		List<CourseRefund> records = pageList.getRecords();
		for (CourseRefund record : records) {
			User user = userService.getById(record.getUid());
			record.setNickName(user.getNickname());
			Course course = courseService.getById(record.getCourseId());
			record.setCourseName(course.getCourseName());
			UserCourse userCourse = userCourseervice.queryByCourse(user.getId(), course.getId());
			record.setStatus(userCourse.getStatus());
		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param courseRefund
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<CourseRefund> add(@RequestBody CourseRefund courseRefund) {
		Result<CourseRefund> result = new Result<CourseRefund>();
		try {
			courseRefundService.save(courseRefund);
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
	 * @param courseRefund
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CourseRefund> edit(@RequestBody CourseRefund courseRefund) {
		Result<CourseRefund> result = new Result<CourseRefund>();
		CourseRefund courseRefundEntity = courseRefundService.getById(courseRefund.getId());

		//修改课程状态
		UserCourse userCourse = userCourseervice.queryByCourse(courseRefund.getUid(), courseRefund.getCourseId());
		userCourse.setStatus(courseRefund.getStatus());
		userCourseervice.updateById(userCourse);

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
	public Result<CourseRefund> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CourseRefund> result = new Result<CourseRefund>();
		CourseRefund courseRefund = courseRefundService.getById(id);
		if(courseRefund==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseRefund);
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
