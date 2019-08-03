package org.benben.modules.business.bbuservideo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.bbuservideo.entity.BbVideoType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 视频类型关联表
 * @author： jeecg-boot
 * @date：   2019-05-29
 * @version： V1.0
 */
public interface BbVideoTypeMapper extends BaseMapper<BbVideoType> {

	@Select("select * from bb_video_type where video_type = #{videoType}")
	BbVideoType queryByName(String videoType);
}
