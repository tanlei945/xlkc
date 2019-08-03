package org.benben.modules.business.userpayevdence.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.benben.modules.business.userpayevdence.entity.UserPayEvdence;
import org.benben.modules.business.userpayevdence.service.IUserPayEvdenceService;

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
 * @Description: 支付凭证管理
 * @author： jeecg-boot
 * @date：   2019-06-19
 * @version： V1.0
 */
@RestController
@RequestMapping("/userpayevdence/userPayEvdence")
@Slf4j
public class UserPayEvdenceController {
	@Autowired
	private IUserPayEvdenceService userPayEvdenceService;

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;

	@Autowired
	private IUserCourseService userCourseService;
	
	/**
	  * 分页列表查询
	 * @param userPayEvdence
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserPayEvdence>> queryPageList(UserPayEvdence userPayEvdence,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserPayEvdence>> result = new Result<IPage<UserPayEvdence>>();
		QueryWrapper<UserPayEvdence> queryWrapper = QueryGenerator.initQueryWrapper(userPayEvdence, req.getParameterMap());
		Page<UserPayEvdence> page = new Page<UserPayEvdence>(pageNo, pageSize);
		IPage<UserPayEvdence> pageList = userPayEvdenceService.page(page, queryWrapper);
		List<UserPayEvdence> list = pageList.getRecords();
		for (UserPayEvdence payEvdence : list) {
			User user = userService.getById(payEvdence.getUid());
			payEvdence.setNickname(user.getNickname());
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userPayEvdence
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserPayEvdence> add(@RequestBody UserPayEvdence userPayEvdence) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		try {
			userPayEvdenceService.save(userPayEvdence);
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
	 * @param userPayEvdence
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserPayEvdence> edit(@RequestBody UserPayEvdence userPayEvdence) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		if (StringUtils.isBlank(userPayEvdence.getUserCourseId())) {
			Order order = orderService.getById(userPayEvdence.getOrderId());
			if (order == null ) {
				result.error500("未找到对应实体");
			}
			order.setState(userPayEvdence.getStatus());
			//根据订单id修改支付状态
			orderService.updateById(order);
		} else if (StringUtils.isBlank(userPayEvdence.getOrderId())) {
			UserCourse us = userCourseService.getById(userPayEvdence.getUserCourseId());
			if (us == null) {
				result.error500("未找到对应实体");
			}

		}

		UserPayEvdence userPayEvdenceEntity = userPayEvdenceService.getById(userPayEvdence.getId());
		if(userPayEvdenceEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userPayEvdenceService.updateById(userPayEvdence);
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
	public Result<UserPayEvdence> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		UserPayEvdence userPayEvdence = userPayEvdenceService.getById(id);
		if(userPayEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userPayEvdenceService.removeById(id);
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
	public Result<UserPayEvdence> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userPayEvdenceService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserPayEvdence> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		UserPayEvdence userPayEvdence = userPayEvdenceService.getById(id);
		if(userPayEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userPayEvdence);
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
      QueryWrapper<UserPayEvdence> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserPayEvdence userPayEvdence = JSON.parseObject(deString, UserPayEvdence.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userPayEvdence, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserPayEvdence> pageList = userPayEvdenceService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "支付凭证管理列表");
      mv.addObject(NormalExcelConstants.CLASS, UserPayEvdence.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("支付凭证管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserPayEvdence> listUserPayEvdences = ExcelImportUtil.importExcel(file.getInputStream(), UserPayEvdence.class, params);
              for (UserPayEvdence userPayEvdenceExcel : listUserPayEvdences) {
                  userPayEvdenceService.save(userPayEvdenceExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserPayEvdences.size());
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
