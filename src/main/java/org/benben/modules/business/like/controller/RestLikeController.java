package org.benben.modules.business.like.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.like.entity.Like;
import org.benben.modules.business.like.service.ILikeService;
import org.benben.modules.business.posts.service.IPostsService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.userevaluate.service.IUserEvaluateService;
import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.service.IUserPostsService;
import org.benben.modules.system.entity.SysUser;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 点赞管理
* @author： jeecg-boot
* @date：   2019-06-10
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/like")
@Slf4j
public class RestLikeController {
   @Autowired
   private ILikeService likeService;

   @Autowired
   private IUserMessageService userMessageService;

   @Autowired
   private IUserPostsService userPostsService;

   @Autowired
   private IUserEvaluateService userEvaluateService;
   /**
	 * 分页列表查询
	* @param like
	* @param pageNo
	* @param pageSize
	* @param req
	* @return
	*/
   @GetMapping(value = "/list")
   public Result<IPage<Like>> queryPageList(Like like,
									 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									 HttpServletRequest req) {
	   Result<IPage<Like>> result = new Result<IPage<Like>>();
	   QueryWrapper<Like> queryWrapper = QueryGenerator.initQueryWrapper(like, req.getParameterMap());
	   Page<Like> page = new Page<Like>(pageNo, pageSize);
	   IPage<Like> pageList = likeService.page(page, queryWrapper);
	   result.setSuccess(true);
	   result.setResult(pageList);
	   return result;
   }

   /**
	 *   添加
	* @param like
	* @return
	*/
   @PostMapping(value = "/add")
   @ApiOperation(value = "点赞帖子添加接口",tags = {"帖子接口"},notes = "点赞添加接口")
   public RestResponseBean add(@RequestBody Like like) {
	   Result<Like> result = new Result<Like>();
	   User sysUser = (User) SecurityUtils.getSubject().getPrincipal();
	   like.setUid(sysUser.getId());
	   //查询点赞是否存在
	   List<Like> likes = likeService.queryLike(sysUser.getId(), like.getPostId());
	   if (likes.size() != 0) {
		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"该用户已经点赞过！");
	   }
	   try {
		   likeService.save(like);
			//点赞的用户添加进我的消息中
		   UserMessage userMessage = new UserMessage();
		   userMessage.setPostsId(like.getPostId());
		   userMessage.setUserLikeId(like.getUid());
		   //得到被点赞的帖子的那个用户
		   UserPosts userPosts = userPostsService.getByPostId(like.getPostId());
		   if (userPosts != null) {
			   userMessage.setUserId(userPosts.getUserId());
			   userMessage.setState(0);
		   }
		   userMessageService.save(userMessage);

		   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	   } catch (Exception e) {
		   e.printStackTrace();
		   log.info(e.getMessage());
		   result.error500("操作失败");
		   return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");
	   }
   }
	@PostMapping(value = "/addEvaluate")
	@ApiOperation(value = "点赞评论添加接口",tags = {"用户评论接口"},notes = "点赞添加接口")
	public RestResponseBean addEvaluate(@RequestBody Like like) {
		Result<Like> result = new Result<Like>();
		User sysUser = (User) SecurityUtils.getSubject().getPrincipal();
		like.setUid(sysUser.getId());
		//查询点赞是否存在
		List<Like> likes = likeService.queryEvaluate(sysUser.getId(), like.getEvaluateId());
		if (likes.size() != 0) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"该用户已经点赞过！");
		}

		//通过评论id得到帖子id
		UserEvaluate userEvaluate = userEvaluateService.getById(like.getEvaluateId());

		try {
			likeService.save(like);
			//点赞的用户添加进我的消息中
			UserMessage userMessage = new UserMessage();
			userMessage.setPostsId(userEvaluate.getPostsId());
			userMessage.setUserLikeId(like.getUid());
			//得到被点赞的帖子的那个用户
			UserPosts userPosts = userPostsService.getByPostId(userEvaluate.getPostsId());
			if (userPosts != null) {
				userMessage.setUserId(userPosts.getUserId());
				userMessage.setState(0);
			}
			userMessageService.save(userMessage);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");
		}
	}


   /**
	 *  编辑
	* @param like
	* @return
	*/
   @PutMapping(value = "/edit")
   public Result<Like> edit(@RequestBody Like like) {
	   Result<Like> result = new Result<Like>();

	   Like likeEntity = likeService.getById(like.getId());
	   if(likeEntity==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = likeService.updateById(like);
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
   public Result<Like> delete(@RequestParam(name="id",required=true) String id) {
	   Result<Like> result = new Result<Like>();
	   Like like = likeService.getById(id);
	   if(like==null) {
		   result.error500("未找到对应实体");
	   }else {
		   boolean ok = likeService.removeById(id);
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
   public Result<Like> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
	   Result<Like> result = new Result<Like>();
	   if(ids==null || "".equals(ids.trim())) {
		   result.error500("参数不识别！");
	   }else {
		   this.likeService.removeByIds(Arrays.asList(ids.split(",")));
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
   public Result<Like> queryById(@RequestParam(name="id",required=true) String id) {
	   Result<Like> result = new Result<Like>();
	   Like like = likeService.getById(id);
	   if(like==null) {
		   result.error500("未找到对应实体");
	   }else {
		   result.setResult(like);
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
	 QueryWrapper<Like> queryWrapper = null;
	 try {
		 String paramsStr = request.getParameter("paramsStr");
		 if (oConvertUtils.isNotEmpty(paramsStr)) {
			 String deString = URLDecoder.decode(paramsStr, "UTF-8");
			 Like like = JSON.parseObject(deString, Like.class);
			 queryWrapper = QueryGenerator.initQueryWrapper(like, request.getParameterMap());
		 }
	 } catch (UnsupportedEncodingException e) {
		 e.printStackTrace();
	 }

	 //Step.2 AutoPoi 导出Excel
	 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	 List<Like> pageList = likeService.list(queryWrapper);
	 //导出文件名称
	 mv.addObject(NormalExcelConstants.FILE_NAME, "点赞管理列表");
	 mv.addObject(NormalExcelConstants.CLASS, Like.class);
	 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("点赞管理列表数据", "导出人:Jeecg", "导出信息"));
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
			 List<Like> listLikes = ExcelImportUtil.importExcel(file.getInputStream(), Like.class, params);
			 for (Like likeExcel : listLikes) {
				 likeService.save(likeExcel);
			 }
			 return Result.ok("文件导入成功！数据行数：" + listLikes.size());
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
