package org.benben.modules.business.usermessage.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.usermessage.vo.EvaluateMessageVo;
import org.benben.modules.business.usermessage.vo.LikeVo;
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
 * @Description: 用户消息管理
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userMessage")
@Api(tags = "我的消息")
@Slf4j
public class RestUserMessageController {
	@Autowired
	private IUserMessageService userMessageService;

//	public RestResponseBean

	 //获取点赞的消息或者评论的消息第一条
	 @GetMapping("/getNew")
	 @ApiOperation(value = "取点赞的消息或者评论的消息第一条（0/点赞1/评论）", tags = "我的消息", notes = "取点赞的消息或者评论的消息第一条（0/点赞1/评论）")
	 public RestResponseBean getNew(@RequestParam Integer state) {
		 User user = (User) SecurityUtils.getSubject().getPrincipal();
		 if(state == 0) {
			 LikeVo likeVo = userMessageService.queryNewLike(state,user);
			 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),likeVo);
		 } else {
			 EvaluateMessageVo evaluateMessageVo = userMessageService.queryNewLikes(state, user.getId());
			 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),evaluateMessageVo);
		 }
	 }

	 // 查询我的消息未读的有几条
	 @GetMapping("/getMessageNum")
	 @ApiOperation(value = "查看系统消息未读的有几条", tags = "我的消息", notes = "查看系统消息未读的有几条")
	 public RestResponseBean getMessageNum() {
	 	Integer number = userMessageService.getMessageNum();
	 	return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),number);
	 }

	 /**
	  * 查询系统消息
	  * @param state
	  * @return
	  */
	@PostMapping("/queryByState")
	@ApiOperation(value = "查询系统消息接口", tags = "我的消息", notes = "查询系统消息接口")
	@ApiImplicitParam(name = "state",value = "消息状态 0/赞同的消息 1/评论的消息 2/普通消息")
	public RestResponseBean queryByState(@RequestParam Integer state) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (state == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		if (state == 2 ) {
			List<UserMessage> userMessages = userMessageService.queryByState(state,user.getId());
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userMessages);
		} else if(state == 0) {
			List<LikeVo> userMessageVos = userMessageService.queryByLike(state,user);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userMessageVos);
		} else {
			List<EvaluateMessageVo> userMessageVos = userMessageService.queryByLikes(state, user.getId());
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userMessageVos);
		}

	}
	/**
	  * 分页列表查询
	 * @param userMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserMessage>> queryPageList(UserMessage userMessage,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserMessage>> result = new Result<IPage<UserMessage>>();
		QueryWrapper<UserMessage> queryWrapper = QueryGenerator.initQueryWrapper(userMessage, req.getParameterMap());
		Page<UserMessage> page = new Page<UserMessage>(pageNo, pageSize);
		IPage<UserMessage> pageList = userMessageService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userMessage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserMessage> add(@RequestBody UserMessage userMessage) {
		Result<UserMessage> result = new Result<UserMessage>();
		try {
			userMessageService.save(userMessage);
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
	 * @param userMessage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserMessage> edit(@RequestBody UserMessage userMessage) {
		Result<UserMessage> result = new Result<UserMessage>();
		UserMessage userMessageEntity = userMessageService.getById(userMessage.getId());
		if(userMessageEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userMessageService.updateById(userMessage);
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
	@ApiOperation(value = "删除系统消息", tags = "我的消息", notes = "删除系统消息")
	public RestResponseBean delete(@RequestParam String id) {
		Result<UserMessage> result = new Result<UserMessage>();
		UserMessage userMessage = userMessageService.getById(id);
		if(userMessage==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			boolean ok = userMessageService.removeById(id);
			if(ok) {
				result.success("删除成功!");
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"删除成功");
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");

	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<UserMessage> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserMessage> result = new Result<UserMessage>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userMessageService.removeByIds(Arrays.asList(ids.split(",")));
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
	@ApiOperation(value = "获取系统消息详情", tags = "我的消息", notes = "获取系统消息详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserMessage> result = new Result<UserMessage>();
		UserMessage userMessage = userMessageService.getById(id);
		if(userMessage==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			result.setResult(userMessage);
			result.setSuccess(true);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userMessage);
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
      QueryWrapper<UserMessage> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserMessage userMessage = JSON.parseObject(deString, UserMessage.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userMessage, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserMessage> pageList = userMessageService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户消息表列表");
      mv.addObject(NormalExcelConstants.CLASS, UserMessage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户消息表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserMessage> listUserMessages = ExcelImportUtil.importExcel(file.getInputStream(), UserMessage.class, params);
              for (UserMessage userMessageExcel : listUserMessages) {
                  userMessageService.save(userMessageExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserMessages.size());
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
