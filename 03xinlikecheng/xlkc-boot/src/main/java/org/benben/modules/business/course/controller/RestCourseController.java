package org.benben.modules.business.course.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.course.vo.*;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.posts.vo.PostsVos;
import org.benben.modules.business.user.entity.User;
import org.hibernate.validator.constraints.pl.REGON;
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
import org.w3c.dom.ls.LSInput;

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
 * @Description: 课程的相关管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/course")
@Api(tags = {"课程接口"})
@Slf4j
public class RestCourseController {
	@Autowired
	private ICourseService courseService;
	@Autowired
	private ICoursePerInformationService coursePerInformationService;

	@PostMapping("/getCourseName")
	public RestResponseBean getCourseName() {
		List<CourseVo> courseNames = courseService.getCourseName();
		return new RestResponseBean(ResultEnum.OPERATION_CORRECT.getValue(),ResultEnum.OPERATION_CORRECT.getDesc(),courseNames);
	}

	@GetMapping("/verifyCourse")
	@ApiOperation(value = "预约课程校验", tags = {"课程接口"}, notes = "预约课程校验")
	public RestResponseBean verifyCourse(@RequestParam String courseId) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		CoursePerInformation coursePerInformation1 = coursePerInformationService.queryByPerInformation(user.getId(),courseId);
		if (coursePerInformation1 != null) {
			return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"你已经预约过该课程");
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"可以预约");
		}
	}

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 课程列表显示
	 * @description 课程列表显示接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/course/listPage
	 * @return {"code": 1,"msg": "操作成功","time": "1562295569490","data": {"total": 4,"videoList": [{ 	"id": "1", 	"courseName": "MMLP", 	"address": "大唐西市", 	"num": 50, 	"starttime": "2019-02-04", 	"endtime": "2019-02-12"},{ 	"id": "2", 	"courseName": "NLPP", 	"address": "大唐西市", 	"num": 50, 	"starttime": "2019-02-01", 	"endtime": "2019-02-12"},{ 	"id": "297e017e6ac9c038016ac9c038ef0000", 	"courseName": "NLPP", 	"address": "大唐西市", 	"num": 50, 	"starttime": "2019-05-05", 	"endtime": "2019-05-17"},{ 	"id": "6b84052c790db7c5bc1014008ba0b2d1", 	"courseName": "NLPMP", 	"address": "香港", 	"num": 12, 	"starttime": "2019-06-21", 	"endtime": "2019-06-28"}]}}
	 * @param pageNumber 必须 int 分页从第几个开始
	 * @param pageSize 必须 int 一页显示几个
	 * @return_param courseName String 课程名称
	 * @return_param num Integer 课程人数
	 * @return_param starttime Date 上课开始时间
	 * @return_param endtime Date 课程结束时间
	 * @return_param address String 上课地址
	 * @remark 课程接口
	 * @number 99
	 **/
	@GetMapping("/listPage")
	@ApiOperation(value = "课程列表（模糊查询，类型）显示",tags = {"课程接口"}, notes = "课程列表（模糊查询，类型）显示")
	public RestResponseBean listPage(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,  String name, String typeId){
		if(StringUtils.isNotBlank(name) && StringUtils.isBlank(typeId)) {
			CoursesVo coursesVo = courseService.queryByName(name,pageNumber,pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coursesVo);
		} else if (StringUtils.isNotBlank(typeId) && StringUtils.isBlank(name)) {
			CoursesVo course = courseService.queryById(typeId,  pageNumber,  pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),course);
		} else if (StringUtils.isBlank(typeId) && StringUtils.isBlank(name)) {
			if (name == null) {
				name = "";
			}
			CoursesVo coursesVos = courseService.queryCourse(pageNumber,pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coursesVos);
		} else {
			CoursesVo courseVo = courseService.queryCourses(typeId,name,pageNumber,pageSize);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),courseVo);
		}

	}

	/**
	 * 分页列表查询
	 * @param course
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Course>> queryPageList(Course course,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<Course>> result = new Result<IPage<Course>>();
		QueryWrapper<Course> queryWrapper = QueryGenerator.initQueryWrapper(course, req.getParameterMap());
		Page<Course> page = new Page<Course>(pageNo, pageSize);
		IPage<Course> pageList = courseService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param course
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Course> add(@RequestBody Course course) {
		Result<Course> result = new Result<Course>();
		try {
			courseService.save(course);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}

	@PutMapping(value = "/edit")
//	@ApiOperation(value = "修改预约课程的状态",tags = "课程接口",notes = "修改预约课程的状态")
	public Result<Course> edit() {
		Result<Course> result = new Result<Course>();
		Course course = new Course();
		Course courseEntity = courseService.getById(course.getId());
		if(courseEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseService.updateById(course);
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
	public Result<Course> delete(@RequestParam(name="id",required=true) String id) {
		Result<Course> result = new Result<Course>();
		Course course = courseService.getById(id);
		if(course==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseService.removeById(id);
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
	public Result<Course> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Course> result = new Result<Course>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * showdoc
	 * @catalog 课程接口
	 * @title 查看课程详情
	 * @description 查看课程详情接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/course/queryById
	 * @return {"code": 1,"msg": "操作成功","time": "1562228650621","data": {"id": "297e017e6ac9c038016ac9c038ef0000","courseName": "NLPP","comment": "关于NLPP的课程简介","chinaPrice": 2000,"hkPrice": 2261.4,"num": 50,"starttime": "2019-05-05 19:21:12","endtime": "2019-05-17 23:21:12","address": "大唐西市","language": 0,"intro": "上课的简介","courseContent": "发电房撒防守打法搜到","pictureUrl": "轮播图地址","courseImg": null,"videoUrl": "视频地址","courseVerify": 1,"courseRefund": 0,"achieve": 1,"level": 1,"numCourse": 4,"dayCourse": 3,"bukeNum": 10,"createTime": "2019-05-18 15:02:59","createBy": "324","updateTime": "2019-06-24 08:57:39","updateBy": "34234","courseType": "","assistantNumber": 10,"enterNum": null,"openingTime": null,"expirationTime": null},"timestamp": 1561339086273}
	 * @param id 必须 int 课程id
	 * @return_param courseName String 课程名称
	 * @return_param chinaPrice BigDecimal 课程人民币价格
	 * @return_param hkPrice BigDecimal 课程港币价格
	 * @return_param num Integer 课程人数
	 * @return_param starttime Date 上课开始时间
	 * @return_param endtime Date 课程结束时间
	 * @return_param address String 上课地址
	 * @return_param language Integer 语种，0/普通话，1/粤语
	 * @return_param intro String 上课简介(富文本)
	 * @return_param pictureUrl String 课程轮播图
	 * @return_param courseImg String 课程封面地址
	 * @return_param videoUrl String 课程视频地址
	 * @return_param level Integer 课程等级
	 * @return_param bukeNum Integer 课程补课的天数
	 * @return_param assistantNumber Integer 可以申请助教人数
	 * @return_param openingTime Date 助教开通时间
	 * @return_param expirationTime Date 助教截止时间
	 * @remark 课程接口
	 * @number 99
	 **/
	@GetMapping(value = "/queryById")
	@ApiOperation(value = "查看课程详情",tags = {"课程接口"}, notes = "查看课程详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<Course> result = new Result<Course>();
		Course course = courseService.getById(id);

		String bookUrl = course.getPictureUrl();
		List<String> list = new ArrayList<>();
		list = Arrays.asList(bookUrl.split(","));

		Courses courses = new Courses();
		BeanUtils.copyProperties(course,courses);
		courses.setPictureUrl(list);

		if(course==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),courses);
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
		QueryWrapper<Course> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Course course = JSON.parseObject(deString, Course.class);
				queryWrapper = QueryGenerator.initQueryWrapper(course, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Course> pageList = courseService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "课程的相关管理列表");
		mv.addObject(NormalExcelConstants.CLASS, Course.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("课程的相关管理列表数据", "导出人:Jeecg", "导出信息"));
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
				List<Course> listCourses = ExcelImportUtil.importExcel(file.getInputStream(), Course.class, params);
				for (Course courseExcel : listCourses) {
					courseService.save(courseExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listCourses.size());
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
