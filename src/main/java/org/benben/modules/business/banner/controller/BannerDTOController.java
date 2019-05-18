package org.benben.modules.business.banner.controller;

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
import org.benben.modules.business.banner.entity.BannerDTO;
import org.benben.modules.business.banner.service.IBannerDTOService;

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
 * @Description: 轮播图模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/banner")
@Slf4j
@Api(tags = {
		"轮播图模块"
})
public class BannerDTOController {
	@Autowired
	private IBannerDTOService bannerDTOService;
	
	/**
	  * 分页列表查询
	 * @param bannerDTO
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "查询轮播图列表", tags = {"查询轮播图列表"}, notes = "查询轮播图列表")
	public Result<IPage<BannerDTO>> queryPageList(BannerDTO bannerDTO,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<BannerDTO>> result = new Result<IPage<BannerDTO>>();
		QueryWrapper<BannerDTO> queryWrapper = QueryGenerator.initQueryWrapper(bannerDTO, req.getParameterMap());
		Page<BannerDTO> page = new Page<BannerDTO>(pageNo, pageSize);
		IPage<BannerDTO> pageList = bannerDTOService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bannerDTO
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "新增轮播图", tags = {"新增轮播图"}, notes = "新增轮播图")
	public Result<BannerDTO> add(@RequestBody BannerDTO bannerDTO) {
		Result<BannerDTO> result = new Result<BannerDTO>();
		try {
			bannerDTO.setId(UUIDGenerator.generate());
			bannerDTOService.save(bannerDTO);
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
	 * @param bannerDTO
	 * @return
	 */
	@PutMapping(value = "/edit")
	@ApiOperation(value = "修改轮播图", tags = {"修改轮播图"}, notes = "修改轮播图")
	public Result<BannerDTO> edit(@RequestBody BannerDTO bannerDTO) {
		Result<BannerDTO> result = new Result<BannerDTO>();
		BannerDTO bannerDTOEntity = bannerDTOService.getById(bannerDTO.getId());
		if(bannerDTOEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bannerDTOService.updateById(bannerDTO);
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
	@ApiOperation(value = "删除轮播图", tags = {"删除轮播图"}, notes = "删除轮播图")
	public Result<BannerDTO> delete(@RequestParam(name="id",required=true) String id) {
		Result<BannerDTO> result = new Result<BannerDTO>();
		BannerDTO bannerDTO = bannerDTOService.getById(id);
		if(bannerDTO==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bannerDTOService.removeById(id);
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
	@ApiOperation(value = "批量删除轮播图", tags = {"批量删除轮播图"}, notes = "批量删除轮播图")
	public Result<BannerDTO> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<BannerDTO> result = new Result<BannerDTO>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bannerDTOService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "查询轮播图详情", tags = {"查询轮播图详情"}, notes = "查询轮播图详情·")
	public Result<BannerDTO> queryById(@RequestParam(name="id",required=true) String id) {
		Result<BannerDTO> result = new Result<BannerDTO>();
		BannerDTO bannerDTO = bannerDTOService.getById(id);
		if(bannerDTO==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bannerDTO);
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
      QueryWrapper<BannerDTO> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              BannerDTO bannerDTO = JSON.parseObject(deString, BannerDTO.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bannerDTO, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<BannerDTO> pageList = bannerDTOService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "轮播图模块列表");
      mv.addObject(NormalExcelConstants.CLASS, BannerDTO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("轮播图模块列表数据", "导出人:Jeecg", "导出信息"));
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
              List<BannerDTO> listBannerDTOs = ExcelImportUtil.importExcel(file.getInputStream(), BannerDTO.class, params);
              for (BannerDTO bannerDTOExcel : listBannerDTOs) {
                  bannerDTOService.save(bannerDTOExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listBannerDTOs.size());
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
