package org.benben.modules.business.bookrefund.controller;

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
import org.benben.modules.business.bookrefund.entity.BookRefund;
import org.benben.modules.business.bookrefund.service.IBookRefundService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
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
 * @Description: 书籍退款表
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@RestController
@RequestMapping("/bookrefund/bookRefund")
@Slf4j
public class BookRefundController {
	@Autowired
	private IBookRefundService bookRefundService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IBooksService bookService;

	/**
	  * 分页列表查询
	 * @param bookRefund
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<BookRefund>> queryPageList(BookRefund bookRefund,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<BookRefund>> result = new Result<IPage<BookRefund>>();
		QueryWrapper<BookRefund> queryWrapper = QueryGenerator.initQueryWrapper(bookRefund, req.getParameterMap());
		Page<BookRefund> page = new Page<BookRefund>(pageNo, pageSize);
		IPage<BookRefund> pageList = bookRefundService.page(page, queryWrapper);
		List<BookRefund> records = pageList.getRecords();
		for (BookRefund record : records) {

			User user = userService.getById(record.getUid());
			record.setNickName(user.getNickname());
			Books books = bookService.getById(record.getBookId());
			record.setBookName(books.getName());
		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param bookRefund
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<BookRefund> add(@RequestBody BookRefund bookRefund) {
		Result<BookRefund> result = new Result<BookRefund>();
		try {
			bookRefundService.save(bookRefund);
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
	 * @param bookRefund
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<BookRefund> edit(@RequestBody BookRefund bookRefund) {
		Result<BookRefund> result = new Result<BookRefund>();
		BookRefund bookRefundEntity = bookRefundService.getById(bookRefund.getId());
		if(bookRefundEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bookRefundService.updateById(bookRefund);
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
	public Result<BookRefund> delete(@RequestParam(name="id",required=true) String id) {
		Result<BookRefund> result = new Result<BookRefund>();
		BookRefund bookRefund = bookRefundService.getById(id);
		if(bookRefund==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bookRefundService.removeById(id);
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
	public Result<BookRefund> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<BookRefund> result = new Result<BookRefund>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bookRefundService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<BookRefund> queryById(@RequestParam(name="id",required=true) String id) {
		Result<BookRefund> result = new Result<BookRefund>();
		BookRefund bookRefund = bookRefundService.getById(id);
		if(bookRefund==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bookRefund);
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
      QueryWrapper<BookRefund> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              BookRefund bookRefund = JSON.parseObject(deString, BookRefund.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bookRefund, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<BookRefund> pageList = bookRefundService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "书籍退款表列表");
      mv.addObject(NormalExcelConstants.CLASS, BookRefund.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("书籍退款表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<BookRefund> listBookRefunds = ExcelImportUtil.importExcel(file.getInputStream(), BookRefund.class, params);
              for (BookRefund bookRefundExcel : listBookRefunds) {
                  bookRefundService.save(bookRefundExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listBookRefunds.size());
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
