package org.benben.modules.business.commen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.benben.common.util.FileUtil;
import org.benben.modules.business.commen.service.IQiniuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * 七牛云
 */
@Service
public class IQiniuServiceImpl implements IQiniuService {
	private static final Logger logger = LoggerFactory.getLogger(IQiniuServiceImpl.class);

	// 设置好账号的ACCESS_KEY和SECRET_KEY
	String ACCESS_KEY = "###################";
	String SECRET_KEY = "###################";
	// 要上传的空间
	String bucketname = "####";

	// 密钥配置
	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
	Configuration cfg = new Configuration(Zone.zone0());
	// ...其他参数参考类注释
	UploadManager uploadManager = new UploadManager(cfg);

	// 测试域名，只有30天有效期
	private static String QINIU_IMAGE_DOMAIN = "http://############/";

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public String getUpToken() {
		return auth.uploadToken(bucketname);
	}

	@Override
	public String saveImage(MultipartFile file) throws IOException {
		try {
			int dotPos = file.getOriginalFilename().lastIndexOf(".");
			if (dotPos < 0) {
				return null;
			}
			String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
			// 判断是否是合法的文件后缀
			if (!FileUtil.isFileAllowed(fileExt)) {
				return null;
			}

			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
			// 调用put方法上传
			Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
			// 打印返回的信息
			if (res.isOK() && res.isJson()) {
				// 返回这张存储照片的地址
				return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
			} else {
				logger.error("七牛异常:" + res.bodyString());
				return null;
			}
		} catch (QiniuException e) {
			// 请求失败时打印的异常的信息
			logger.error("七牛异常:" + e.getMessage());
			return null;
		}
	}
	/**
	 * 下载
	 */
	@Override
	public String download(String targetUrl) {
		//获取downloadUrl，获取下载文件路径，即：donwloadUrl
		String downloadUrl = auth.privateDownloadUrl(targetUrl);
		//本地保存路径
		String filePath = "D:/temp/picture/";
		return download(downloadUrl, filePath);
	}
	/**
	 * 通过发送http get 请求获取文件资源
	 * @param url
	 * @param filepath
	 * @return
	 */
	private static String  download(String url, String filepath) {
		OkHttpClient client = new OkHttpClient();
		System.out.println(url);
		Request req = new Request.Builder().url(url).build();
		okhttp3.Response resp = null;
		try {
			resp = client.newCall(req).execute();
			System.out.println(resp.isSuccessful());
			if(resp.isSuccessful()) {
				ResponseBody body = resp.body();
				InputStream is = body.byteStream();
				byte[] data = readInputStream(is);
				File imgFile = new File(filepath + "123.png");          //下载到本地的图片命名
				FileOutputStream fops = new FileOutputStream(imgFile);
				fops.write(data);
				fops.close();
				return "下载成功";
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unexpected code " + resp);
		}
		return "下载失败";
	}
	/**
	 * 读取字节输入流内容
	 * @param is
	 * @return
	 */
	private static byte[] readInputStream(InputStream is) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		byte[] buff = new byte[1024 * 2];
		int len = 0;
		try {
			while((len = is.read(buff)) != -1) {
				writer.write(buff, 0, len);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toByteArray();
	}


	/**
	 * 主函数：测试
	 * @param args
	 */
	public static void main(String[] args) {
		//构造私有空间的需要生成的下载的链接；
		//格式： http://私有空间绑定的域名/空间下的文件名

		String targetUrl = "http://p7s6tmhce.bkt.clouddn.com/123.png";         //外链域名下的图片路径
//		new DownLoad().download(targetUrl);
	}
}
