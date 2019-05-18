package org.benben.modules.business.course.controller;

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
import org.benben.common.util.UUIDGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.CourseDTO;
import org.benben.modules.business.course.service.ICourseDTOService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.course.vo.CourseQueryVo;
import org.benben.modules.business.coursetype.entity.CourseType;
import org.benben.modules.business.coursetype.service.ICourseTypeService;
import org.benben.modules.business.information.entity.InformationDTO;
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
 * @Description: 课程模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/course")
@Slf4j
@Api(tags = "课程模块")
public class CourseDTOController {
	@Autowired
	private ICourseDTOService courseDTOService;

	@Autowired
	private ICourseTypeService courseTypeService;


	
	/**
	  * 分页列表查询
	 * @param courseDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "查询课程列表", tags = {"课程模块"}, notes = "查询课程列表")
	public Result<IPage<CourseDTO>> queryPageList(CourseDTO courseDTO,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<CourseDTO>> result = new Result<IPage<CourseDTO>>();
		QueryWrapper<CourseDTO> queryWrapper = QueryGenerator.initQueryWrapper(courseDTO, req.getParameterMap());
		Page<CourseDTO> page = new Page<CourseDTO>(pageNo, pageSize);
		IPage<CourseDTO> pageList = courseDTOService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param courseDTO
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "新增课程", tags = {"课程模块"}, notes = "新增课程")
	public Result<CourseDTO> add(@RequestBody CourseDTO courseDTO) {
		Result<CourseDTO> result = new Result<CourseDTO>();
		try {
			courseDTO.setId(UUIDGenerator.generate());
			courseDTOService.save(courseDTO);
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
	 * @param courseDTO
	 * @return
	 */
	@PutMapping(value = "/edit")
	@ApiOperation(value = "修改课程", tags = {"课程模块"}, notes = "修改课程")
	public Result<CourseDTO> edit(@RequestBody CourseDTO courseDTO) {
		Result<CourseDTO> result = new Result<CourseDTO>();
		CourseDTO courseDTOEntity = courseDTOService.getById(courseDTO.getId());
		if(courseDTOEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseDTOService.updateById(courseDTO);
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
	@ApiOperation(value = "删除课程", tags = {"课程模块"}, notes = "删除课程")
	public Result<CourseDTO> delete(@RequestParam(name="id",required=true) String id) {
		Result<CourseDTO> result = new Result<CourseDTO>();
		CourseDTO courseDTO = courseDTOService.getById(id);
		if(courseDTO==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseDTOService.removeById(id);
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
	@ApiOperation(value = "批量删除课程", tags = {"课程模块"}, notes = "批量删除课程")
	public Result<CourseDTO> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CourseDTO> result = new Result<CourseDTO>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseDTOService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "查询课程详情", tags = {"课程模块"}, notes = "查询课程详情")
	public Result<CourseDTO> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CourseDTO> result = new Result<CourseDTO>();
		CourseDTO courseDTO = courseDTOService.getById(id);
		CourseType courseType = courseTypeService.getById(courseDTO.getCourseType());
		courseDTO.setCourseTypeName(courseType.getName());
		if(courseDTO==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseDTO);
			result.setSuccess(true);
		}
		return result;
	}


	 /**
	  * 搜索课程列表
	  * @param courseQueryVo
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @GetMapping(value = "/search")
	 @ApiOperation(value = "搜索课程列表", tags = {"课程模块"}, notes = "搜索课程列表")
	 public Result<IPage<CourseDTO>> searchCourse(CourseQueryVo courseQueryVo,
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Result<IPage<CourseDTO>> result = new Result<IPage<CourseDTO>>();
		 IPage<CourseDTO> pageList = courseDTOService.searchNotice(pageNo, pageSize, courseQueryVo);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }

	 /**
	  * 课程报名
	  * @param courseId
	  * @return
	  */
	 @GetMapping(value = "/update/num")
	 @ApiOperation(value = "课程报名", tags = {"课程模块"}, notes = "课程报名")
	 public Result<CourseDTO> updateCourseNum(String courseId, InformationDTO informationDTO){
		 // TODO: 2019/5/18 0018 微信支付相关功能
	 	return null;
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
      QueryWrapper<CourseDTO> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              CourseDTO courseDTO = JSON.parseObject(deString, CourseDTO.class);
              queryWrapper = QueryGenerator.initQueryWrapper(courseDTO, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<CourseDTO> pageList = courseDTOService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "课程模块列表");
      mv.addObject(NormalExcelConstants.CLASS, CourseDTO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("课程模块列表数据", "导出人:Jeecg", "导出信息"));
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
              List<CourseDTO> listCourseDTOs = ExcelImportUtil.importExcel(file.getInputStream(), CourseDTO.class, params);
              for (CourseDTO courseDTOExcel : listCourseDTOs) {
                  courseDTOService.save(courseDTOExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCourseDTOs.size());
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
