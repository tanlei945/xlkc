package org.benben.modules.business.aboutus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.aboutus.entity.AboutUs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.aboutus.vo.AboutUsVo;

/**
 * @Description: 关于我们和联系我们管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
public interface AboutUsMapper extends BaseMapper<AboutUs> {

	@Select("select * from bb_about_us where type = 0 ORDER BY MAX(create_time)")
	AboutUsVo getAboutUs();

}
