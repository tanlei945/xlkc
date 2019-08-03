package org.benben.modules.business.userevaluate.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.userevaluate.service.IUserEvaluateService;
import org.benben.modules.business.userevaluate.vo.UserEvaluateVo;
import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.service.IUserPostsService;
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
 * @Description: 用户评论表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userEvaluate")
@Api(tags = {"用户评论接口"})
@Slf4j
public class RestUserEvaluateController {
	@Autowired
	private IUserEvaluateService userEvaluateService;

	@Autowired
	private IUserMessageService userMessageService;

	@Autowired
	private IUserPostsService userPostsService;


	@GetMapping("queryByPostId")
	@ApiOperation(value = "通过帖子查看评论", tags = {"用户评论接口"}, notes = "通过帖子查看评论")
	public RestResponseBean queryByPostId(@RequestParam String postId) {
		List<UserEvaluateVo> userEvaluateVo= userEvaluateService.queryByPostId(postId);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userEvaluateVo);
	}
	@PostMapping("/queryByEvaluateId")
	@ApiOperation(value = "通过评论id得到全部的评论", tags = {"用户评论接口"},notes ="通过评论ID得到全部的评论")
	public  RestResponseBean queryByEvaluateId(@RequestParam String EvaluateId) {
		List<UserEvaluateVo> list = userEvaluateService.queryByEvaluateId(EvaluateId);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
	}

	/**
	 * 分页列表查询
	 * @param userEvaluate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserEvaluate>> queryPageList(UserEvaluate userEvaluate,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserEvaluate>> result = new Result<IPage<UserEvaluate>>();
		QueryWrapper<UserEvaluate> queryWrapper = QueryGenerator.initQueryWrapper(userEvaluate, req.getParameterMap());
		Page<UserEvaluate> page = new Page<UserEvaluate>(pageNo, pageSize);
		IPage<UserEvaluate> pageList = userEvaluateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加评论内容", tags = {"用户评论接口"}, notes = "添加评论内容")
	public RestResponseBean add(String parentId, String postsId,@RequestParam String content) {
		Result<UserEvaluate> result = new Result<UserEvaluate>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UserEvaluate userEvaluate = new UserEvaluate(user.getId(),parentId,postsId,content);
		userEvaluateService.save(userEvaluate);

		//把评论信息添加到我的评论当中
		UserMessage userMessage = new UserMessage();
		userMessage.setPostsId(userEvaluate.getPostsId());
		userMessage.setEvaluateId(userEvaluate.getId());
		//得到被评论的帖子的那个用户
		UserPosts userPosts = userPostsService.getByPostId(userEvaluate.getPostsId());
		if (userPosts != null) {
			userMessage.setUserId(userPosts.getUserId());
			userMessage.setState(1);
		}

		userMessageService.save(userMessage);

		try {
			result.success("添加成功！");
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
	 * @param userEvaluate
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserEvaluate> edit(@RequestBody UserEvaluate userEvaluate) {
		Result<UserEvaluate> result = new Result<UserEvaluate>();
		UserEvaluate userEvaluateEntity = userEvaluateService.getById(userEvaluate.getId());
		if(userEvaluateEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userEvaluateService.updateById(userEvaluate);
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
	public Result<UserEvaluate> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserEvaluate> result = new Result<UserEvaluate>();
		UserEvaluate userEvaluate = userEvaluateService.getById(id);
		if(userEvaluate==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userEvaluateService.removeById(id);
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
	public Result<UserEvaluate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserEvaluate> result = new Result<UserEvaluate>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userEvaluateService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserEvaluate> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserEvaluate> result = new Result<UserEvaluate>();
		UserEvaluate userEvaluate = userEvaluateService.getById(id);
		if(userEvaluate==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userEvaluate);
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
		QueryWrapper<UserEvaluate> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserEvaluate userEvaluate = JSON.parseObject(deString, UserEvaluate.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userEvaluate, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserEvaluate> pageList = userEvaluateService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户评论表管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserEvaluate.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户评论表管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserEvaluate> listUserEvaluates = ExcelImportUtil.importExcel(file.getInputStream(), UserEvaluate.class, params);
				for (UserEvaluate userEvaluateExcel : listUserEvaluates) {
					userEvaluateService.save(userEvaluateExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserEvaluates.size());
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
