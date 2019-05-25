package org.benben.modules.business.verrifycode.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.verrifycode.entity.Verifycode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

/**
 * @Description: 验证码
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface VerifycodeMapper extends BaseMapper<Verifycode> {

	@Select("select * from bb_verifycode where verify = #{verify}")
	Verifycode queryByVerify(int verify);
}
