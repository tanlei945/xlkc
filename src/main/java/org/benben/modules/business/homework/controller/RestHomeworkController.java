package org.benben.modules.business.homework.controller;

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
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.homework.entity.Homework;
import org.benben.modules.business.homework.service.ICourseService;
import org.benben.modules.business.homework.service.IHomeworkService;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import org.w3c.dom.ls.LSInput;

/**
 * @Title: Controller
 * @Description: 我的功课管理
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/homework")
@Api(tags = "我的功课")
@Slf4j
public class RestHomeworkController {
	@Autowired
	private IHomeworkService homeworkService;

	@Autowired
	private ICourseService courseService;

	 /**
	  * 查询完成功课的课程信息名称
	  * @param courseId
	  * @param achieve
	  * @return
	  */
	@PostMapping("/queryByCourseIdAndAchieve")
	@ApiOperation(value = "查询完成功课的信息名称", tags = "我的功课", notes = "查询完成功课的信息名称")
	public RestResponseBean queryByCourseIdAndAchieve(@RequestParam String courseId, @RequestParam Integer achieve){
		if (courseId == null || achieve ==null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		String courseName = courseService.queryByCourseIdAndAchieve(courseId, achieve);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),courseName);
	}

	 /**
	  * 根据用户id和课程id查询我的全部功课
	  * @param uid
	  * @param courseId
	  * @return
	  */
	 @PostMapping(value = "/queryByUidAndCourseId")
	 @ApiOperation(value = "查询全部功课信息",tags = "我的功课", notes = "查询全部功课信息")
	public RestResponseBean queryHomework(@RequestParam String uid, @RequestParam String courseId){
		if (uid == null || courseId == null){
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		 Homework homework = homeworkService.queryByUidAndCourseId(uid, courseId);
		 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), homework);
	}

	/**
	  * 分页列表查询
	 * @param homework
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Homework>> queryPageList(Homework homework,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Homework>> result = new Result<IPage<Homework>>();
		QueryWrapper<Homework> queryWrapper = QueryGenerator.initQueryWrapper(homework, req.getParameterMap());
		Page<Homework> page = new Page<Homework>(pageNo, pageSize);
		IPage<Homework> pageList = homeworkService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param homework
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Homework> add(@RequestBody Homework homework) {
		Result<Homework> result = new Result<Homework>();
		try {
			homeworkService.save(homework);
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
	 * @param homework
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Homework> edit(@RequestBody Homework homework) {
		Result<Homework> result = new Result<Homework>();
		Homework homeworkEntity = homeworkService.getById(homework.getId());
		if(homeworkEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = homeworkService.updateById(homework);
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
	public Result<Homework> delete(@RequestParam(name="id",required=true) String id) {
		Result<Homework> result = new Result<Homework>();
		Homework homework = homeworkService.getById(id);
		if(homework==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = homeworkService.removeById(id);
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
	public Result<Homework> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Homework> result = new Result<Homework>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.homeworkService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "根据id查询功课的详细信息", tags = "我的功课", notes = "根据id查询功课的详细信息")
	public Result<Homework> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Homework> result = new Result<Homework>();
		Homework homework = homeworkService.getById(id);
		if(homework==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(homework);
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
      QueryWrapper<Homework> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Homework homework = JSON.parseObject(deString, Homework.class);
              queryWrapper = QueryGenerator.initQueryWrapper(homework, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Homework> pageList = homeworkService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "我的功课管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Homework.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("我的功课管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Homework> listHomeworks = ExcelImportUtil.importExcel(file.getInputStream(), Homework.class, params);
              for (Homework homeworkExcel : listHomeworks) {
                  homeworkService.save(homeworkExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listHomeworks.size());
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
