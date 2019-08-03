package org.benben.modules.business.recommendposts.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.like.service.ILikeService;
import org.benben.modules.business.posts.vo.PostsVos;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.benben.modules.business.recommendposts.service.IRecommendPostsService;
import org.benben.modules.business.recommendposts.vo.ChallengeVo;
import org.benben.modules.business.recommendposts.vo.RecommendPostsVo;
import org.benben.modules.business.recommendposts.vo.RecommendsVos;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.business.usercollect.service.IUserCollectService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 管理员推荐帖子
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/recommendPosts")
@Api(tags = {"帖子接口"})
@Slf4j
public class RestRecommendPostsController {
	@Autowired
	private IRecommendPostsService recommendPostsService;

	@Autowired
	private ILikeService likeService;
	@Autowired
	private IUserCollectService collectService;

	@GetMapping("/challengePosts")
	@ApiOperation(value = "挑战帖子", tags = {"帖子接口"}, notes = "挑战帖子")
	public RestResponseBean challengePosts(){
		RecommendPosts recommendPosts = recommendPostsService.challengePosts();
		ChallengeVo challengeVo = new ChallengeVo();
		BeanUtils.copyProperties(recommendPosts,challengeVo);
		//查询点赞的状态
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Integer likeType = likeService.getLikeType(user.getId(), challengeVo.getId());
		if (likeType == null) {
			challengeVo.setLikeType(0);
		} else {
			challengeVo.setLikeType(1);
		}

		//给是否取消收藏一个默认值
		UserCollect collect = collectService.getCollect( recommendPosts.getId(),user.getId());
		if (collect == null) {
			challengeVo.setCollectType(0);
		} else {
			challengeVo.setCollectType(1);
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),challengeVo);
	}

	@GetMapping("/listRecommendPosts")
	@ApiOperation(value = "展示最新的推荐帖子", tags = {"帖子接口"}, notes = "展示最新的推荐帖子")
	public RestResponseBean listRecommendPosts(@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
		RecommendPostsVo recommendPostsVo = recommendPostsService.listRecommendPosts(pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),recommendPostsVo);
	}

	/**
	 * 分页列表查询
	 * @param recommendPosts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<RecommendPosts>> queryPageList(RecommendPosts recommendPosts,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<RecommendPosts>> result = new Result<IPage<RecommendPosts>>();
		QueryWrapper<RecommendPosts> queryWrapper = QueryGenerator.initQueryWrapper(recommendPosts, req.getParameterMap());
		Page<RecommendPosts> page = new Page<RecommendPosts>(pageNo, pageSize);
		IPage<RecommendPosts> pageList = recommendPostsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param recommendPosts
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<RecommendPosts> add(@RequestBody RecommendPosts recommendPosts) {
		Result<RecommendPosts> result = new Result<RecommendPosts>();
		try {
			recommendPostsService.save(recommendPosts);
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
	 * @param recommendPosts
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<RecommendPosts> edit(@RequestBody RecommendPosts recommendPosts) {
		Result<RecommendPosts> result = new Result<RecommendPosts>();
		RecommendPosts recommendPostsEntity = recommendPostsService.getById(recommendPosts.getId());
		if(recommendPostsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = recommendPostsService.updateById(recommendPosts);
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
	public Result<RecommendPosts> delete(@RequestParam(name="id",required=true) String id) {
		Result<RecommendPosts> result = new Result<RecommendPosts>();
		RecommendPosts recommendPosts = recommendPostsService.getById(id);
		if(recommendPosts==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = recommendPostsService.removeById(id);
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
	@PostMapping(value = "/deleteBatch")
	@ApiOperation(value = "删除推荐帖子", tags = {"帖子接口"}, notes = "删除推荐帖子")
	public Result<RecommendPosts> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<RecommendPosts> result = new Result<RecommendPosts>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.recommendPostsService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "查看推荐帖子详情",tags = {"帖子接口"}, notes = "查看推荐帖子详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<RecommendPosts> result = new Result<RecommendPosts>();
		RecommendPosts recommendPosts = recommendPostsService.getById(id);
		//
		String bookUrl = recommendPosts.getIntroImg();
		List<String> list = new ArrayList<>();
		list = Arrays.asList(bookUrl.split(","));

		RecommendsVos recommendsVos = new RecommendsVos();
		BeanUtils.copyProperties(recommendPosts,recommendsVos);
		recommendsVos.setIntroImg(list);
		//查询点赞的状态
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Integer likeType = likeService.getLikeType(user.getId(), recommendsVos.getId());
		if (likeType == null) {
			recommendsVos.setLikeType(0);
		} else {
			recommendsVos.setLikeType(1);
		}
		//给是否取消收藏一个默认值
		UserCollect collect = collectService.getCollect( recommendsVos.getId(),user.getId());
		if (collect == null) {
			recommendsVos.setCollectType(0);
		} else {
			recommendsVos.setCollectType(1);
		}

		if(recommendPosts==null) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			result.setResult(recommendPosts);
			result.setSuccess(true);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),recommendsVos);
		}
	}
	@GetMapping(value = "/queryChallengeById")
	@ApiOperation(value = "查看挑战帖子详情",tags = {"帖子接口"}, notes = "查看挑战帖子详情")
	public RestResponseBean queryChallengeById(@RequestParam(name="id",required=true) String id) {
		RecommendPosts recommendPosts = recommendPostsService.getById(id);


		ChallengeVo challengeVo = new ChallengeVo();
		BeanUtils.copyProperties(recommendPosts,challengeVo);

		RecommendsVos recommendsVos = new RecommendsVos();
		//查询点赞的状态
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Integer likeType = likeService.getLikeType(user.getId(), challengeVo.getId());
		if (likeType == null) {
			challengeVo.setLikeType(0);
		} else {
			challengeVo.setLikeType(1);
		}
		//给是否取消收藏一个默认值
		UserCollect collect = collectService.getCollect( challengeVo.getId(),user.getId());
		if (collect == null) {
			challengeVo.setCollectType(0);
		} else {
			challengeVo.setCollectType(1);
		}

		if(recommendPosts==null) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),challengeVo);
		}
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
		QueryWrapper<RecommendPosts> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				RecommendPosts recommendPosts = JSON.parseObject(deString, RecommendPosts.class);
				queryWrapper = QueryGenerator.initQueryWrapper(recommendPosts, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<RecommendPosts> pageList = recommendPostsService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "管理员推荐帖子列表");
		mv.addObject(NormalExcelConstants.CLASS, RecommendPosts.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("管理员推荐帖子列表数据", "导出人:Jeecg", "导出信息"));
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
				List<RecommendPosts> listRecommendPostss = ExcelImportUtil.importExcel(file.getInputStream(), RecommendPosts.class, params);
				for (RecommendPosts recommendPostsExcel : listRecommendPostss) {
					recommendPostsService.save(recommendPostsExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listRecommendPostss.size());
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
