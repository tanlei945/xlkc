package org.benben.modules.business.homework.controller;

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
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.homework.entity.Homework;
import org.benben.modules.business.homework.service.IHomeworkService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.homework.vo.HomeWorkVo;
import org.benben.modules.business.homework.vo.HomeworkCourseVo;
import org.benben.modules.business.homework.vo.Homeworks;
import org.benben.modules.business.homework.vo.MyHomeWork;
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
import com.alibaba.fastjson.JSON;

/**
 * @Title: Controller
 * @Description: 我的功课管理
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/homework")
@Api(tags = "我的功课")
@Slf4j
public class RestHomeworkController {
	@Autowired
	private IHomeworkService homeworkService;

	@Autowired
	private ICourseService courseService;

	/**
	 * showdoc
	 * @catalog 我的功课接口
	 * @title 得到完成课程下的小组名称
	 * @description 得到完成课程下的小组名称接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/homework/queryGroupName
	 * @param courseId String 课程id
	 * @return {"code": 1,"msg": "操作成功","time": "1562207702636","data": ["第一小组","第二小组"]}
	 * @remark 我的功课接口
	 * @number 99
	 **/
	@PostMapping("/queryGroupName")
	@ApiOperation(value = "得到完成课程下的小组名称", tags = "我的功课", notes = "得到完成课程下的小组名称")
	public RestResponseBean queryGroupName(@RequestParam String courseId){
		List<String> groupNames = homeworkService.queryGroupName(courseId);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),groupNames);
	}

	/**
	 * showdoc
	 * @catalog 我的功课接口
	 * @title 查询完成功课的信息名称
	 * @description 查询完成功课的信息名称接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/homework/queryGroupName
	 * @param courseId String 课程id
	 * @return {"code": 1,"msg": "操作成功","time": "1562208900784","data": {"id": "297e017e6ac9c038016ac9c038ef0000","courseName": "NLPP","starttime": "2019-05-05 ","endtime": "2019-05-17 "}}
	 * @remark 我的功课接口
	 * @number 99
	 **/
	@PostMapping("/queryByCourseIdAndAchieve")
	@ApiOperation(value = "查询我的完成功课的信息名称", tags = "我的功课", notes = "查询完成功课的信息名称")
	public RestResponseBean queryByCourseIdAndAchieve(@RequestParam String courseId){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (courseId == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		HomeworkCourseVo homeworkCourseVo = courseService.queryByCourseIdAndAchieve(courseId, user.getId());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),homeworkCourseVo);
	}

	/**
	 * showdoc
	 * @catalog 我的功课接口
	 * @title 查询全部功课信息
	 * @description 查询全部功课信息接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/homework/queryByUidAndCourseId
	 * @param courseId String 课程id
	 * @return {"code": 1,"msg": "操作成功","time": "1562209881540","data": [{"id": "1","userId": "f654ba74348da755785ac5d50bd871ef","courseId": "297e017e6ac9c038016ac9c038ef0000","groupName": "第一小组","crew": "张三，李四，王二，赵五","wordUrl": "asd","wordName": "Nll课程文档","pictureUrl": null,"pictureName": null,"voiceUrl": null,"voiceName": null,"videoUrl": null,"videoName": null,"createTime": null,"createBy": null,"updateTime": null,"updateBy": null},{"id": "63f76d0ad1b50c611366d55f89deafc1","userId": "f654ba74348da755785ac5d50bd871ef","courseId": "297e017e6ac9c038016ac9c038ef0000","groupName": "第二小组","crew": "张三，李四，王二，赵五","wordUrl": "ssss","wordName": "ssss","pictureUrl": "123a","pictureName": "aaa","voiceUrl": "vvvv","voiceName": "cccc","videoUrl": "ooooooo","videoName": "aaaa","createTime": "2019-06-06 15:13:55","createBy": null,"updateTime": "2019-06-17 15:14:57","updateBy": "superadmin"}]}
	 * @remark 我的功课接口
	 * @number 99
	 **/
	 @PostMapping(value = "/queryByUidAndCourseId")
	 @ApiOperation(value = "查询我的全部功课信息",tags = "我的功课", notes = "查询全部功课信息")
	public RestResponseBean queryHomework(){
		 User user = (User) SecurityUtils.getSubject().getPrincipal();
		 String uid = user.getId();
		if (uid == null ){
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		 List<MyHomeWork> homework = homeworkService.queryByUidAndCourseId(uid);
		 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), homework);
	}

	/**
	  * 分页列表查询
	 * @param homework
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Homework>> queryPageList(Homework homework,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Homework>> result = new Result<IPage<Homework>>();
		QueryWrapper<Homework> queryWrapper = QueryGenerator.initQueryWrapper(homework, req.getParameterMap());
		Page<Homework> page = new Page<Homework>(pageNo, pageSize);
		IPage<Homework> pageList = homeworkService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * showdoc
	 * @catalog 我的功课接口
	 * @title 提交功课信息
	 * @description 提交功课信息接口
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/homework/add
	 * @param courseId String 课程id
	 * @param crew String 组员名称
	 * @param groupName String 小组名称
	 * @param pictureUrl String 图片地址
	 * @param videoUrl String 视频地址
	 * @param voiceUrl String 音频地址
	 * @param wordUrl String 文档地址
	 * @return {"code": 1,"msg": "操作成功","time": "1562229972433","data": "添加成功"}
	 * @remark 我的功课接口
	 * @number 99
	 **/
	@PostMapping(value = "/add")
	@ApiOperation(value = "提交功课信息",tags = "我的功课", notes = "提交功课信息")
	public RestResponseBean add(@RequestBody HomeWorkVo homeWorkVo) {
		Result<Homework> result = new Result<Homework>();
		Homework homework = new Homework();
		BeanUtils.copyProperties(homeWorkVo,homework);

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		homework.setUserId(user.getId());
		try {
			homeworkService.save(homework);
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
	 * @param homework
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Homework> edit(@RequestBody Homework homework) {
		Result<Homework> result = new Result<Homework>();
		Homework homeworkEntity = homeworkService.getById(homework.getId());
		if(homeworkEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = homeworkService.updateById(homework);
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
	public Result<Homework> delete(@RequestParam(name="id",required=true) String id) {
		Result<Homework> result = new Result<Homework>();
		Homework homework = homeworkService.getById(id);
		if(homework==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = homeworkService.removeById(id);
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
	public Result<Homework> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Homework> result = new Result<Homework>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.homeworkService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * showdoc
	 * @catalog 我的功课接口
	 * @title 根据id查询功课的详细信息
	 * @description 根据id查询功课的详细信息接口
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/homework/add
	 * @param id String 课程id
	 * @return {"code": 1,"msg": "操作成功","time": "1562228650621","data": {"id": "9ba163aadc49902dcf0b20cd99d5fcde","userId": "f654ba74348da755785ac5d50bd871ef","courseId": "1","groupName": "sss","crew": "aaa","wordUrl": "rfv","wordName": null,"pictureUrl": "qaz","pictureName": null,"voiceUrl": "edc","voiceName": null,"videoUrl": "wsx","videoName": null,"createTime": "2019-07-04 11:25:19","createBy": null,"updateTime": null,"updateBy": null},"timestamp": 1562211199936}
	 * @remark 我的功课接口
	 * @number 99
	 **/
	@GetMapping(value = "/queryById")
	@ApiOperation(value = "根据id查询功课的详细信息", tags = "我的功课", notes = "根据id查询功课的详细信息")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<Homework> result = new Result<Homework>();
		Homework homework = homeworkService.getById(id);
		Homeworks homeworks = new Homeworks();
		Course course = courseService.getById(homework.getCourseId());
		BeanUtils.copyProperties(homework,homeworks);
		homeworks.setCourseName(course.getCourseName());
		if(homework==null) {
			result.error500("未找到对应实体");
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应的实体");
		}else {
			result.setResult(homework);
			result.setSuccess(true);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),homeworks);
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
      QueryWrapper<Homework> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Homework homework = JSON.parseObject(deString, Homework.class);
              queryWrapper = QueryGenerator.initQueryWrapper(homework, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Homework> pageList = homeworkService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "我的功课管理列表");
      mv.addObject(NormalExcelConstants.CLASS, Homework.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("我的功课管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Homework> listHomeworks = ExcelImportUtil.importExcel(file.getInputStream(), Homework.class, params);
              for (Homework homeworkExcel : listHomeworks) {
                  homeworkService.save(homeworkExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listHomeworks.size());
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
