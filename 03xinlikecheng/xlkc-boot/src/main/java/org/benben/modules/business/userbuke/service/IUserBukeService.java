package org.benben.modules.business.userbuke.service;

import org.benben.modules.business.userbuke.entity.UserBuke;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userbuke.vo.UserBukesVo;

/**
 * @Description: 用户补课的管理
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
public interface IUserBukeService extends IService<UserBuke> {

	UserBukesVo listBukkePage(String id, Integer pageNumber, Integer pageSize);

	UserBukesVo queryByName(String id, Integer pageNumber, Integer pageSize, String name);
}
