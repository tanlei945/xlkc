package org.benben.modules.business.userpayevdence.controller;

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
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.benben.modules.business.userpayevdence.entity.UserPayEvdence;
import org.benben.modules.business.userpayevdence.service.IUserPayEvdenceService;
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
 * @Description: 用户支付凭证管理
 * @author： jeecg-boot
 * @date：   2019-06-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/userPayEvdence")
@Api(tags = {"用户接口"})
@Slf4j
public class RestUserPayEvdenceController {
	@Autowired
	private IUserPayEvdenceService userPayEvdenceService;
	@Autowired
	private ICoursePerInformationService coursePerInformationService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private ICourseService courseService;
	@Autowired
	private IBooksService booksService;
	@Autowired
	private IUserCourseService userCourseService;

	/**
	 * 分页列表查询
	 * @param userPayEvdence
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserPayEvdence>> queryPageList(UserPayEvdence userPayEvdence,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<UserPayEvdence>> result = new Result<IPage<UserPayEvdence>>();
		QueryWrapper<UserPayEvdence> queryWrapper = QueryGenerator.initQueryWrapper(userPayEvdence, req.getParameterMap());
		Page<UserPayEvdence> page = new Page<UserPayEvdence>(pageNo, pageSize);
		IPage<UserPayEvdence> pageList = userPayEvdenceService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 添加支付凭证
	 * @description 添加支付凭证接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/userPayEvdence/add
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 用户接口
	 * @number 99
	 */
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加支付凭证",tags = "用户接口",notes = "添加支付凭证")
	public RestResponseBean add(@RequestBody UserPayEvdence userPayEvdence) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		userPayEvdence.setUid(user.getId());
		//首先，判断是书籍订单id，还是预约课程id
		Order order = orderService.getById(userPayEvdence.getOrderId());
		if (order == null) {
			CoursePerInformation coursePerInformation = coursePerInformationService.getById(userPayEvdence.getOrderId());
			//添加到我的课程
			UserCourse userCourse = new UserCourse();
			userCourse.setUid(user.getId());
			userCourse.setCourseVerify(0);
			userCourse.setStatus(3);
			userCourse.setCourseId(coursePerInformation.getCourseId());



			//首先查询我的课程是否存在我的课程是否存在
			UserCourse userCourse1 = userCourseService.queryByCourse(user.getId(), coursePerInformation.getCourseId());
			if (userCourse1 != null) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"该课程已经预约过");
			} else {
				//修改课程预约数量
				Course course = new Course();
				course.setId(coursePerInformation.getCourseId());
				Course byId = courseService.getById(course.getId());
				byId.setApplyNum(byId.getApplyNum()+1);
				if (byId.getNum() > 0) {

					byId.setNum(byId.getNum()-1);
				} else {

					return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),"已经没有库存");
				}

				courseService.updateById(byId);
				userCourseService.save(userCourse);
			}
			userPayEvdence.setOrderId(null);
			userPayEvdence.setUserCourseId(userCourse.getCourseId());
		} else {
			Books books = new Books();
			//通过orderId得到书籍
			Order order1 = orderService.getById(userPayEvdence.getOrderId());
			if (order1 != null) {
				//改变库存量和购买人数
				Books books1 = booksService.getById(order1.getBookId());
				if(books1.getBookNum() > 0) {
					books1.setBookNum(books1.getBookNum() - 1);
				} else{
					return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),"已经没有库存");
				}
				books1.setNum(books1.getNum()+1);
				booksService.updateById(books1);
			}
		}

		try {
			userPayEvdenceService.save(userPayEvdence);
			result.success("添加成功！");
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"操作失败");

		}
	}

	/**
	 *  编辑
	 * @param userPayEvdence
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserPayEvdence> edit(@RequestBody UserPayEvdence userPayEvdence) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		UserPayEvdence userPayEvdenceEntity = userPayEvdenceService.getById(userPayEvdence.getId());
		if(userPayEvdenceEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userPayEvdenceService.updateById(userPayEvdence);
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
	public Result<UserPayEvdence> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		UserPayEvdence userPayEvdence = userPayEvdenceService.getById(id);
		if(userPayEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userPayEvdenceService.removeById(id);
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
	public Result<UserPayEvdence> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userPayEvdenceService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserPayEvdence> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserPayEvdence> result = new Result<UserPayEvdence>();
		UserPayEvdence userPayEvdence = userPayEvdenceService.getById(id);
		if(userPayEvdence==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userPayEvdence);
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
		QueryWrapper<UserPayEvdence> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				UserPayEvdence userPayEvdence = JSON.parseObject(deString, UserPayEvdence.class);
				queryWrapper = QueryGenerator.initQueryWrapper(userPayEvdence, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<UserPayEvdence> pageList = userPayEvdenceService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "用户支付凭证管理列表");
		mv.addObject(NormalExcelConstants.CLASS, UserPayEvdence.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户支付凭证管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<UserPayEvdence> listUserPayEvdences = ExcelImportUtil.importExcel(file.getInputStream(), UserPayEvdence.class, params);
				for (UserPayEvdence userPayEvdenceExcel : listUserPayEvdences) {
					userPayEvdenceService.save(userPayEvdenceExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listUserPayEvdences.size());
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
