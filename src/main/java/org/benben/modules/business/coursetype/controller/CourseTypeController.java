package org.benben.modules.business.coursetype.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.coursetype.entity.CourseType;
import org.benben.modules.business.coursetype.service.ICourseTypeService;

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
 * @Description: 课程类型
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/coursetype")
@Slf4j
@Api(tags = {"课程类型模块"})
public class CourseTypeController {
	@Autowired
	private ICourseTypeService courseTypeService;
	
	/**
	  * 分页列表查询
	 * @param courseType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(tags = {"课程类型模块"}, value = "查询课程类型列表", notes = "查询课程类型列表")
	public Result<IPage<CourseType>> queryPageList(CourseType courseType,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CourseType>> result = new Result<IPage<CourseType>>();
		QueryWrapper<CourseType> queryWrapper = QueryGenerator.initQueryWrapper(courseType, req.getParameterMap());
		Page<CourseType> page = new Page<CourseType>(pageNo, pageSize);
		IPage<CourseType> pageList = courseTypeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param courseType
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(tags = {"课程类型模块"}, value = "新增课程类型", notes = "新增课程类型")
	public Result<CourseType> add(@RequestBody CourseType courseType) {
		Result<CourseType> result = new Result<CourseType>();
		try {
			courseTypeService.save(courseType);
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
	 * @param courseType
	 * @return
	 */
	@PutMapping(value = "/edit")
	@ApiOperation(tags = {"课程类型模块"}, value = "修改课程类型", notes = "修改课程类型")
	public Result<CourseType> edit(@RequestBody CourseType courseType) {
		Result<CourseType> result = new Result<CourseType>();
		CourseType courseTypeEntity = courseTypeService.getById(courseType.getId());
		if(courseTypeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseTypeService.updateById(courseType);
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
	@ApiOperation(tags = {"课程类型模块"}, value = "删除课程类型", notes = "删除课程类型")
	public Result<CourseType> delete(@RequestParam(name="id",required=true) String id) {
		Result<CourseType> result = new Result<CourseType>();
		CourseType courseType = courseTypeService.getById(id);
		if(courseType==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseTypeService.removeById(id);
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
	@ApiOperation(tags = {"课程类型模块"}, value = "批量删除课程类型", notes = "批量删除课程类型")
	public Result<CourseType> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CourseType> result = new Result<CourseType>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseTypeService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(tags = {"课程类型模块"}, value = "查询课程详情", notes = "查询课程详情")
	public Result<CourseType> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CourseType> result = new Result<CourseType>();
		CourseType courseType = courseTypeService.getById(id);
		if(courseType==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseType);
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
      QueryWrapper<CourseType> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CourseType courseType = JSON.parseObject(deString, CourseType.class);
              queryWrapper = QueryGenerator.initQueryWrapper(courseType, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CourseType> pageList = courseTypeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "课程类型列表");
      mv.addObject(NormalExcelConstants.CLASS, CourseType.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("课程类型列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CourseType> listCourseTypes = ExcelImportUtil.importExcel(file.getInputStream(), CourseType.class, params);
              for (CourseType courseTypeExcel : listCourseTypes) {
                  courseTypeService.save(courseTypeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCourseTypes.size());
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
