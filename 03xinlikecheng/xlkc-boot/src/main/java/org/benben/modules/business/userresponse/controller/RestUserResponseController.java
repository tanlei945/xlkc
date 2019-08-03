package org.benben.modules.business.userresponse.controller;

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
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.userevaluate.service.IUserEvaluateService;
import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.service.IUserPostsService;
import org.benben.modules.business.userresponse.entity.UserResponse;
import org.benben.modules.business.userresponse.service.IUserResponseService;

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
 * @Description: 用户反馈表管理
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userResponse")
@Api(tags = {"用户评论接口"})
@Slf4j
public class RestUserResponseController {
	@Autowired
	private IUserResponseService userResponseService;

	@Autowired
	private IUserMessageService userMessageService;

	@Autowired
	private IUserPostsService userPostsService;

	@Autowired
	private IUserEvaluateService userEvaluateService;

	/**
	 * 分页列表查询
	 * @param userResponse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserResponse>> queryPageList(UserResponse userResponse,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserResponse>> result = new Result<IPage<UserResponse>>();
		QueryWrapper<UserResponse> queryWrapper = QueryGenerator.initQueryWrapper(userResponse, req.getParameterMap());
		Page<UserResponse> page = new Page<UserResponse>(pageNo, pageSize);
		IPage<UserResponse> pageList = userResponseService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param userResponse
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加回复内容", tags = {"用户评论接口"}, notes = "添加回复内容")
	public RestResponseBean add(@RequestBody UserResponse userResponse) {
		Result<UserResponse> result = new Result<UserResponse>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		userResponse.setEvaluateUserId(user.getId());

		userResponseService.save(userResponse);

		//把评论信息添加到我的评论当中
		UserMessage userMessage = new UserMessage();

		//添加返回用戶id
		userMessage.setResponseId(userResponse.getId());
		//添加评论用户id
		userMessage.setEvaluateId(userResponse.getEvaluateId());

		//通过回复的用户得到帖子id
		UserEvaluate userEvaluate = userEvaluateService.getById(userResponse.getEvaluateId());

		//得到被点赞的帖子的那个用户
		UserPosts userPosts = userPostsService.getByPostId(userEvaluate.getPostsId());
		if (userPosts != null) {
			userMessage.setPostsId(userEvaluate.getPostsId());
			userMessage.setUserId(userPosts.getUserId());
			userMessage.setState(2);
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
	 * @param userResponse
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserResponse> edit(@RequestBody UserResponse userResponse) {
		Result<UserResponse> result = new Result<UserResponse>();
		UserResponse userResponseEntity = userResponseService.getById(userResponse.getId());
		if(userResponseEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userResponseService.updateById(userResponse);
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
	public Result<UserResponse> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserResponse> result = new Result<UserResponse>();
		UserResponse userResponse = userResponseService.getById(id);
		if(userResponse==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userResponseService.removeById(id);
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
	public Result<UserResponse> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserResponse> result = new Result<UserResponse>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userResponseService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserResponse> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserResponse> result = new Result<UserResponse>();
		UserResponse userResponse = userResponseService.getById(id);
		if(userResponse==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userResponse);
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
		QueryWrapper<UserResponse> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserResponse userResponse = JSON.parseObject(deString, UserResponse.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userResponse, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserResponse> pageList = userResponseService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户反馈表管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserResponse.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户反馈表管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserResponse> listUserResponses = ExcelImportUtil.importExcel(file.getInputStream(), UserResponse.class, params);
				for (UserResponse userResponseExcel : listUserResponses) {
					userResponseService.save(userResponseExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserResponses.size());
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
