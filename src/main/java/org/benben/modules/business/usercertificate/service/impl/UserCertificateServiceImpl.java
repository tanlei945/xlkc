package org.benben.modules.business.usercertificate.service.impl;

import org.benben.modules.business.usercertificate.entity.UserCertificate;
import org.benben.modules.business.usercertificate.mapper.UserCertificateMapper;
import org.benben.modules.business.usercertificate.service.IUserCertificateService;
import org.benben.modules.business.usercertificate.vo.CourseNameVo;
import org.benben.modules.business.usercertificate.vo.UserCertificateVo;
import org.benben.modules.business.usercertificate.vo.UserCertificatesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 我的证书管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Service
public class UserCertificateServiceImpl extends ServiceImpl<UserCertificateMapper, UserCertificate> implements IUserCertificateService {

	@Autowired
	private UserCertificateMapper certificateMapper;

	@Override
	public UserCertificatesVo listCertificate(String uid, Integer pageNumber, Integer pageSize) {
		List<UserCertificateVo> userCertificateVo = certificateMapper.queryCertificate(uid,pageNumber,pageSize);
		Integer total = certificateMapper.queryCertificateAll(uid);
		if (total == null) {
			total = 0 ;
		}
		UserCertificatesVo userCertificatesVo = new UserCertificatesVo();
		userCertificatesVo.setTotal(total);
		userCertificatesVo.setUserCertificateVos(userCertificateVo);
		return userCertificatesVo;
	}

	@Override
	public List<CourseNameVo> getCourseName() {
		return certificateMapper.getCourseName();
	}
}
