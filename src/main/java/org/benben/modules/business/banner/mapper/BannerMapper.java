package org.benben.modules.business.banner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.banner.entity.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 轮播图管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
public interface BannerMapper extends BaseMapper<Banner> {

	@Select("select * from bb_banner where type = #{type}")
	Banner queryByType(Integer type);
}
