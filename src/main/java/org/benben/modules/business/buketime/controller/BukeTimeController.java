package org.benben.modules.business.buketime.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.buketime.entity.BukeTime;
import org.benben.modules.business.buketime.service.IBukeTimeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.leavetime.entity.LeaveTime;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userbuke.entity.UserBuke;
import org.benben.modules.business.userbuke.service.IUserBukeService;
import org.benben.modules.business.userleave.entity.UserLeave;
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
 * @Description: 补课时间管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@RestController
@RequestMapping("/buketime/bukeTime")
@Slf4j
public class BukeTimeController {
	@Autowired
	private IBukeTimeService bukeTimeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserBukeService userBukeService;

	/**
	  * 分页列表查询
	 * @param bukeTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<BukeTime>> queryPageList(BukeTime bukeTime,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<BukeTime>> result = new Result<IPage<BukeTime>>();
		QueryWrapper<BukeTime> queryWrapper = QueryGenerator.initQueryWrapper(bukeTime, req.getParameterMap());
		queryWrapper.isNull("parent_id");
		Page<BukeTime> page = new Page<BukeTime>(pageNo, pageSize);
		IPage<BukeTime> pageList = bukeTimeService.page(page, queryWrapper);
		List<BukeTime> records = pageList.getRecords();
		for (BukeTime record : records) {
			//得到用户的昵称
			User user = userService.getById(record.getUid());
			record.setNickName(user.getNickname());
			//得到对应的日期天数
			List<BukeTime> bukeTime1 = bukeTimeService.getByParentId(record.getId());
			String days = "";
			if (bukeTime1.size() < 2) {
				record.setDays(bukeTime1.get(0).getDay()+"");
			} else {
				int i = 0;
				for (BukeTime time : bukeTime1) {
					if (i == bukeTime1.size() -1 ) {
						days = time.getDay() + "";
					}else {
						days = time.getDay() + ",";

					}
					i++;
				}
				record.setDays(days);
			}

			//把对应的请假内容显示给后台
			UserBuke userBuke =userBukeService.getById(record.getUserBukeId());
			record.setComment(userBuke.getComment());
			record.setStatus(userBuke.getStatus());

		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param bukeTime
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<BukeTime> add(@RequestBody BukeTime bukeTime) {
		Result<BukeTime> result = new Result<BukeTime>();
		try {
			bukeTimeService.save(bukeTime);
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
	 * @param bukeTime
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<BukeTime> edit(@RequestBody BukeTime bukeTime) {
		Result<BukeTime> result = new Result<BukeTime>();
		BukeTime bukeTimeEntity = bukeTimeService.getById(bukeTime.getId());

		//修改请假表的状态、、
		UserBuke userBuke = new UserBuke();
		userBuke.setId(bukeTime.getUserBukeId());
		userBuke.setStatus(bukeTime.getStatus());
		userBukeService.updateById(userBuke);

		if(bukeTimeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bukeTimeService.updateById(bukeTime);
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
	public Result<BukeTime> delete(@RequestParam(name="id",required=true) String id) {
		Result<BukeTime> result = new Result<BukeTime>();
		BukeTime bukeTime = bukeTimeService.getById(id);
		if(bukeTime==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bukeTimeService.removeById(id);
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
	public Result<BukeTime> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<BukeTime> result = new Result<BukeTime>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bukeTimeService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<BukeTime> queryById(@RequestParam(name="id",required=true) String id) {
		Result<BukeTime> result = new Result<BukeTime>();
		BukeTime bukeTime = bukeTimeService.getById(id);
		if(bukeTime==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bukeTime);
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
      QueryWrapper<BukeTime> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              BukeTime bukeTime = JSON.parseObject(deString, BukeTime.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bukeTime, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<BukeTime> pageList = bukeTimeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "补课时间管理列表");
      mv.addObject(NormalExcelConstants.CLASS, BukeTime.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("补课时间管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<BukeTime> listBukeTimes = ExcelImportUtil.importExcel(file.getInputStream(), BukeTime.class, params);
              for (BukeTime bukeTimeExcel : listBukeTimes) {
                  bukeTimeService.save(bukeTimeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listBukeTimes.size());
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
