package org.benben.modules.business.usercertificate.service;

import org.benben.modules.business.usercertificate.entity.UserCertificate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.usercertificate.vo.CourseNameVo;
import org.benben.modules.business.usercertificate.vo.UserCertificatesVo;

import java.util.List;

/**
 * @Description: 我的证书管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface IUserCertificateService extends IService<UserCertificate> {

	UserCertificatesVo listCertificate(String id, Integer pageNumber, Integer pageSize);

	List<CourseNameVo> getCourseName();

}
