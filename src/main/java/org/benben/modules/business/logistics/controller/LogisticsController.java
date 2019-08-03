package org.benben.modules.business.logistics.controller;

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
import org.benben.modules.business.logistics.entity.Logistics;
import org.benben.modules.business.logistics.service.ILogisticsService;

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
 * @Description: 物流信息管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@RestController
@RequestMapping("/logistics/logistics")
@Slf4j
public class LogisticsController {
	@Autowired
	private ILogisticsService logisticsService;
	
	/**
	  * 分页列表查询
	 * @param logistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Logistics>> queryPageList(Logistics logistics,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Logistics>> result = new Result<IPage<Logistics>>();
		QueryWrapper<Logistics> queryWrapper = QueryGenerator.initQueryWrapper(logistics, req.getParameterMap());
		Page<Logistics> page = new Page<Logistics>(pageNo, pageSize);
		IPage<Logistics> pageList = logisticsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param logistics
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Logistics> add(@RequestBody Logistics logistics) {
		Result<Logistics> result = new Result<Logistics>();
		try {
			logisticsService.save(logistics);
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
	 * @param logistics
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Logistics> edit(@RequestBody Logistics logistics) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logisticsEntity = logisticsService.getById(logistics.getId());
		if(logisticsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = logisticsService.updateById(logistics);
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
	public Result<Logistics> delete(@RequestParam(name="id",required=true) String id) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logistics = logisticsService.getById(id);
		if(logistics==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = logisticsService.removeById(id);
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
	public Result<Logistics> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Logistics> result = new Result<Logistics>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.logisticsService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Logistics> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logistics = logisticsService.getById(id);
		if(logistics==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(logistics);
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
      QueryWrapper<Logistics> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Logistics logistics = JSON.parseObject(deString, Logistics.class);
              queryWrapper = QueryGenerator.initQueryWrapper(logistics, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Logistics> pageList = logisticsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "物流信息管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Logistics.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物流信息管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Logistics> listLogisticss = ExcelImportUtil.importExcel(file.getInputStream(), Logistics.class, params);
              for (Logistics logisticsExcel : listLogisticss) {
                  logisticsService.save(logisticsExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listLogisticss.size());
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
