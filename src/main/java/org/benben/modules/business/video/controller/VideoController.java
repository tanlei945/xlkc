package org.benben.modules.business.video.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.video.entity.Video;
import org.benben.modules.business.video.service.IVideoService;

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
 * @Description: 学习园地视频管理
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/video")
@Api(tags = "学习园地接口")
@Slf4j
public class VideoController {
	@Autowired
	private IVideoService videoService;


	 @PostMapping("/queryVideo")
	 @ApiOperation(value = "已经输入过邀请码视频接口", tags = "学习园地接口", notes = "已经输入过邀请码视频接口")
	 public RestResponseBean queryVideo() {

		 List<Video> videos = videoService.queryVideo();
		 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),videos);
	 }

	 @PostMapping("/videoByInvitecode")
	 @ApiOperation(value = "邀请码视频接口", tags = "学习园地接口", notes = "邀请码视频接口")
	 @ApiImplicitParams({
			 @ApiImplicitParam(name = "invitecode",value = "邀请码"),
	 })
	 public RestResponseBean queryByInvitecode(@RequestParam String invitecode) {
		if (!StringUtils.isNotBlank(invitecode)) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		 List<Video> videos = videoService.queryByTypeAndInvitecode(invitecode);
		 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),videos);
	 }

	@PostMapping("/videoClassify")
	@ApiOperation(value = "免费视频接口", tags = "学习园地接口", notes = "免费视频接口")
	public RestResponseBean query() {
		/*if (type == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}*/
		List<Video> videos = videoService.queryByType();
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),videos);
	}

	/**
	  * 分页列表查询
	 * @param video
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Video>> queryPageList(Video video,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Video>> result = new Result<IPage<Video>>();
		QueryWrapper<Video> queryWrapper = QueryGenerator.initQueryWrapper(video, req.getParameterMap());
		Page<Video> page = new Page<Video>(pageNo, pageSize);
		IPage<Video> pageList = videoService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param video
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Video> add(@RequestBody Video video) {
		Result<Video> result = new Result<Video>();
		try {
			videoService.save(video);
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
	 * @param video
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Video> edit(@RequestBody Video video) {
		Result<Video> result = new Result<Video>();
		Video videoEntity = videoService.getById(video.getId());
		if(videoEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = videoService.updateById(video);
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
	public Result<Video> delete(@RequestParam(name="id",required=true) String id) {
		Result<Video> result = new Result<Video>();
		Video video = videoService.getById(id);
		if(video==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = videoService.removeById(id);
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
	public Result<Video> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Video> result = new Result<Video>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.videoService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Video> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Video> result = new Result<Video>();
		Video video = videoService.getById(id);
		if(video==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(video);
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
      QueryWrapper<Video> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Video video = JSON.parseObject(deString, Video.class);
              queryWrapper = QueryGenerator.initQueryWrapper(video, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Video> pageList = videoService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "学习园地视频管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Video.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("学习园地视频管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Video> listVideos = ExcelImportUtil.importExcel(file.getInputStream(), Video.class, params);
              for (Video videoExcel : listVideos) {
                  videoService.save(videoExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listVideos.size());
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
