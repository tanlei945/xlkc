package org.benben.modules.business.courseassistant.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.course.vo.CoursesConfirmVo;
import org.benben.modules.business.courseassistant.entity.CourseAssistant;
import org.benben.modules.business.courseassistant.service.ICourseAssistantService;
import org.benben.modules.business.courseassistant.vo.CourseAssistantTimeVo;
import org.benben.modules.business.courseassistant.vo.HomeWorkAchieveVo;
import org.benben.modules.business.courseassistanttime.entity.CourseAssistanTime;
import org.benben.modules.business.courseassistanttime.service.ICourseAssistanTimeService;
import org.benben.modules.business.courseassistanttime.vo.TimeVo;
import org.benben.modules.business.coursetime.vo.CourseTimeVo;
import org.benben.modules.business.user.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: Controller
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/courseAssistant")
@Api(tags = {"助教接口"})
@Slf4j
public class RestCourseAssistantController {
	@Autowired
	private ICourseAssistantService courseAssistantService;

	@Autowired
	private ICourseAssistanTimeService timeService;

	@Autowired
	private ICourseService courseService;

	/**
	 * showdoc
	 * @catalog 助教接口
	 * @title 课程已完成列表
	 * @description 课程已完成列表接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseAssistant/courseAchieve
	 *@param pageNumber int 分页从第几个开始
	 *@param pageSize int 一页显示几个
	 * @return {"code": 1,"msg": "操作成功","time": "1561337860029","data": {"total": 2,"courseConfirmVos": [{"id": "297e017e6ac9c038016ac9c038ef0000","courseName": "NLPP","comment": "关于NLPP的课程简介","starttime": "2019-05-05","endtime": "2019-05-17","address": "大唐西市"},{"id": "1","courseName": "MMLP","comment": "关于NLPP的课程简介","starttime": "2019-02-04","endtime": "2019-02-12","address": "大唐西市"}]}}
	 * @remark 助教接口
	 * @number 99
	 **/
	@PostMapping("/courseAchieve")
	@ApiOperation(value = "课程已完成（模糊查询）列表",tags = {"助教接口"}, notes = "课程已完成（模糊查询）列表")
	public RestResponseBean coureseAchieve(@RequestParam Integer pageNumber,@RequestParam Integer pageSize, String name){
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (name == null) {
			name = "";
		}
		CoursesConfirmVo coourses = courseService.courseAssistantAchieve(name, user.getId(),pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),coourses);
	}
	@GetMapping("/homeworkAchieve")
	@ApiOperation(value = "课程已完成功课列表", tags = {"助教接口"}, notes = "课程已完成功课列表")
	public RestResponseBean homeworkAchieve() {

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<HomeWorkAchieveVo> list = courseAssistantService.homeworkAchieve(user.getId());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
	}

	@PostMapping("/homeworkById")
	@ApiOperation(value = "通过课程id得到添加功课的信息", tags = {"助教接口"}, notes = "通过课程id得到添加功课的信息")
	public RestResponseBean homeworkById(@RequestParam String courseId) {
		HomeWorkAchieveVo homework = courseAssistantService.homeworkById(courseId);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),homework);
	}

	/**
	 * showdoc
	 * @catalog 助教接口
	 * @title 得到名称和日期
	 * @description 得到名称和日期接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseAssistant/queeryId
	 *@param id int 课程id
	 * @return {"code": 1,"msg": "操作成功","time": "1561339921201","data": {"id": "297e017e6ac9c038016ac9c038ef0000","courseName": "NLPP","dates": ["2019-06-04"]}}
	 * @remark 助教接口
	 * @number 99
	 **/
	@PostMapping("/queeryId")
	@ApiOperation(value = "得到名称和日期", tags = {"助教接口"},notes = "得到名称和日期")
	public RestResponseBean queryId(@RequestParam String id) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		CourseTimeVo courseTimeVo = courseAssistantService.queryId(user.getId(),id);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(), courseTimeVo);
	}

	/**
	 * 分页列表查询
	 * @param courseAssistant
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<CourseAssistant>> queryPageList(CourseAssistant courseAssistant,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<CourseAssistant>> result = new Result<IPage<CourseAssistant>>();
		QueryWrapper<CourseAssistant> queryWrapper = QueryGenerator.initQueryWrapper(courseAssistant, req.getParameterMap());
		Page<CourseAssistant> page = new Page<CourseAssistant>(pageNo, pageSize);
		IPage<CourseAssistant> pageList = courseAssistantService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * showdoc
	 * @catalog 助教接口
	 * @title 添加助教
	 * @description 添加助教
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/courseAssistant/add
	 * @param company String 所属公司名称
	 * @param courseId String 课程id
	 * @param forte String 个人长处
	 * @param language int 语言种类0、普通话1、粤语
	 * @param picture String 照片存放地址
	 * @param skill String 对学习者的贡献
	 * @param state int 0/申请失败1/成功
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 助教接口
	 * @number 99
	 **/
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加助教", tags = {"助教接口"}, notes = "添加助教")
	public RestResponseBean add(@RequestBody CourseAssistantTimeVo courseAssistanTimes) {
		CourseAssistant courseAssistant = new CourseAssistant();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		BeanUtils.copyProperties(courseAssistanTimes,courseAssistant);
		courseAssistant.setUserId(user.getId());
		courseAssistantService.save(courseAssistant);
		Map maps = new HashMap();

		//添加课程助教时间
		List<TimeVo> timeVoList = courseAssistanTimes.getTimeVoList();
		for (TimeVo timeVo : timeVoList) {
			maps.put(timeVo.getDate(),timeVo.getDays());
		}

		System.out.println(maps);

		for (Object o : maps.keySet()) {
			CourseAssistanTime courseAssistanTime = new CourseAssistanTime();
			try {

				courseAssistanTime.setDate((Date) o);
				courseAssistanTime.setUid(user.getId());
				courseAssistanTime.setCourseAssistantId(courseAssistant.getId());
				timeService.save(courseAssistanTime);
				List<Integer> dayList = (List<Integer>) maps.get(o);
				for (Integer integer : dayList) {
					CourseAssistanTime courseAssistanTime1 = new CourseAssistanTime();
					courseAssistanTime1.setParentId(courseAssistanTime.getId());
					courseAssistanTime1.setUid(user.getId());
					courseAssistanTime1.setDay(integer);
					timeService.save(courseAssistanTime1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	/*	try {

			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}*/
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"添加成功");
	}

	/**
	 *  编辑
	 * @param courseAssistant
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<CourseAssistant> edit(@RequestBody CourseAssistant courseAssistant) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistantEntity = courseAssistantService.getById(courseAssistant.getId());
		if(courseAssistantEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseAssistantService.updateById(courseAssistant);
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
	public Result<CourseAssistant> delete(@RequestParam(name="id",required=true) String id) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistant = courseAssistantService.getById(id);
		if(courseAssistant==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = courseAssistantService.removeById(id);
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
	public Result<CourseAssistant> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.courseAssistantService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<CourseAssistant> queryById(@RequestParam(name="id",required=true) String id) {
		Result<CourseAssistant> result = new Result<CourseAssistant>();
		CourseAssistant courseAssistant = courseAssistantService.getById(id);
		if(courseAssistant==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(courseAssistant);
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
		QueryWrapper<CourseAssistant> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				CourseAssistant courseAssistant = JSON.parseObject(deString, CourseAssistant.class);
				queryWrapper = QueryGenerator.initQueryWrapper(courseAssistant, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<CourseAssistant> pageList = courseAssistantService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "关于助教的申请列表");
		mv.addObject(NormalExcelConstants.CLASS, CourseAssistant.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("关于助教的申请列表数据", "导出人:Jeecg", "导出信息"));
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
				List<CourseAssistant> listCourseAssistants = ExcelImportUtil.importExcel(file.getInputStream(), CourseAssistant.class, params);
				for (CourseAssistant courseAssistantExcel : listCourseAssistants) {
					courseAssistantService.save(courseAssistantExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listCourseAssistants.size());
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
