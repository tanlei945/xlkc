package org.benben.modules.business.bookpreorder.controller;

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
import org.benben.modules.business.bookpreorder.entity.BookPreorder;
import org.benben.modules.business.bookpreorder.service.IBookPreorderService;

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
 * @Description: 书籍预购管理
 * @author： jeecg-boot
 * @date：   2019-07-12
 * @version： V1.0
 */
@RestController
@RequestMapping("/bookpreorder/bookPreorder")
@Slf4j
public class BookPreorderController {
	@Autowired
	private IBookPreorderService bookPreorderService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IBooksService booksService;

	/**
	  * 分页列表查询
	 * @param bookPreorder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<BookPreorder>> queryPageList(BookPreorder bookPreorder,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<BookPreorder>> result = new Result<IPage<BookPreorder>>();
		QueryWrapper<BookPreorder> queryWrapper = QueryGenerator.initQueryWrapper(bookPreorder, req.getParameterMap());
		Page<BookPreorder> page = new Page<BookPreorder>(pageNo, pageSize);
		IPage<BookPreorder> pageList = bookPreorderService.page(page, queryWrapper);
		List<BookPreorder> records = pageList.getRecords();
		for (BookPreorder record : records) {
			Books book = booksService.getById(record.getBookId());
			User user = userService.getById(record.getUid());
			record.setNickName(user.getNickname());
			record.setBookName(book.getName());
		}

		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param bookPreorder
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<BookPreorder> add(@RequestBody BookPreorder bookPreorder) {
		Result<BookPreorder> result = new Result<BookPreorder>();
		try {
			bookPreorderService.save(bookPreorder);
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
	 * @param bookPreorder
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<BookPreorder> edit(@RequestBody BookPreorder bookPreorder) {
		Result<BookPreorder> result = new Result<BookPreorder>();
		BookPreorder bookPreorderEntity = bookPreorderService.getById(bookPreorder.getId());
		if(bookPreorderEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bookPreorderService.updateById(bookPreorder);
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
	public Result<BookPreorder> delete(@RequestParam(name="id",required=true) String id) {
		Result<BookPreorder> result = new Result<BookPreorder>();
		BookPreorder bookPreorder = bookPreorderService.getById(id);
		if(bookPreorder==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = bookPreorderService.removeById(id);
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
	public Result<BookPreorder> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<BookPreorder> result = new Result<BookPreorder>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.bookPreorderService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<BookPreorder> queryById(@RequestParam(name="id",required=true) String id) {
		Result<BookPreorder> result = new Result<BookPreorder>();
		BookPreorder bookPreorder = bookPreorderService.getById(id);
		if(bookPreorder==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(bookPreorder);
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
      QueryWrapper<BookPreorder> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              BookPreorder bookPreorder = JSON.parseObject(deString, BookPreorder.class);
              queryWrapper = QueryGenerator.initQueryWrapper(bookPreorder, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<BookPreorder> pageList = bookPreorderService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "书籍预购管理列表");
      mv.addObject(NormalExcelConstants.CLASS, BookPreorder.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("书籍预购管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<BookPreorder> listBookPreorders = ExcelImportUtil.importExcel(file.getInputStream(), BookPreorder.class, params);
              for (BookPreorder bookPreorderExcel : listBookPreorders) {
                  bookPreorderService.save(bookPreorderExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listBookPreorders.size());
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
