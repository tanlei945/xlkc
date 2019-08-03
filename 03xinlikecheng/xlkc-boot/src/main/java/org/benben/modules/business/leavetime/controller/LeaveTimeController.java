package org.benben.modules.business.leavetime.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.deliveryaddress.vo.DeliveryaddresVo;
import org.benben.modules.business.leavetime.entity.LeaveTime;
import org.benben.modules.business.leavetime.service.ILeaveTimeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.leavetime.vo.LeaveTimeVo;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userleave.entity.UserLeave;
import org.benben.modules.business.userleave.service.IUserLeaveService;
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
 * @Description: 用户请假时间表
 * @author： jeecg-boot
 * @date：   2019-07-11
 * @version： V1.0
 */
@RestController
@RequestMapping("/leavetime/leaveTime")
@Slf4j
public class LeaveTimeController {

	@Autowired
	private ILeaveTimeService leaveTimeService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserLeaveService userLeaveService;


	/**
	  * 分页列表查询
	 * @param leaveTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<LeaveTime>> queryPageList(LeaveTime leaveTime,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<LeaveTime>> result = new Result<IPage<LeaveTime>>();
		Result<IPage<LeaveTimeVo>> results = new Result<IPage<LeaveTimeVo>>();
		QueryWrapper<LeaveTime> queryWrapper = QueryGenerator.initQueryWrapper(leaveTime, req.getParameterMap());
		queryWrapper.isNull("parent_id");
		Page<LeaveTime> page = new Page<LeaveTime>(pageNo, pageSize);
		IPage<LeaveTime> pageList = leaveTimeService.page(page, queryWrapper);
		List<LeaveTime> records = pageList.getRecords();
		for (LeaveTime record : records) {
			User user = userService.getById(record.getUid());
			record.setNickName(user.getNickname());
			//得到对应的日期天数
			List<LeaveTime> leaveTime1 = leaveTimeService.getByParentId(record.getId());
			Integer[] dayArray= new Integer[leaveTime1.size()];
			String days = "";
			if (leaveTime1.size() < 2) {
				record.setDays(leaveTime1.get(0).getDay()+"");
			} else {
				int i =  0;
				for (LeaveTime time : leaveTime1) {

					if (i == leaveTime1.size()-1) {
						days = time.getDay()+"";
					} else{
						days = time.getDay() + ",";
					}
					i++;
				}
				record.setDays(days);
			}

/*
			//通过请假的时间的parentId 得到用户请假表的idl
			LeaveTime leaveTime2 = leaveTimeService.getById(record.getParentId());
*/

			//把对应的请假内容显示给后台
			UserLeave userLeave = userLeaveService.getById(record.getUserLeaveId());
			record.setComment(userLeave.getComment());
			record.setStatus(userLeave.getStatus());
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param leaveTime
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<LeaveTime> add(@RequestBody LeaveTime leaveTime) {
		Result<LeaveTime> result = new Result<LeaveTime>();
		try {
			leaveTimeService.save(leaveTime);
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
	 * @param leaveTime
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<LeaveTime> edit(@RequestBody LeaveTime leaveTime) {
		Result<LeaveTime> result = new Result<LeaveTime>();
		LeaveTime leaveTimeEntity = leaveTimeService.getById(leaveTime.getId());

		//修改请假表的状态、、
		UserLeave userLeave = new UserLeave();
		userLeave.setId(leaveTime.getUserLeaveId());
		userLeave.setStatus(leaveTime.getStatus());
		userLeaveService.updateById(userLeave);

		if(leaveTimeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = leaveTimeService.updateById(leaveTime);
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
	public Result<LeaveTime> delete(@RequestParam(name="id",required=true) String id) {
		Result<LeaveTime> result = new Result<LeaveTime>();
		LeaveTime leaveTime = leaveTimeService.getById(id);
		if(leaveTime==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = leaveTimeService.removeById(id);
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
	public Result<LeaveTime> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<LeaveTime> result = new Result<LeaveTime>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.leaveTimeService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<LeaveTime> queryById(@RequestParam(name="id",required=true) String id) {
		Result<LeaveTime> result = new Result<LeaveTime>();
		LeaveTime leaveTime = leaveTimeService.getById(id);
		if(leaveTime==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(leaveTime);
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
      QueryWrapper<LeaveTime> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              LeaveTime leaveTime = JSON.parseObject(deString, LeaveTime.class);
              queryWrapper = QueryGenerator.initQueryWrapper(leaveTime, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<LeaveTime> pageList = leaveTimeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户请假时间表列表");
      mv.addObject(NormalExcelConstants.CLASS, LeaveTime.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户请假时间表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<LeaveTime> listLeaveTimes = ExcelImportUtil.importExcel(file.getInputStream(), LeaveTime.class, params);
              for (LeaveTime leaveTimeExcel : listLeaveTimes) {
                  leaveTimeService.save(leaveTimeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listLeaveTimes.size());
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
