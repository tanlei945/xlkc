package org.benben.modules.business.courseperinformation.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.user.entity.User;
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
 * @Description: 预约课程个人信息管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/coursePerInformation")
@Api(tags = {"课程接口"})
@Slf4j
public class RestCoursePerInformationController {
	@Autowired
	private ICoursePerInformationService coursePerInformationService;

	/**
	 * 分页列表查询
	 * @param coursePerInformation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CoursePerInformation>> queryPageList(CoursePerInformation coursePerInformation,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<CoursePerInformation>> result = new Result<IPage<CoursePerInformation>>();
		QueryWrapper<CoursePerInformation> queryWrapper = QueryGenerator.initQueryWrapper(coursePerInformation, req.getParameterMap());
		Page<CoursePerInformation> page = new Page<CoursePerInformation>(pageNo, pageSize);
		IPage<CoursePerInformation> pageList = coursePerInformationService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 添加预约课程个人信息
	 * @description 添加预约课程个人信息接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/coursePerInformation/add
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @param address String 课程名字
	 * @param company int 分页从第几个开始
	 * @param courseId int 课程id
	 * @param email int 一页显示几个
	 * @param name int 报名人名称
	 * @param phone int 联系电话
	 * @param referrer int 推荐人
	 * @param voucher int 凭证确认1/是0/否认
	 * @remark 课程接口
	 * @number 99
	 **/
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加预约课程个人信息",tags = {"课程接口"},notes = "添加预约课程个人信息")
	public RestResponseBean add(@RequestBody CoursePerInformation coursePerInformation) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		if (coursePerInformation == null || StringUtils.isBlank(coursePerInformation.getCourseId()) || StringUtils.isBlank(coursePerInformation.getPhone())) {
			return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"请添加数据");
		}
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		coursePerInformation.setUid(user.getId());

		//首先查询该用户是否已经预约过该课程
		CoursePerInformation coursePerInformation1 = coursePerInformationService.queryByPerInformation(user.getId(), coursePerInformation.getCourseId());
		if (coursePerInformation1 != null) {
			return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"你已经预约过该课程");
		}
		//		coursePerInformation.setVoucher(0);
		try {
			coursePerInformationService.save(coursePerInformation);
			result.success("添加成功！");
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coursePerInformation.getId());
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"添加失败");
		}
	}

	/**
	 *  编辑
	 * @param coursePerInformation
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CoursePerInformation> edit(@RequestBody CoursePerInformation coursePerInformation) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformationEntity = coursePerInformationService.getById(coursePerInformation.getId());
		if(coursePerInformationEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = coursePerInformationService.updateById(coursePerInformation);
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
	public Result<CoursePerInformation> delete(@RequestParam(name="id",required=true) String id) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformation = coursePerInformationService.getById(id);
		if(coursePerInformation==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = coursePerInformationService.removeById(id);
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
	public Result<CoursePerInformation> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.coursePerInformationService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<CoursePerInformation> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CoursePerInformation> result = new Result<CoursePerInformation>();
		CoursePerInformation coursePerInformation = coursePerInformationService.getById(id);
		if(coursePerInformation==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(coursePerInformation);
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
		QueryWrapper<CoursePerInformation> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				CoursePerInformation coursePerInformation = JSON.parseObject(deString, CoursePerInformation.class);
				queryWrapper = QueryGenerator.initQueryWrapper(coursePerInformation, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<CoursePerInformation> pageList = coursePerInformationService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "预约课程个人信息管理列表");
		mv.addObject(NormalExcelConstants.CLASS, CoursePerInformation.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("预约课程个人信息管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<CoursePerInformation> listCoursePerInformations = ExcelImportUtil.importExcel(file.getInputStream(), CoursePerInformation.class, params);
				for (CoursePerInformation coursePerInformationExcel : listCoursePerInformations) {
					coursePerInformationService.save(coursePerInformationExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listCoursePerInformations.size());
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
