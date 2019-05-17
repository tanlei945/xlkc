package org.benben.common.util.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author: WangHao
 * @date: 2019/4/24 11:11
 * @description: 阿里云短信
 */
public class AliMessageSend {

    // 产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    // 产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    private static String accessKeyId = "LTAIbNPovuHJy8ak";
    private static String accessKeySecret = "ULaRPWWZjUy92oJ1AEjgHe90taoPxG";
    private static String signName = "阿里云短信测试专用";


    // 发送短信方法
    public static SendSmsResponse sendSms(String mobile,String jsonParam,String templeteCode) {

        SendSmsResponse sendSmsResponse = null;

        try {

            // 可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            // 初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-beijing", "cn-beijing", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            // 组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();

            // 必填:待发送手机号
            request.setPhoneNumbers(mobile);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            // 必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templeteCode);

            // 可选:模板中的变量替换JSON串,如模板内容为"尊敬的用户,您的验证码为${code}"时,此处的值为
            request.setTemplateParam(jsonParam);

            // hint 此处可能会抛出异常，注意catch
            sendSmsResponse = acsClient.getAcsResponse(request);

        } catch (ClientException e) {
            e.printStackTrace();
        }

        return sendSmsResponse;
    }

}
