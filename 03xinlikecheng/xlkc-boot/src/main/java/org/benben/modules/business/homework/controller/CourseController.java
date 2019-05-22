package org.benben.modules.business.homework.controller;

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
import org.benben.modules.business.homework.entity.Course;
import org.benben.modules.business.homework.service.ICourseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 关于课程的表
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/course")
@Slf4j
public class CourseController {
	@Autowired
	private ICourseService courseService;
	
	/**
	  * 分页列表查询
	 * @param course
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Course>> queryPageList(Course course,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Course>> result = new Result<IPage<Course>>();
		QueryWrapper<Course> queryWrapper = QueryGenerator.initQueryWrapper(course, req.getParameterMap());
		Page<Course> page = new Page<Course>(pageNo, pageSize);
		IPage<Course> pageList = courseService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param course
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Course> add(@RequestBody Course course) {
		Result<Course> result = new Result<Course>();
		try {
			courseService.save(course);
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
	 * @param course
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Course> edit(@RequestBody Course course) {
		Result<Course> result = new Result<Course>();
		Course courseEntity = courseService.getById(course.getId());
		if(courseEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseService.updateById(course);
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
	public Result<Course> delete(@RequestParam(name="id",required=true) String id) {
		Result<Course> result = new Result<Course>();
		Course course = courseService.getById(id);
		if(course==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseService.removeById(id);
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
	public Result<Course> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Course> result = new Result<Course>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Course> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Course> result = new Result<Course>();
		Course course = courseService.getById(id);
		if(course==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(course);
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
      QueryWrapper<Course> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Course course = JSON.parseObject(deString, Course.class);
              queryWrapper = QueryGenerator.initQueryWrapper(course, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Course> pageList = courseService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "关于课程的表列表");
      mv.addObject(NormalExcelConstants.CLASS, Course.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("关于课程的表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Course> listCourses = ExcelImportUtil.importExcel(file.getInputStream(), Course.class, params);
              for (Course courseExcel : listCourses) {
                  courseService.save(courseExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCourses.size());
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
