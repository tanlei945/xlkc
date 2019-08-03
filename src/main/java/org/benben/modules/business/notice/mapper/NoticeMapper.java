package org.benben.modules.business.notice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.notice.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 关于通知的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface NoticeMapper extends BaseMapper<Notice> {

	@Select("select * from bb_notice ORDER BY create_time desc")
	List<Notice> queryNotice();
}
