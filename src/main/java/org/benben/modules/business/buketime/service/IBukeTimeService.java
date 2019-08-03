package org.benben.modules.business.buketime.service;

import org.benben.modules.business.buketime.entity.BukeTime;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.buketime.vo.UserBukeDetailsVo;

import java.util.List;

/**
 * @Description: 补课时间管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
public interface IBukeTimeService extends IService<BukeTime> {

	UserBukeDetailsVo getUserBukeDetails(String id, String bukeId);

	List<BukeTime> getByParentId(String parentId);
}
