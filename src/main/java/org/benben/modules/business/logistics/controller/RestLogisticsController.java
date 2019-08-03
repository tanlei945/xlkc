package org.benben.modules.business.logistics.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.ExpressUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.logistics.entity.Logistics;
import org.benben.modules.business.logistics.service.ILogisticsService;
import org.benben.modules.business.logistics.vo.LogisticsVo;
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
 * @Description: 物流信息管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/logistics")
@Api(tags = {"物流接口"})
@Slf4j
public class RestLogisticsController {
	@Autowired
	private ILogisticsService logisticsService;

	@Autowired
	private IBooksService booksService;

	/**
	 * showdoc
	 * @catalog 物流接口
	 * @title 得到物流信息
	 * @description 得到物流信息接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/logistics/getExpress
	 * @param bookId String 商品书籍id
	 * @return {"code": 1,"msg": "操作成功","time": "1562212106868","data": {"id": "1","name": "心理学课程","bookUrl": "","waybill": "806325354023412007","express": "圆通快递","orderId": "1","json": "{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"806325354023412007\",\"type\":\"yto\",\"list\":[{\"time\":\"2019-6-12 18:24:24\",\"status\":\"客户 签收人: 郑得旺 已签收 感谢使用圆通速递，期待再次为您服务\"},{\"time\":\"2019-6-12 6:57:10\",\"status\":\"【河南省郑州市绿城广场公司】 派件人: 张凯强 派件中 派件员电话19103822527\"},{\"time\":\"2019-6-12 6:56:28\",\"status\":\"【河南省郑州市绿城广场公司】 已收入\"},{\"time\":\"2019-6-11 21:03:00\",\"status\":\"【郑州转运中心】 已发出 下一站 【河南省郑州市绿城广场公司】\"},{\"time\":\"2019-6-11 20:55:47\",\"status\":\"【郑州转运中心】 已收入\"},{\"time\":\"2019-6-10 19:46:40\",\"status\":\"【温州转运中心】 已发出 下一站 【郑州转运中心】\"},{\"time\":\"2019-6-10 19:45:25\",\"status\":\"【温州转运中心】 已收入\"},{\"time\":\"2019-6-10 18:00:52\",\"status\":\"【浙江省温州市平阳县】 已发出 下一站 【温州转运中心】\"},{\"time\":\"2019-6-10 17:55:03\",\"status\":\"【浙江省温州市平阳县公司】 已打包\"},{\"time\":\"2019-6-10 17:45:55\",\"status\":\"【浙江省温州市平阳县公司】 已收件\"}],\"deliverystatus\":\"3\",\"issign\":\"1\",\"expName\":\"圆通速递\",\"expSite\":\"www.yto.net.cn \",\"expPhone\":\"95554\",\"logo\":\"http:\\/\\/img3.fegine.com\\/express\\/yto.jpg\",\"courier\":\"\",\"courierPhone\":\"\"}}"}}
	 * @remark 物流接口
	 * @number 99
	 **/
	@GetMapping("/getExpress")
	@ApiOperation(value = "得到物流信息", tags = {"物流接口"}, notes = "得到物流信息")
	public RestResponseBean getExpress(@RequestParam String orderId) {
		LogisticsVo logisticsVo = new LogisticsVo();
		Logistics logistics = logisticsService.queryById(orderId);
		if (logistics == null) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),logisticsVo);
		}
		Books book = booksService.getById(logistics.getBookId());
		if (book == null || logistics == null) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),logisticsVo);
		}

		logisticsVo.setName(book.getName());
		logisticsVo.setBookUrl(book.getBookUrl());
		logisticsVo.setExpress(logistics.getExpress());
		logisticsVo.setId(logistics.getId());
		logisticsVo.setWaybill(logistics.getWaybill());
		logisticsVo.setOrderId(logistics.getOrderId());
		logisticsVo.setJson(ExpressUtil.express(logisticsVo.getWaybill()));

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),logisticsVo);
	}

	/**
	 * 分页列表查询
	 * @param logistics
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Logistics>> queryPageList(Logistics logistics,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<Logistics>> result = new Result<IPage<Logistics>>();
		QueryWrapper<Logistics> queryWrapper = QueryGenerator.initQueryWrapper(logistics, req.getParameterMap());
		Page<Logistics> page = new Page<Logistics>(pageNo, pageSize);
		IPage<Logistics> pageList = logisticsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param logistics
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Logistics> add(@RequestBody Logistics logistics) {
		Result<Logistics> result = new Result<Logistics>();
		try {
			logisticsService.save(logistics);
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
	 * @param logistics
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Logistics> edit(@RequestBody Logistics logistics) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logisticsEntity = logisticsService.getById(logistics.getId());
		if(logisticsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = logisticsService.updateById(logistics);
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
	public Result<Logistics> delete(@RequestParam(name="id",required=true) String id) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logistics = logisticsService.getById(id);
		if(logistics==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = logisticsService.removeById(id);
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
	public Result<Logistics> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Logistics> result = new Result<Logistics>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.logisticsService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Logistics> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Logistics> result = new Result<Logistics>();
		Logistics logistics = logisticsService.getById(id);
		if(logistics==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(logistics);
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
		QueryWrapper<Logistics> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Logistics logistics = JSON.parseObject(deString, Logistics.class);
				queryWrapper = QueryGenerator.initQueryWrapper(logistics, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Logistics> pageList = logisticsService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "物流信息管理列表");
		mv.addObject(NormalExcelConstants.CLASS, Logistics.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物流信息管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<Logistics> listLogisticss = ExcelImportUtil.importExcel(file.getInputStream(), Logistics.class, params);
				for (Logistics logisticsExcel : listLogisticss) {
					logisticsService.save(logisticsExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listLogisticss.size());
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
