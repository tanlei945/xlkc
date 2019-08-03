package org.benben.modules.business.usercertificate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.usercertificate.entity.UserCertificate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usercertificate.vo.CourseNameVo;
import org.benben.modules.business.usercertificate.vo.UserCertificateVo;

/**
 * @Description: 我的证书管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface UserCertificateMapper extends BaseMapper<UserCertificate> {

	@Select("SELECT * from user_certificate where uid = #{uid} and state = 1 LIMIT #{pageNumber},#{pageSize}")
	List<UserCertificateVo> queryCertificate(String uid, Integer pageNumber, Integer pageSize);

	@Select("SELECT count(1) from user_certificate where uid = #{uid} and state = 1")
	Integer queryCertificateAll(String uid);

	@Select("select course_name from bb_course")
	List<CourseNameVo> getCourseName();

}
