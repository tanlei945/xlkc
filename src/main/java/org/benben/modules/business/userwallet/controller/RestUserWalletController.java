package org.benben.modules.business.userwallet.controller;

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
import org.benben.modules.business.userwallet.entity.UserWallet;
import org.benben.modules.business.userwallet.service.IUserWalletService;
import org.benben.modules.business.userwallet.vo.UserWalletVo;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import org.benben.modules.business.userwalletevdence.service.IUserWalletEvdenceService;
import org.benben.modules.business.userwalletevdence.vo.UserWalletEvdenceVo;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 用户钱包管理
 * @author： jeecg-boot
 * @date：   2019-07-08
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userWallet")
@Slf4j
public class RestUserWalletController {
	@Autowired
	private IUserWalletService userWalletService;
	@Autowired
	private IUserWalletEvdenceService userWalletEvdenceService;

	@GetMapping("/accountBackNow")
	@ApiOperation(value = "账号回现",tags = "用户接口", notes = "账号绑定")
	public RestResponseBean accountBackNow() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UserWalletEvdenceVo userWalletEvdenceVo = userWalletEvdenceService.accountBackNow(user.getId());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userWalletEvdenceVo);
	}

	@GetMapping("/bingDingAccount")
	@ApiOperation(value = "账号绑定", tags = {"用户接口"},notes = "账号绑定")
	public RestResponseBean bingDingAccount(@RequestParam String id, @RequestParam String realName, @RequestParam String pictureUrl) {
		UserWallet userWallet1 = userWalletService.getById(id);

		UserWalletEvdence userWalletEvdence = userWalletEvdenceService.getById(userWallet1.getWalletEvdenceId());
		userWalletEvdence.setPicture(pictureUrl);
		userWalletEvdence.setRealName(realName);
		boolean b = userWalletEvdenceService.updateById(userWalletEvdence);
		if (b) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), "绑定成功");
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "绑定失败");
		}
	}

	@PostMapping("/withdrawDeposit")
	@ApiOperation(value = "余额提现",tags = {"用户接口"},notes = "余额提现")
	public RestResponseBean withdrawDeposit(@RequestParam String id,@RequestParam BigDecimal money,@RequestParam String pictureUrl) {

		//查看钱包支付凭证

		//查询
		UserWallet userWallet1 = userWalletService.getById(id);

		UserWalletEvdence userWalletEvdence = userWalletEvdenceService.getById(userWallet1.getWalletEvdenceId());
		//判断钱包支付凭证是否确定
		if (userWalletEvdence != null) {
			BigDecimal money1 = userWallet1.getMoney();
			BigDecimal m = money1.subtract(money);

			if (m.compareTo(new BigDecimal("0")) < 0) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "提现额度太大");
			} else {
				userWalletEvdence.setMoney(money);
				userWalletEvdence.setPicture(pictureUrl);
				userWalletEvdenceService.updateById(userWalletEvdence);
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), "提交成功");
			}
		}
	/*	if (userWallet1 != null) {
			BigDecimal money1 = userWallet1.getMoney();
			BigDecimal m = money1.subtract(money);

			if (m.compareTo(new BigDecimal("0")) < 0) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "提现额度太大");
			} else {
				userWallet1.setMoney(m);
				userWalletService.updateById(userWallet1);
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), "提现成功");
			}
		}*/
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), "操作失败");
	}

	@GetMapping("/queryWallet")
	@ApiOperation(value = "显示余额",tags = {"用户接口"}, notes = "显示余额")
	public RestResponseBean queryWallet() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UserWalletVo userWallet = userWalletService.queryByUserId(user.getId());
		return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userWallet);
	}

	/**
	 * 分页列表查询
	 * @param userWallet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserWallet>> queryPageList(UserWallet userWallet,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserWallet>> result = new Result<IPage<UserWallet>>();
		QueryWrapper<UserWallet> queryWrapper = QueryGenerator.initQueryWrapper(userWallet, req.getParameterMap());
		Page<UserWallet> page = new Page<UserWallet>(pageNo, pageSize);
		IPage<UserWallet> pageList = userWalletService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}


	@PostMapping(value = "/add")
	//   @ApiOperation(value = "余额添加",tags = {"用户接口"}, notes = "余额添加")
	public RestResponseBean add(@RequestBody UserWallet userWallet) {
		Result<UserWallet> result = new Result<UserWallet>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		userWallet.setUserId(user.getId());
		try {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");

		}
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 余额充值
	 * @description 余额充值接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/userWallet/edit
	 * @param money BigDecimal 钱包充值的金钱
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "修改成功"}
	 * @remark 用户接口
	 * @number 99
	 **/
	@PostMapping(value = "/edit")
	@ApiOperation(value = "余额修改", tags = {"用户接口"}, notes = "余额修改")
	public RestResponseBean edit(@RequestParam String id, @RequestParam BigDecimal money) {
		UserWallet userWallet = new UserWallet();
		Result<UserWallet> result = new Result<UserWallet>();
		UserWallet userWalletEntity = userWalletService.getById(userWallet.getId());
		if(userWalletEntity==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应的实体");
		}else {
			boolean ok = userWalletService.updateById(userWallet);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"修改成功");
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");
	}

	/**
	 *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<UserWallet> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserWallet> result = new Result<UserWallet>();
		UserWallet userWallet = userWalletService.getById(id);
		if(userWallet==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userWalletService.removeById(id);
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
	public Result<UserWallet> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserWallet> result = new Result<UserWallet>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userWalletService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserWallet> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserWallet> result = new Result<UserWallet>();
		UserWallet userWallet = userWalletService.getById(id);
		if(userWallet==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userWallet);
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
		QueryWrapper<UserWallet> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserWallet userWallet = JSON.parseObject(deString, UserWallet.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userWallet, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserWallet> pageList = userWalletService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户钱包管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserWallet.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户钱包管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserWallet> listUserWallets = ExcelImportUtil.importExcel(file.getInputStream(), UserWallet.class, params);
				for (UserWallet userWalletExcel : listUserWallets) {
					userWalletService.save(userWalletExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserWallets.size());
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
