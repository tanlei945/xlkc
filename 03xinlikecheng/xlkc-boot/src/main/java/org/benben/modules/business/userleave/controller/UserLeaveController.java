package org.benben.modules.business.userleave.controller;

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
import org.benben.modules.business.userleave.entity.UserLeave;
import org.benben.modules.business.userleave.service.IUserLeaveService;

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
 * @Description: 用户请假
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@RestController
@RequestMapping("/userleave/userLeave")
@Slf4j
public class UserLeaveController {
	@Autowired
	private IUserLeaveService userLeaveService;
	
	/**
	  * 分页列表查询
	 * @param userLeave
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserLeave>> queryPageList(UserLeave userLeave,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserLeave>> result = new Result<IPage<UserLeave>>();
		QueryWrapper<UserLeave> queryWrapper = QueryGenerator.initQueryWrapper(userLeave, req.getParameterMap());
		Page<UserLeave> page = new Page<UserLeave>(pageNo, pageSize);
		IPage<UserLeave> pageList = userLeaveService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userLeave
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserLeave> add(@RequestBody UserLeave userLeave) {
		Result<UserLeave> result = new Result<UserLeave>();
		try {
			userLeaveService.save(userLeave);
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
	 * @param userLeave
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserLeave> edit(@RequestBody UserLeave userLeave) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeaveEntity = userLeaveService.getById(userLeave.getId());
		if(userLeaveEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userLeaveService.updateById(userLeave);
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
	public Result<UserLeave> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeave = userLeaveService.getById(id);
		if(userLeave==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userLeaveService.removeById(id);
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
	public Result<UserLeave> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserLeave> result = new Result<UserLeave>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userLeaveService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserLeave> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserLeave> result = new Result<UserLeave>();
		UserLeave userLeave = userLeaveService.getById(id);
		if(userLeave==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userLeave);
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
      QueryWrapper<UserLeave> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserLeave userLeave = JSON.parseObject(deString, UserLeave.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userLeave, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserLeave> pageList = userLeaveService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户请假列表");
      mv.addObject(NormalExcelConstants.CLASS, UserLeave.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户请假列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserLeave> listUserLeaves = ExcelImportUtil.importExcel(file.getInputStream(), UserLeave.class, params);
              for (UserLeave userLeaveExcel : listUserLeaves) {
                  userLeaveService.save(userLeaveExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserLeaves.size());
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
