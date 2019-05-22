package org.benben.modules.business.userfeedback.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.userfeedback.entity.UserFeedback;
import org.benben.modules.business.userfeedback.service.IUserFeedbackService;

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
 * @Description: 用户反馈表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userFeedback")
@Slf4j
public class UserFeedbackController {
	@Autowired
	private IUserFeedbackService userFeedbackService;
	
	/**
	  * 分页列表查询
	 * @param userFeedback
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserFeedback>> queryPageList(UserFeedback userFeedback,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserFeedback>> result = new Result<IPage<UserFeedback>>();
		QueryWrapper<UserFeedback> queryWrapper = QueryGenerator.initQueryWrapper(userFeedback, req.getParameterMap());
		Page<UserFeedback> page = new Page<UserFeedback>(pageNo, pageSize);
		IPage<UserFeedback> pageList = userFeedbackService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userFeedback
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加用户反馈数据", tags = "用户接口", notes = "添加用户反馈数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userFeedback.uid", value = "用户相关联id"),
			@ApiImplicitParam(name = "userFeedback.questionType", value = "问题类型  1下载/加载问题 2/付费问题 3/课程问题 4/体验问题"),
			@ApiImplicitParam(name = "userFeedback.comment", value = "反馈描述"),
			@ApiImplicitParam(name = "userFeedback.contact", value = "反馈图片"),
	})
	public Result<UserFeedback> add(@RequestBody UserFeedback userFeedback) {
		Result<UserFeedback> result = new Result<UserFeedback>();
		try {
			userFeedbackService.save(userFeedback);
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
	 * @param userFeedback
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserFeedback> edit(@RequestBody UserFeedback userFeedback) {
		Result<UserFeedback> result = new Result<UserFeedback>();
		UserFeedback userFeedbackEntity = userFeedbackService.getById(userFeedback.getId());
		if(userFeedbackEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userFeedbackService.updateById(userFeedback);
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
	public Result<UserFeedback> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserFeedback> result = new Result<UserFeedback>();
		UserFeedback userFeedback = userFeedbackService.getById(id);
		if(userFeedback==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userFeedbackService.removeById(id);
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
	public Result<UserFeedback> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserFeedback> result = new Result<UserFeedback>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userFeedbackService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserFeedback> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserFeedback> result = new Result<UserFeedback>();
		UserFeedback userFeedback = userFeedbackService.getById(id);
		if(userFeedback==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userFeedback);
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
      QueryWrapper<UserFeedback> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserFeedback userFeedback = JSON.parseObject(deString, UserFeedback.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userFeedback, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserFeedback> pageList = userFeedbackService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户反馈表列表");
      mv.addObject(NormalExcelConstants.CLASS, UserFeedback.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户反馈表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserFeedback> listUserFeedbacks = ExcelImportUtil.importExcel(file.getInputStream(), UserFeedback.class, params);
              for (UserFeedback userFeedbackExcel : listUserFeedbacks) {
                  userFeedbackService.save(userFeedbackExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserFeedbacks.size());
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
