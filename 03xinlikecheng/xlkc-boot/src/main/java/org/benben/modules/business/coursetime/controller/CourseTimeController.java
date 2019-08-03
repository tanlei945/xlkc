package org.benben.modules.business.coursetime.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.coursetime.entity.CourseTime;
import org.benben.modules.business.coursetime.service.ICourseTimeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.coursetimerelevan.entity.CourseTimeRelevan;
import org.benben.modules.business.coursetimerelevan.service.ICourseTimeRelevanService;
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
 * @Description: 上课时间管理
 * @author： jeecg-boot
 * @date：   2019-06-21
 * @version： V1.0
 */
@RestController
@RequestMapping("/coursetime/courseTime")
@Slf4j
public class CourseTimeController {
	@Autowired
	private ICourseTimeService courseTimeService;

	@Autowired
	private ICourseTimeRelevanService courseTimeRelevanService;
	
	/**
	  * 分页列表查询
	 * @param courseTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CourseTime>> queryPageList(CourseTime courseTime,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CourseTime>> result = new Result<IPage<CourseTime>>();
		QueryWrapper<CourseTime> queryWrapper = QueryGenerator.initQueryWrapper(courseTime, req.getParameterMap());
		queryWrapper.isNull("parent_id");
		Page<CourseTime> page = new Page<CourseTime>(pageNo, pageSize);
		IPage<CourseTime> pageList = courseTimeService.page(page, queryWrapper);

		List<CourseTime> courseTimes = pageList.getRecords();
		//得到课程名称，通过关联表的id得到课程的相关信息
		for (CourseTime time : courseTimes) {
			Course course= courseTimeService.getCourse(time.getId());
			time.setCourseName(course.getCourseName());
			time.setCourseId(course.getId());
		}
		for (CourseTime coursetime : courseTimes) {
			List<Integer> courseTimeList = courseTimeService.queryById(coursetime.getId());
			coursetime.setDays(courseTimeList);
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param courseTime
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<CourseTime> add(@RequestBody CourseTime courseTime) {
		Result<CourseTime> result = new Result<CourseTime>();
		if (courseTime.getDate() != null) {
			List<Integer> days = courseTime.getDays();

			courseTimeService.save(courseTime);
			for (Integer day : days) {
				CourseTime courseTime2 = new CourseTime();
				courseTime2.setDay(day);
				courseTime2.setParentId(courseTime.getId());
				courseTimeService.save(courseTime2);

			}
			CourseTimeRelevan courseTimeRelevan = new CourseTimeRelevan();
			courseTimeRelevan.setCtid(courseTime.getId());
			courseTimeRelevan.setCid(courseTime.getCourseId());
			courseTimeRelevanService.save(courseTimeRelevan);
			result.success("添加成功！");
			return result;
		}
		//保存进入关系表
		try {

			courseTimeService.save(courseTime);
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
	 * @param courseTime
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CourseTime> edit(@RequestBody CourseTime courseTime) {
		Result<CourseTime> result = new Result<CourseTime>();
		if (courseTime.getDate() != null) {

			List<Integer> days = courseTime.getDays();

			//判断该日期下面是否有日期
			List<CourseTime> childTime = courseTimeService.getChildTime(courseTime.getId());
			if (childTime.size() == 0) {

				for (Integer day : days) {
					CourseTime courseTime2 = new CourseTime();
					courseTime2.setDay(day);
					courseTime2.setParentId(courseTime.getId());
					courseTimeService.save(courseTime2);

				}
			} else {
				for (CourseTime time : childTime) {
					courseTimeService.removeById(time.getId());
				}
				for (Integer day : days) {
					CourseTime courseTime2 = new CourseTime();
					courseTime2.setDay(day);
					courseTime2.setParentId(courseTime.getId());
					courseTimeService.save(courseTime2);

				}
			}

			courseTimeService.updateById(courseTime);

			//假如时间修改为别的课程，id同时改变，通过找寻别的id

			result.success("修改成功");
			return result;
		}
		CourseTime courseTimeEntity = courseTimeService.getById(courseTime.getId());
		if(courseTimeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseTimeService.updateById(courseTime);
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
	public Result<CourseTime> delete(@RequestParam(name="id",required=true) String id) {
		Result<CourseTime> result = new Result<CourseTime>();
		CourseTime courseTime = courseTimeService.getById(id);
		if(courseTime==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseTimeService.removeById(id);
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
	public Result<CourseTime> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CourseTime> result = new Result<CourseTime>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseTimeService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById/{id}")
	public Result<CourseTime> queryById(@PathVariable String id) {
		Result<CourseTime> result = new Result<CourseTime>();
		CourseTime courseTime = courseTimeService.getById(id);
		if(courseTime==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseTime);
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
      QueryWrapper<CourseTime> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CourseTime courseTime = JSON.parseObject(deString, CourseTime.class);
              queryWrapper = QueryGenerator.initQueryWrapper(courseTime, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CourseTime> pageList = courseTimeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "上课时间管理列表");
      mv.addObject(NormalExcelConstants.CLASS, CourseTime.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("上课时间管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CourseTime> listCourseTimes = ExcelImportUtil.importExcel(file.getInputStream(), CourseTime.class, params);
              for (CourseTime courseTimeExcel : listCourseTimes) {
                  courseTimeService.save(courseTimeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCourseTimes.size());
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
