package org.benben.modules.business.notice.controller;

import java.util.Arrays;
import java.util.Date;
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
import org.benben.modules.business.notice.entity.NoticeDTO;
import org.benben.modules.business.notice.service.INoticeDTOService;

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
 * @Description: 通知模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/notice")
@Slf4j
@Api(tags = {
		"通知模块"
})
public class NoticeDTOController {
	@Autowired
	private INoticeDTOService noticeDTOService;
	
	/**
	  * 分页列表查询
	 * @param noticeDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "查询通知列表", tags = {"通知模块"}, notes = "查询通知列表")
	public Result<IPage<NoticeDTO>> queryPageList(NoticeDTO noticeDTO,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<NoticeDTO>> result = new Result<IPage<NoticeDTO>>();
		QueryWrapper<NoticeDTO> queryWrapper = QueryGenerator.initQueryWrapper(noticeDTO, req.getParameterMap());
		Page<NoticeDTO> page = new Page<NoticeDTO>(pageNo, pageSize);
		IPage<NoticeDTO> pageList = noticeDTOService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param noticeDTO
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "新增通知", tags = {"通知模块"}, notes = "新增通知")
	public Result<NoticeDTO> add(@RequestBody NoticeDTO noticeDTO) {
		Result<NoticeDTO> result = new Result<NoticeDTO>();
		try {
			noticeDTO.setId(UUIDGenerator.generate());
			noticeDTO.setCreateTime(new Date());
			noticeDTO.setUpdateTime(new Date());
			noticeDTOService.save(noticeDTO);
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
	 * @param noticeDTO
	 * @return
	 */
	@PutMapping(value = "/edit")
	@ApiOperation(value = "修改通知", tags = {"通知模块"}, notes = "修改通知")
	public Result<NoticeDTO> edit(@RequestBody NoticeDTO noticeDTO) {
		Result<NoticeDTO> result = new Result<NoticeDTO>();
		NoticeDTO noticeDTOEntity = noticeDTOService.getById(noticeDTO.getId());
		if(noticeDTOEntity==null) {
			result.error500("未找到对应实体");
		}else {
			noticeDTO.setUpdateTime(new Date());
			boolean ok = noticeDTOService.updateById(noticeDTO);
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
	@ApiOperation(value = "删除通知", tags = {"通知模块"}, notes = "删除通知")
	public Result<NoticeDTO> delete(@RequestParam(name="id",required=true) String id) {
		Result<NoticeDTO> result = new Result<NoticeDTO>();
		NoticeDTO noticeDTO = noticeDTOService.getById(id);
		if(noticeDTO==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = noticeDTOService.removeById(id);
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
	@ApiOperation(value = "批量删除通知", tags = {"通知模块"}, notes = "批量删除通知")
	public Result<NoticeDTO> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<NoticeDTO> result = new Result<NoticeDTO>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.noticeDTOService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "查询通知详情", tags = {"通知模块"}, notes = "查询通知详情")
	public Result<NoticeDTO> queryById(@RequestParam(name="id",required=true) String id) {
		Result<NoticeDTO> result = new Result<NoticeDTO>();
		NoticeDTO noticeDTO = noticeDTOService.getById(id);
		if(noticeDTO==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(noticeDTO);
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
      QueryWrapper<NoticeDTO> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              NoticeDTO noticeDTO = JSON.parseObject(deString, NoticeDTO.class);
              queryWrapper = QueryGenerator.initQueryWrapper(noticeDTO, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<NoticeDTO> pageList = noticeDTOService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "通知模块列表");
      mv.addObject(NormalExcelConstants.CLASS, NoticeDTO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("通知模块列表数据", "导出人:Jeecg", "导出信息"));
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
              List<NoticeDTO> listNoticeDTOs = ExcelImportUtil.importExcel(file.getInputStream(), NoticeDTO.class, params);
              for (NoticeDTO noticeDTOExcel : listNoticeDTOs) {
                  noticeDTOService.save(noticeDTOExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listNoticeDTOs.size());
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
