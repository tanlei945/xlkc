package org.benben.modules.business.posts.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.coursetype.service.ICourseTypeService;
import org.benben.modules.business.coursetype.vo.CourseTypeVo;
import org.benben.modules.business.like.service.ILikeService;
import org.benben.modules.business.posts.entity.Posts;
import org.benben.modules.business.posts.service.IPostsService;
import org.benben.modules.business.posts.vo.PostsVo;
import org.benben.modules.business.posts.vo.PostsVos;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.benben.modules.business.recommendposts.service.IRecommendPostsService;
import org.benben.modules.business.recommendposts.vo.RecommendsVos;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.business.usercollect.service.IUserCollectService;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.service.IUserPostsService;
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
 * @Description: 帖子的管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/posts")
@Api(tags = {"帖子接口"})
@Slf4j
public class RestPostsController {
	@Autowired
	private IPostsService postsService;

	@Autowired
	private ILikeService likeService;

	@Autowired
	private IUserCollectService collectService;

	@Autowired
	private ICourseTypeService courseTypeService;

	@Autowired
	private IUserPostsService userPostsService;

	@Autowired
	private IRecommendPostsService recommendPostsService;
	//热门搜索接口

	@PostMapping("/hotSearch")
	@ApiOperation(value = "热门搜索",tags = {"帖子接口"},notes = "热门搜索")
	public RestResponseBean hotSearch() {
		List<String> list = postsService.hotSearch();
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
	}


	@GetMapping("/getCourseType")
	@ApiOperation(value = "得到我的课程的全部分类", tags = {"帖子接口"}, notes = "得到我的课程的全部分类")
	public RestResponseBean getCourseType() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		List<CourseTypeVo> courseTypeVo = courseTypeService.getCourseType(user.getId());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),courseTypeVo);
	}

	@PostMapping("/listNewPost")
	@ApiOperation(value = "分页展示帖子", tags = {"帖子接口"}, notes = "分页展示帖子")
	public RestResponseBean listNewPosts(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, Integer type,String name,String courseTypeId) {
		//type 0/表示最新的 1/表示最热的 模糊查询的名字
		if (StringUtils.isBlank(name)) {
			name = "";
		}
		PostsVo postsVo;
		if (type == 0) {
			if (StringUtils.isBlank(courseTypeId)) {
				//分页模糊查询最新的
				postsVo = postsService.queryNewPosts(name,pageNumber,pageSize);
			} else {
				//根据课程分类和课程名字来得到数据
				postsVo = postsService.getHottestPosts(courseTypeId,name,pageNumber,pageSize);
			}
		} else {
			//判断是否为按照课程分类得到帖子信息
			if (StringUtils.isBlank(courseTypeId)) {
				//分页模糊查询最热的
				postsVo = postsService.queryHottestPost(name, pageNumber, pageSize);
			} else {
				//根据课程分类和课程名字来得到数据
				postsVo = postsService.queryCourseType(courseTypeId,name,pageNumber,pageSize);
			}
		}
		//		PostsVo postsVo = postsService.listNewPosts(pageNumber,pageSize);
		/*for (Posts post : postsVo.getPostsList()) {
			postsService.updateById(post);
		}*/
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),postsVo);
	}

	/*@GetMapping("/listHottestPost")
	@ApiOperation(value = "分页展示最热帖子", tags = {"帖子接口"}, notes = "分页展示最热帖子")
	public RestResponseBean listHottestPost(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		PostsVo postsVo = postsService.listHottestPosts(pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),postsVo);
	}*/

	/**
	 * 分页列表查询
	 * @param posts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Posts>> queryPageList(Posts posts,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<Posts>> result = new Result<IPage<Posts>>();
		QueryWrapper<Posts> queryWrapper = QueryGenerator.initQueryWrapper(posts, req.getParameterMap());
		Page<Posts> page = new Page<Posts>(pageNo, pageSize);
		IPage<Posts> pageList = postsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param posts
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加帖子",tags = {"帖子接口"},notes = "添加帖子")
	public RestResponseBean add(@RequestBody Posts posts) {
		Result<Posts> result = new Result<Posts>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();


		try {
			postsService.save(posts);
			//添加到我的帖子中
			UserPosts userPosts = new UserPosts();
			userPosts.setPostsId(posts.getId());
			userPosts.setUserId(user.getId());
			userPostsService.save(userPosts);
			result.success("添加成功！");
			return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
			return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");
		}
	}

	/**
	 *  编辑
	 * @param posts
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Posts> edit(@RequestBody Posts posts) {
		Result<Posts> result = new Result<Posts>();
		Posts postsEntity = postsService.getById(posts.getId());
		if(postsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = postsService.updateById(posts);
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
	@PostMapping(value = "/delete")
	//	@ApiOperation(value = "删除帖子", tags = {"帖子接口"}, notes = "删除帖子")
	public Result<Posts> delete(@RequestParam(name="id",required=true) String id) {
		Result<Posts> result = new Result<Posts>();
		Posts posts = postsService.getById(id);
		if(posts==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = postsService.removeById(id);
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
	public Result<Posts> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Posts> result = new Result<Posts>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.postsService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "查看帖子详情",tags = {"帖子接口"}, notes = "查看帖子详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<Posts> result = new Result<Posts>();
		//查看论坛帖子是否为空，，
		Posts posts = postsService.getById(id);
		//推荐帖子
		RecommendPosts recommendPosts = recommendPostsService.getById(id);
		//挑战帖子
		RecommendPosts recommendPosts1 = recommendPostsService.getById(id);

		if(posts != null){
			String bookUrl = posts.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(bookUrl.split(","));

			PostsVos postsVos = new PostsVos();
			BeanUtils.copyProperties(posts,postsVos);
			postsVos.setIntroImg(list);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = likeService.getLikeType(user.getId(), postsVos.getId());
			if (likeType == null) {
				postsVos.setLikeType(0);
			} else {
				postsVos.setLikeType(1);
			}

			//给是否取消收藏一个默认值
			UserCollect collect = collectService.getCollect(postsVos.getId(),user.getId());
			if (collect == null) {
				postsVos.setCollectType(0);
			} else {
				postsVos.setCollectType(1);
			}

			if(posts==null) {
				result.error500("未找到对应实体");
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"没有该帖子");
			}else {
				result.setResult(posts);
				result.setSuccess(true);
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),postsVos);
			}
		} else if (recommendPosts != null){
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
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),recommendsVos);
			}
		} else if (recommendPosts1 != null){
			String bookUrl = recommendPosts1.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(bookUrl.split(","));

			RecommendsVos recommendsVos = new RecommendsVos();
			BeanUtils.copyProperties(recommendPosts1,recommendsVos);
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
			UserCollect collect = collectService.getCollect(recommendsVos.getId(),user.getId());
			if (collect == null) {
				recommendsVos.setCollectType(0);
			} else {
				recommendsVos.setCollectType(1);
			}

			if(recommendPosts1==null) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
			}else {
				result.setSuccess(true);
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),recommendsVos);
			}
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
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
		QueryWrapper<Posts> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Posts posts = JSON.parseObject(deString, Posts.class);
				queryWrapper = QueryGenerator.initQueryWrapper(posts, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Posts> pageList = postsService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "帖子的管理列表");
		mv.addObject(NormalExcelConstants.CLASS, Posts.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("帖子的管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<Posts> listPostss = ExcelImportUtil.importExcel(file.getInputStream(), Posts.class, params);
				for (Posts postsExcel : listPostss) {
					postsService.save(postsExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listPostss.size());
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
