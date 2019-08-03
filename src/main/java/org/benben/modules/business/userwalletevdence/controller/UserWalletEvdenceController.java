package org.benben.modules.business.userwalletevdence.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userwallet.entity.UserWallet;
import org.benben.modules.business.userwallet.service.IUserWalletService;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import org.benben.modules.business.userwalletevdence.service.IUserWalletEvdenceService;

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
 * @Description: 钱包凭证管理
 * @author： jeecg-boot
 * @date：   2019-07-10
 * @version： V1.0
 */
@RestController
@RequestMapping("/userwalletevdence/userWalletEvdence")
@Slf4j
public class UserWalletEvdenceController {
	@Autowired
	private IUserWalletEvdenceService userWalletEvdenceService;

	@Autowired
	private IUserWalletService userWalletService;
	@Autowired
	private IUserService userService;

	/**
	 * 分页列表查询
	 * @param userWalletEvdence
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserWalletEvdence>> queryPageList(UserWalletEvdence userWalletEvdence,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserWalletEvdence>> result = new Result<IPage<UserWalletEvdence>>();
		QueryWrapper<UserWalletEvdence> queryWrapper = QueryGenerator.initQueryWrapper(userWalletEvdence, req.getParameterMap());
		Page<UserWalletEvdence> page = new Page<UserWalletEvdence>(pageNo, pageSize);
		IPage<UserWalletEvdence> pageList = userWalletEvdenceService.page(page, queryWrapper);
		List<UserWalletEvdence> records = pageList.getRecords();
		for (UserWalletEvdence record : records) {
			User user = userService.getById(record.getUid());
			record.setNickname(user.getNickname());
		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param userWalletEvdence
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserWalletEvdence> add(@RequestBody UserWalletEvdence userWalletEvdence) {
		Result<UserWalletEvdence> result = new Result<UserWalletEvdence>();
		try {
			userWalletEvdenceService.save(userWalletEvdence);
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
	 * @param userWalletEvdence
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserWalletEvdence> edit(@RequestBody UserWalletEvdence userWalletEvdence) {
		Result<UserWalletEvdence> result = new Result<UserWalletEvdence>();

		UserWalletEvdence userWalletEvdenceEntity = userWalletEvdenceService.getById(userWalletEvdence.getId());
		if(userWalletEvdenceEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userWalletEvdenceService.updateById(userWalletEvdence);
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
	public Result<UserWalletEvdence> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserWalletEvdence> result = new Result<UserWalletEvdence>();
		UserWalletEvdence userWalletEvdence = userWalletEvdenceService.getById(id);
		if(userWalletEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userWalletEvdenceService.removeById(id);
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
	public Result<UserWalletEvdence> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserWalletEvdence> result = new Result<UserWalletEvdence>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userWalletEvdenceService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserWalletEvdence> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserWalletEvdence> result = new Result<UserWalletEvdence>();
		UserWalletEvdence userWalletEvdence = userWalletEvdenceService.getById(id);
		if(userWalletEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userWalletEvdence);
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
		QueryWrapper<UserWalletEvdence> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserWalletEvdence userWalletEvdence = JSON.parseObject(deString, UserWalletEvdence.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userWalletEvdence, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserWalletEvdence> pageList = userWalletEvdenceService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "钱包凭证管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserWalletEvdence.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("钱包凭证管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserWalletEvdence> listUserWalletEvdences = ExcelImportUtil.importExcel(file.getInputStream(), UserWalletEvdence.class, params);
				for (UserWalletEvdence userWalletEvdenceExcel : listUserWalletEvdences) {
					userWalletEvdenceService.save(userWalletEvdenceExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserWalletEvdences.size());
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
