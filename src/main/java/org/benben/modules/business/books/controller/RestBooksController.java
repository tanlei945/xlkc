package org.benben.modules.business.books.controller;

import java.awt.print.Book;
import java.util.ArrayList;
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
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.service.IBooksService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.books.vo.Booksvos;
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
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/books")
@Api(tags = {"书店接口"})
@Slf4j
public class RestBooksController {
	@Autowired
	private IBooksService booksService;

	@GetMapping("/recommendBooks")
	@ApiOperation(value = "支付完后推荐书籍",tags = {"书店接口"},notes = "支付完成候推荐书籍")
	public RestResponseBean recommendBooks() {
		List<Books> books = booksService.recommendBooks();
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),books);
	}

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 修改商品书籍的数量
	 * @description 显示修改商品书籍的数量
	 * @method Post
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/books/updateNum
	 * @return {"code": 1,"msg": "操作成功","time": "1561173188861","data": true}
	 * @param id String 商品id
	 * @param num int 书籍商品的数量
	 * @remark 书店接口
	 * @number 99
	 */
	@PostMapping("/updateNum")
	@ApiOperation(value = "修改商品购买书籍的数量", tags = "书店接口",notes = "修改商品购买书籍的数量")
	public RestResponseBean updateNum(@RequestParam String id, @RequestParam Integer num){
		boolean b = booksService.updateNum(id, num);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),b);
	}

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 分页显示全部书籍
	 * @description 显示分页显示全部书籍
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/books/queryBooks
	 * @return {"code": 1,"msg": "操作成功","time": "1561173360964","data": {"total": 2,"videoList": [{"id": "1","name": "心理学课程","bookintro": "教会你催眠","price": 100,"num": 6},{"id": "a54918d9a40f566fb540fccd1b368277","name": "NLPP书","bookintro": "针对NLPP课程的介绍","price": 50,"num": 0}]}
	 * @param pageNumber int 分页从第几个开始
	 * @param pageSize int 一页显示几个
	 * @remark 书店接口
	 * @number 99
	 */
	/*@GetMapping("/queryBooks")
	@ApiOperation(value = "分页显示全部书籍", tags ={"书店接口"}, notes = "分页显示全部书籍")
	public RestResponseBean queryBooks(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
		BooksVo books = booksService.queryBooks(pageNumber,pageSize);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),books);
	}*/

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 分页显示全部书籍
	 * @description 显示分页显示全部书籍
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/books/queryByName
	 * @return {"code": 1,"msg": "操作成功","time": "1561173360964","data": {"total": 2,"videoList": [{"id": "1","name": "心理学课程","bookintro": "教会你催眠","price": 100,"num": 6},{"id": "a54918d9a40f566fb540fccd1b368277","name": "NLPP书","bookintro": "针对NLPP课程的介绍","price": 50,"num": 0}]}
	 * @param name String 书籍商品的名称
	 * @param pageNumber int 分页从第几个开始
	 * @param pageSize int 一页显示几个
	 * @remark 书店接口
	 * @number 99
	 */
	@GetMapping("/queryByName")
	@ApiOperation(value = "分页模糊查询名称书籍", tags ={"书店接口"}, notes = "分页模糊查询名称书籍")
	public RestResponseBean queryByName(@RequestParam Integer pageNumber, @RequestParam Integer pageSize, String name) {
		BooksVo books = booksService.queryByName(pageNumber,pageSize,name);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),books);
	}



	/**
	 * 分页列表查询
	 * @param books
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Books>> queryPageList(Books books,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<Books>> result = new Result<IPage<Books>>();
		QueryWrapper<Books> queryWrapper = QueryGenerator.initQueryWrapper(books, req.getParameterMap());
		Page<Books> page = new Page<Books>(pageNo, pageSize);
		IPage<Books> pageList = booksService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *   添加
	 * @param books
	 * @return
	 */
	@PostMapping(value = "/add")
	//	@ApiOperation(value = "添加数据接口", tags = "书店接口", notes = "添加数据接口")
	public Result<Books> add(@RequestBody Books books) {
		Result<Books> result = new Result<Books>();
		try {
			booksService.save(books);
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
	 * @param books
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Books> edit(@RequestBody Books books) {
		Result<Books> result = new Result<Books>();
		Books booksEntity = booksService.getById(books.getId());
		if(booksEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = booksService.updateById(books);
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
	public Result<Books> delete(@RequestParam(name="id",required=true) String id) {
		Result<Books> result = new Result<Books>();
		Books books = booksService.getById(id);
		if(books==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = booksService.removeById(id);
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
	public Result<Books> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Books> result = new Result<Books>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.booksService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * showdoc
	 * @catalog 书店接口
	 * @title 查看书籍详情
	 * @method Get
	 * @url https://192.168.1.125:8081/xlkc-boot/api/v1/books/queryById
	 * @return {"code": 1,"msg": "操作成功","time": "1562228650621","data": {"id": "1","name": "心理学课程","bookintro": "教会你催眠","price": 100,"num": 6,"bookComment": "%3Cp%3E%E5%AE%89%E9%A1%BF%E5%A5%BD%E5%8D%A1%E6%97%B6%E9%97%B4%E6%AE%B5%E5%92%8Ckasdsa%3C%2Fp%3E","bookContent": "大富科技纳斯达克建立符合实际可怜","bookNum": 50,"bookUrl": "","bookImg": "{\"asda\",\"da\"}","createTime": "2019-05-23 20:13:49","createBy": "boot","updateTime": "2019-06-13 18:31:45","updateBy": "superadmin"}}
	 * @param id String 书籍商品id
	 * @remark 书店接口
	 * @number 99
	 */
	@GetMapping(value = "/queryById")
	@ApiOperation(value = "查看书籍详情", tags = {"书店接口"}, notes = "查看书籍详情")
	public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
		Result<Books> result = new Result<Books>();
		Books books = booksService.getById(id);
		//BeanUtils.copyProperties(源对象, 目标对象);
		//把轮播图地址转换为数组
		String bookUrl = books.getBookUrl();
		List<String> list = new ArrayList<>();
		 list = Arrays.asList(bookUrl.split(","));

		Booksvos booksvos = new Booksvos();
		BeanUtils.copyProperties(books,booksvos);
		booksvos.setBookImg(list);
		if(books==null) {
			result.error500("未找到对应实体");
		}else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),booksvos);
		}
		return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),booksvos);
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
		QueryWrapper<Books> queryWrapper = null;
		try {
			String paramsStr = request.getParameter("paramsStr");
			if (oConvertUtils.isNotEmpty(paramsStr)) {
				String deString = URLDecoder.decode(paramsStr, "UTF-8");
				Books books = JSON.parseObject(deString, Books.class);
				queryWrapper = QueryGenerator.initQueryWrapper(books, request.getParameterMap());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<Books> pageList = booksService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "书籍表列表");
		mv.addObject(NormalExcelConstants.CLASS, Books.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("书籍表列表数据", "导出人:Jeecg", "导出信息"));
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
				List<Books> listBookss = ExcelImportUtil.importExcel(file.getInputStream(), Books.class, params);
				for (Books booksExcel : listBookss) {
					booksService.save(booksExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listBookss.size());
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
