package org.benben.modules.business.commen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.benben.modules.business.commen.service.ICommonService;
import org.benben.modules.business.commen.service.IQiniuService;
import org.benben.modules.business.commen.service.impl.IQiniuServiceImpl;
import org.benben.modules.business.video.entity.Video;
import org.benben.modules.business.video.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: WangHao
 * @date: 2019/4/12 14:00
 * @description:
 */
@RestController
@RequestMapping("/api/v1/common/")
@Api(tags = {"通用接口"})
public class RestCommonController {

    @Value(value = "${benben.path.upload}")
    private String uploadpath;
    @Autowired
    private ICommonService commonService;

    @Autowired
	private IQiniuService qiniuService;

    @Autowired
	private IVideoService videoService;

	@PostMapping(value = "/upload_image_ali")
	@ApiOperation(value = "学习园地上传视频",tags = {"学习园地接口"}, notes = "学习园地上传视频")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "视频的名称"),
			@ApiImplicitParam(name = "videoType", value = "视频的类型"),
			@ApiImplicitParam(name = "videoClass", value = "0/免费视频 1/邀请码视频"),
			@ApiImplicitParam(name = "invitecode", value = "邀请码")

	})
	public RestResponseBean upload(@RequestParam(value = "file") MultipartFile[] files,@RequestParam String name,@RequestParam String videoType,
			@RequestParam Integer videoClass, @RequestParam String invitecode) {
		String result = "";
		Video video;
		try {
			result = OSSClientUtils.fileVideoUpload(files,name,videoType);
			if (videoClass == 0) {
				video = new Video(name, result, videoType, videoClass, null);
			}else {
				video = new Video(name, result, videoType, videoClass, invitecode);
			}

			videoService.save(video);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), result);
	}



	@PostMapping("/down_file")
	@ApiOperation(value = "使用七牛云下载文件", tags = "通用接口", notes = "使用七牛云下载文件")
	public RestResponseBean downloadFile(@RequestParam String targetUrl) {
		if (targetUrl != null || targetUrl.equals("")) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		String download = qiniuService.download(targetUrl);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),download);
	}

   /* @PostMapping("/upload_file")
	@ApiOperation(value = "使用七牛云上传文件", tags = "通用接口", notes = "使用七牛云上传文件")
	public RestResponseBean uploadFile(@RequestParam(value = "file") MultipartFile file) {
		if(file.isEmpty()) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}

		try {
			String fileUrl=qiniuService.saveImage(file);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),fileUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
	}*/

    @PostMapping(value = "/upload_image_local")
    @ApiOperation(value = "上传图片到本地",tags = {"通用接口"}, notes = "上传图片到本地")
    public RestResponseBean uploadImage(@RequestParam(value = "file") MultipartFile file) {

        String dbpath =commonService.localUploadImage(file);

        if(StringUtils.isBlank(dbpath)){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),dbpath);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);

    }


    @PostMapping(value = "/file_upload")
    @ApiOperation(value = "上传视频",tags = {"通用接口"}, notes = "上传视频")
    public Result<Object> fileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        Result<Object> result = new Result<Object>();

        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            result.error500("上传失败!");
        }

        InputStream fileContent = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String url = OSSClientUtils.uploadFile(fileContent, fileName);
        String[] strarray = {fileName, url};

        return result.ok(strarray);
    }
}
