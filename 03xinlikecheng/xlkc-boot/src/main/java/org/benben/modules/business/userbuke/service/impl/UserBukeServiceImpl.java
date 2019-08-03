package org.benben.modules.business.userbuke.service.impl;

import org.benben.modules.business.userbuke.entity.UserBuke;
import org.benben.modules.business.userbuke.mapper.UserBukeMapper;
import org.benben.modules.business.userbuke.service.IUserBukeService;
import org.benben.modules.business.userbuke.vo.UserBukeVo;
import org.benben.modules.business.userbuke.vo.UserBukesVo;
import org.benben.modules.business.userleave.mapper.UserLeaveMapper;
import org.benben.modules.business.userleave.service.IUserLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户补课的管理
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@Service
public class UserBukeServiceImpl extends ServiceImpl<UserBukeMapper, UserBuke> implements IUserBukeService {

	@Autowired
	private  UserBukeMapper userBukeMapper;
	@Autowired
	private IUserLeaveService userLeaveService;

	@Override
	public UserBukesVo listBukkePage(String id, Integer pageNumber, Integer pageSize) {
		String name = "";
		List<UserBukeVo> userBukeVos = userBukeMapper.queryByName(id,pageNumber,pageSize,name);

		//添加补课状态和补课id
		List<UserBuke> userBukeList = userBukeMapper.queryByUid(id);
		for (UserBukeVo userBukeVo : userBukeVos) {
			if (userBukeList.size() != 0) {
				for (UserBuke userBuke : userBukeList) {
					if (userBukeVo.getId().equals(userBuke.getCourseId())) {
						if (userBuke.getStatus() == 2 || userBuke.getStatus() == 1) {
							userBukeVo.setListId(userBuke.getId());
							userBukeVo.setStatus(userBuke.getStatus());
							break;
						}
					} else {
						userBukeVo.setStatus(0);
					}
				}
			} else {
				userBukeVo.setStatus(0);
			}
		}

		Integer all = userBukeMapper.queryByNameAll(id,name);
		if (all == null) {
			all = 0;
		}
		UserBukesVo userBukesVo = new UserBukesVo();
		userBukesVo.setTotal(all);
		userBukesVo.setUserBukeVos(userBukeVos);
		return userBukesVo;
	}

	@Override
	public UserBukesVo queryByName(String id, Integer pageNumber, Integer pageSize, String name) {
		List<UserBukeVo> userBukeVos = userBukeMapper.queryByName(id,pageNumber,pageSize,name);
		//添加补课状态和补课id
		List<UserBuke> userBukeList = userBukeMapper.queryByUid(id);
		for (UserBukeVo userBukeVo : userBukeVos) {
			if (userBukeList.size() != 0) {
				for (UserBuke userBuke : userBukeList) {
					if (userBukeVo.getId().equals(userBuke.getCourseId())) {
						if (userBuke.getStatus() == 2 || userBuke.getStatus() ==1) {
							userBukeVo.setListId(userBuke.getId());
							userBukeVo.setStatus(userBuke.getStatus());
							break;
						}
					} else {
						userBukeVo.setStatus(0);
					}
				}
			} else {
				userBukeVo.setStatus(0);
			}
		}
		Integer all = userBukeMapper.queryByNameAll(id,name);
		if (all == null) {
			all = 0;
		}
		UserBukesVo userBukesVo = new UserBukesVo();
		userBukesVo.setTotal(all);
		userBukesVo.setUserBukeVos(userBukeVos);
		return userBukesVo;
	}
}
