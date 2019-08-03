package org.benben.modules.business.bbuservideo.service;

import org.benben.modules.business.bbuservideo.entity.BbVideoType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 视频类型关联表
 * @author： jeecg-boot
 * @date：   2019-05-29
 * @version： V1.0
 */
public interface IBbVideoTypeService extends IService<BbVideoType> {

	BbVideoType queryByName(String videoType);
}
