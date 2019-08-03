package org.benben.modules.business.courseperinformation.controller;

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
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;

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
 * @Description: 预约课程个人信息管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@RestController
@RequestMapping("/courseperinformation/coursePerInformation")
@Slf4j
public class CoursePerInformationController {
	@Autowired
	private ICoursePerInformationService coursePerInformationService;
	
	/**
	  * 分页列表查询
	 * @param coursePerInformation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CoursePerInformation>> queryPageList(CoursePerInformation coursePerInformation,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CoursePerInformation>> result = new Result<IPage<CoursePerInformation>>();
		QueryWrapper<CoursePerInformation> queryWrapper = QueryGenerator.initQueryWrapper(coursePerInformation, req.getParameterMap());
		Page<CoursePerInformation> page = new Page<CoursePerInformation>(pageNo, pageSize);
		IPage<CoursePerInformation> pageList = coursePerInformationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param coursePerInformation
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<CoursePerInformation> add(@RequestBody CoursePerInformation coursePerInformation) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		try {
			coursePerInformationService.save(coursePerInformation);
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
	 * @param coursePerInformation
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CoursePerInformation> edit(@RequestBody CoursePerInformation coursePerInformation) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformationEntity = coursePerInformationService.getById(coursePerInformation.getId());
		if(coursePerInformationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = coursePerInformationService.updateById(coursePerInformation);
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
	public Result<CoursePerInformation> delete(@RequestParam(name="id",required=true) String id) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformation = coursePerInformationService.getById(id);
		if(coursePerInformation==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = coursePerInformationService.removeById(id);
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
	public Result<CoursePerInformation> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.coursePerInformationService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<CoursePerInformation> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformation = coursePerInformationService.getById(id);
		if(coursePerInformation==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(coursePerInformation);
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
      QueryWrapper<CoursePerInformation> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CoursePerInformation coursePerInformation = JSON.parseObject(deString, CoursePerInformation.class);
              queryWrapper = QueryGenerator.initQueryWrapper(coursePerInformation, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CoursePerInformation> pageList = coursePerInformationService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "预约课程个人信息管理列表");
      mv.addObject(NormalExcelConstants.CLASS, CoursePerInformation.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("预约课程个人信息管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CoursePerInformation> listCoursePerInformations = ExcelImportUtil.importExcel(file.getInputStream(), CoursePerInformation.class, params);
              for (CoursePerInformation coursePerInformationExcel : listCoursePerInformations) {
                  coursePerInformationService.save(coursePerInformationExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCoursePerInformations.size());
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
