package org.benben.modules.business.notice.controller;

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
import org.benben.modules.business.notice.entity.Notice;
import org.benben.modules.business.notice.service.INoticeService;

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
 * @Description: 通知表管理
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/notice")
@Api(tags = "通知接口")
@Slf4j
public class NoticeController {
	@Autowired
	private INoticeService noticeService;

	@PostMapping("/queryNotice")
	@ApiOperation(value = "显示最新的通知", tags = "通知接口", notes = "显示最新的通知")
	public RestResponseBean queryNotice() {
		Notice notice = noticeService.queryNotice();
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),notice);
	}
	
	/**
	  * 分页列表查询
	 * @param notice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Notice>> queryPageList(Notice notice,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Notice>> result = new Result<IPage<Notice>>();
		QueryWrapper<Notice> queryWrapper = QueryGenerator.initQueryWrapper(notice, req.getParameterMap());
		Page<Notice> page = new Page<Notice>(pageNo, pageSize);
		IPage<Notice> pageList = noticeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param notice
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Notice> add(@RequestBody Notice notice) {
		Result<Notice> result = new Result<Notice>();
		try {
			noticeService.save(notice);
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
	 * @param notice
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Notice> edit(@RequestBody Notice notice) {
		Result<Notice> result = new Result<Notice>();
		Notice noticeEntity = noticeService.getById(notice.getId());
		if(noticeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = noticeService.updateById(notice);
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
	public Result<Notice> delete(@RequestParam(name="id",required=true) String id) {
		Result<Notice> result = new Result<Notice>();
		Notice notice = noticeService.getById(id);
		if(notice==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = noticeService.removeById(id);
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
	public Result<Notice> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Notice> result = new Result<Notice>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.noticeService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Notice> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Notice> result = new Result<Notice>();
		Notice notice = noticeService.getById(id);
		if(notice==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(notice);
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
      QueryWrapper<Notice> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Notice notice = JSON.parseObject(deString, Notice.class);
              queryWrapper = QueryGenerator.initQueryWrapper(notice, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Notice> pageList = noticeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "通知表管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Notice.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("通知表管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Notice> listNotices = ExcelImportUtil.importExcel(file.getInputStream(), Notice.class, params);
              for (Notice noticeExcel : listNotices) {
                  noticeService.save(noticeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listNotices.size());
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
