package org.benben.modules.business.courseassistant.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseassistant.entity.CourseAssistant;
import org.benben.modules.business.courseassistant.service.ICourseAssistantService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.courseassistant.vo.CourseAssistantVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Title: Controller
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
@RestController
@RequestMapping("/courseassistant/courseAssistant")
@Slf4j
public class CourseAssistantController {
	@Autowired
	private ICourseAssistantService courseAssistantService;

	@Autowired
	private ICourseService courseService;
	
	/**
	  * 分页列表查询
	 * @param courseAssistant
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CourseAssistant>> queryPageList(CourseAssistant courseAssistant,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CourseAssistant>> result = new Result<IPage<CourseAssistant>>();
		QueryWrapper<CourseAssistant> queryWrapper = QueryGenerator.initQueryWrapper(courseAssistant, req.getParameterMap());
		Page<CourseAssistant> page = new Page<CourseAssistant>(pageNo, pageSize);
		IPage<CourseAssistant> pageList = courseAssistantService.page(page, queryWrapper);

		List<CourseAssistant> records = pageList.getRecords();
		for (int i = 0; i < records.size(); i++) {
			Course course = courseService.getById(records.get(i).getCourseId());
			records.get(i).setCourseName(course.getCourseName());
		}
		pageList.setRecords(records);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param courseAssistant
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<CourseAssistant> add(@RequestBody CourseAssistant courseAssistant) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		try {
			courseAssistantService.save(courseAssistant);
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
	 * @param courseAssistant
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CourseAssistant> edit(@RequestBody CourseAssistant courseAssistant) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistantEntity = courseAssistantService.getById(courseAssistant.getId());
		if(courseAssistantEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseAssistantService.updateById(courseAssistant);
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
	public Result<CourseAssistant> delete(@RequestParam(name="id",required=true) String id) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistant = courseAssistantService.getById(id);
		if(courseAssistant==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseAssistantService.removeById(id);
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
	public Result<CourseAssistant> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseAssistantService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<CourseAssistant> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistant = courseAssistantService.getById(id);
		if(courseAssistant==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseAssistant);
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
      QueryWrapper<CourseAssistant> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CourseAssistant courseAssistant = JSON.parseObject(deString, CourseAssistant.class);
              queryWrapper = QueryGenerator.initQueryWrapper(courseAssistant, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CourseAssistant> pageList = courseAssistantService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "关于助教的申请列表");
      mv.addObject(NormalExcelConstants.CLASS, CourseAssistant.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("关于助教的申请列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CourseAssistant> listCourseAssistants = ExcelImportUtil.importExcel(file.getInputStream(), CourseAssistant.class, params);
              for (CourseAssistant courseAssistantExcel : listCourseAssistants) {
                  courseAssistantService.save(courseAssistantExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCourseAssistants.size());
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
