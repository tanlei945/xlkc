package org.benben.modules.business.userbuke.controller;

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
import org.benben.modules.business.userbuke.entity.UserBuke;
import org.benben.modules.business.userbuke.service.IUserBukeService;

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
 * @Description: 用户补课的管理
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@RestController
@RequestMapping("/userbuke/userBuke")
@Slf4j
public class UserBukeController {
	@Autowired
	private IUserBukeService userBukeService;
	
	/**
	  * 分页列表查询
	 * @param userBuke
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserBuke>> queryPageList(UserBuke userBuke,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserBuke>> result = new Result<IPage<UserBuke>>();
		QueryWrapper<UserBuke> queryWrapper = QueryGenerator.initQueryWrapper(userBuke, req.getParameterMap());
		Page<UserBuke> page = new Page<UserBuke>(pageNo, pageSize);
		IPage<UserBuke> pageList = userBukeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userBuke
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserBuke> add(@RequestBody UserBuke userBuke) {
		Result<UserBuke> result = new Result<UserBuke>();
		try {
			userBukeService.save(userBuke);
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
	 * @param userBuke
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserBuke> edit(@RequestBody UserBuke userBuke) {
		Result<UserBuke> result = new Result<UserBuke>();
		UserBuke userBukeEntity = userBukeService.getById(userBuke.getId());
		if(userBukeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userBukeService.updateById(userBuke);
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
	public Result<UserBuke> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserBuke> result = new Result<UserBuke>();
		UserBuke userBuke = userBukeService.getById(id);
		if(userBuke==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userBukeService.removeById(id);
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
	public Result<UserBuke> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserBuke> result = new Result<UserBuke>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userBukeService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserBuke> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserBuke> result = new Result<UserBuke>();
		UserBuke userBuke = userBukeService.getById(id);
		if(userBuke==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userBuke);
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
      QueryWrapper<UserBuke> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserBuke userBuke = JSON.parseObject(deString, UserBuke.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userBuke, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserBuke> pageList = userBukeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户补课的管理列表");
      mv.addObject(NormalExcelConstants.CLASS, UserBuke.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户补课的管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserBuke> listUserBukes = ExcelImportUtil.importExcel(file.getInputStream(), UserBuke.class, params);
              for (UserBuke userBukeExcel : listUserBukes) {
                  userBukeService.save(userBukeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserBukes.size());
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
