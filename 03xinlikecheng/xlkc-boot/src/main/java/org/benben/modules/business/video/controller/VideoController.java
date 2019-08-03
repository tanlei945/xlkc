package org.benben.modules.business.video.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.bbuservideo.entity.BbUserVideo;
import org.benben.modules.business.bbuservideo.entity.BbVideoType;
import org.benben.modules.business.bbuservideo.service.IBbUserVideoService;
import org.benben.modules.business.bbuservideo.service.IBbVideoTypeService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
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
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/video/video")
@Slf4j
public class VideoController {
	@Autowired
	private IVideoService videoService;

	@Autowired
	private IBbVideoTypeService videoTypeService;

	@Autowired
	private IBbUserVideoService bbUserVideoService;

	@Autowired
	private IUserService userService;

	/**
	  * 分页列表查询
	 * @param video
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
//	@ApiOperation(value = "xainsh", tags = "ton")
	public Result<IPage<Video>> queryPageList(Video video,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Video>> result = new Result<IPage<Video>>();
		QueryWrapper<Video> queryWrapper = QueryGenerator.initQueryWrapper(video, req.getParameterMap());
		Page<Video> page = new Page<Video>(pageNo, pageSize);
		IPage<Video> pageList = videoService.page(page, queryWrapper);
//		pageList.getRecords().get(0).
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	public static void main(String[] args) {

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




			//首先添加到是否收费视频上面
			BbVideoType videoType = new BbVideoType();
			if (video.getVideoClass() == 1) {
				videoType.setInvitecode(video.getInvitecode());
				videoType.setVideoClass(video.getVideoClass());
				videoType.setVideoType(video.getVideoType());
				videoTypeService.save(videoType);
			}
			if (StringUtils.isNotBlank(videoType.getId())) {
				video.setParentid(videoType.getId());
			}
			videoService.save(video);
			//添加的是收费视频的时候全部添加进我的收费视频当中
			//查询全部的用户信息
			List<String> list = userService.queryAll();
			if(video.getVideoClass() == 1 && StringUtils.isNotBlank(videoType.getId())) {
				for (String s : list) {
					BbUserVideo userVideo = new BbUserVideo();
					userVideo.setVid(videoType.getId());
					userVideo.setUid(s);
					userVideo.setState(1);
					bbUserVideoService.save(userVideo);
				}
			}
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	@PostMapping(value = "/adds")
//	@ApiOperation(value = "添加信息",tags = "实验接口 ")
	public Result<Video> adds(@RequestBody Video video) {
		Result<Video> result = new Result<Video>();
		try {

			if (StringUtils.isNotBlank(video.getVideoType())) {
				//查询书籍名称是否存在
				BbVideoType bbVideoType1 = videoTypeService.queryByName(video.getVideoType());
				if (bbVideoType1 == null) {
					//添加书籍名称
					BbVideoType bbVideoType = new BbVideoType();
					bbVideoType.setVideoType(video.getVideoType());
					bbVideoType.setVideoClass(video.getVideoClass());
					bbVideoType.setInvitecode(video.getInvitecode());
					videoTypeService.save(bbVideoType);

					BbVideoType bbVideoType2 = videoTypeService.queryByName(video.getVideoType());
					String vtid = bbVideoType2.getId();
					video.setParentid(vtid);
					List<String> uids = userService.queryAll();
					for (int i = 0; i < uids.size(); i++) {
						BbUserVideo bbUserVideo = new BbUserVideo();
						bbUserVideo.setVid(vtid);
						bbUserVideo.setUid(uids.get(i));
						bbUserVideo.setState(1);
						bbUserVideoService.save(bbUserVideo);
					}
				} else {
					video.setParentid(bbVideoType1.getId());
				}
			}


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
      mv.addObject(NormalExcelConstants.FILE_NAME, "对视频的管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Video.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("对视频的管理列表数据", "导出人:Jeecg", "导出信息"));
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
