package org.benben.modules.business.commen.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IQiniuService {
	//七牛云上传
	public String saveImage(MultipartFile file) throws IOException;
	//七牛云下载
	public String download(String targetUrl);
}
