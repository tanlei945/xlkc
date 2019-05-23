package org.benben.modules.business.usercollect.controller;

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
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.business.usercollect.service.IUserCollectService;

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
 * @Description: 用户收藏表管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/usercollect/userCollect")
@Slf4j
public class UserCollectController {
	@Autowired
	private IUserCollectService userCollectService;
	
	/**
	  * 分页列表查询
	 * @param userCollect
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserCollect>> queryPageList(UserCollect userCollect,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserCollect>> result = new Result<IPage<UserCollect>>();
		QueryWrapper<UserCollect> queryWrapper = QueryGenerator.initQueryWrapper(userCollect, req.getParameterMap());
		Page<UserCollect> page = new Page<UserCollect>(pageNo, pageSize);
		IPage<UserCollect> pageList = userCollectService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userCollect
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserCollect> add(@RequestBody UserCollect userCollect) {
		Result<UserCollect> result = new Result<UserCollect>();
		try {
			userCollectService.save(userCollect);
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
	 * @param userCollect
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserCollect> edit(@RequestBody UserCollect userCollect) {
		Result<UserCollect> result = new Result<UserCollect>();
		UserCollect userCollectEntity = userCollectService.getById(userCollect.getId());
		if(userCollectEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userCollectService.updateById(userCollect);
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
	public Result<UserCollect> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserCollect> result = new Result<UserCollect>();
		UserCollect userCollect = userCollectService.getById(id);
		if(userCollect==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userCollectService.removeById(id);
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
	public Result<UserCollect> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserCollect> result = new Result<UserCollect>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userCollectService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserCollect> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserCollect> result = new Result<UserCollect>();
		UserCollect userCollect = userCollectService.getById(id);
		if(userCollect==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userCollect);
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
      QueryWrapper<UserCollect> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserCollect userCollect = JSON.parseObject(deString, UserCollect.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userCollect, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserCollect> pageList = userCollectService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户收藏表管理列表");
      mv.addObject(NormalExcelConstants.CLASS, UserCollect.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户收藏表管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserCollect> listUserCollects = ExcelImportUtil.importExcel(file.getInputStream(), UserCollect.class, params);
              for (UserCollect userCollectExcel : listUserCollects) {
                  userCollectService.save(userCollectExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserCollects.size());
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
