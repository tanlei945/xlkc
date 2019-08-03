package org.benben.modules.business.userleave.service.impl;

import org.benben.modules.business.coursetime.entity.CourseTime;
import org.benben.modules.business.coursetime.vo.Day;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.benben.modules.business.userbuke.vo.UserBukeVo;
import org.benben.modules.business.usercourse.vo.UserCourseVo;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;
import org.benben.modules.business.userleave.entity.UserLeave;
import org.benben.modules.business.userleave.mapper.UserLeaveMapper;
import org.benben.modules.business.userleave.service.IUserLeaveService;
import org.benben.modules.business.userleave.vo.UserCourseStateVo;
import org.benben.modules.business.userleave.vo.UserLeaveDetailsVo;
import org.benben.modules.business.userleave.vo.UserLeaveVo;
import org.benben.modules.business.userleave.vo.UserLeavesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户请假
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@Service
public class UserLeaveServiceImpl extends ServiceImpl<UserLeaveMapper, UserLeave> implements IUserLeaveService {

	@Autowired
	private UserLeaveMapper userLeaveMapper;

	@Override
	public UserLeavesVo queryByName(String id, Integer pageNumber, Integer pageSize, String name) {
		//得到我的课程的主键id和名称
		List<UserCourseStateVo> list = userLeaveMapper.queryByName(id, pageNumber, pageSize, name);


		//查询关于我的所有请假
		List<UserLeave> userLeaveList = userLeaveMapper.queryByUid(id);
		for (UserCourseStateVo userCourseStateVo : list) {
			if(userLeaveList.size() > 0) {
				for (UserLeave userLeave : userLeaveList) {
					//				System.out.println(userCourseStateVo.getId());
					//				System.out.println(userLeave.getCourseId());
					if (userCourseStateVo.getId().equals(userLeave.getCourseId())) {
						if (userLeave.getStatus() == 2 || userLeave.getStatus() == 1) {
							userCourseStateVo.setListId(userLeave.getId());
							userCourseStateVo.setStatus(userLeave.getStatus());
							break;
						}
					} else {
						userCourseStateVo.setStatus(0);
					}
				}
			} else{
				userCourseStateVo.setStatus(0);
			}
		}

	/*	for (UserCourseStateVo userCourseStateVo : list) {
			//通过我的课程id查看这个课程的请假状态
			List<Integer> states = userLeaveMapper.queryState(id,userCourseStateVo.getId());
			if (states.size() == 0) {
				userCourseStateVo.setStatus(0);
			}
			for (Integer state : states) {
				if (state == 0){
					userCourseStateVo.setStatus(0);
				} else if (state == 1) {
					userCourseStateVo.setStatus(1);
					break;
				} else {
					if (state == 0) {
						System.out.println("不变");
					} else {
						userCourseStateVo.setStatus(2);
					}
				}
			}
		}*/
		for (UserCourseStateVo userCourseStateVo : list) {
			System.out.println(userCourseStateVo.toString());
		}

		//		List<UserLeaveVo> userLeaveVos = userLeaveMapper.listPage(id, pageNumber, pageSize);
		UserLeavesVo userLeavesVo = new UserLeavesVo();
		//查询总条数
		userLeavesVo.setTotal(userLeaveMapper.querByNameAll(id,name));
		userLeavesVo.setUserCourseStateVos(list);
		return userLeavesVo;
	}
	@Override
	public UserLeavesVo listPage(String id, Integer pageNumber, Integer pageSize) {

		//得到我的课程的主键id和名称
		List<UserCourseStateVo> list = userLeaveMapper.queryCourse(id, pageNumber, pageSize);

		//查询关于我的所有请假
		List<UserLeave> userLeaveList = userLeaveMapper.queryByUid(id);
		for (UserCourseStateVo userCourseStateVo : list) {
			if(userLeaveList.size() > 0) {
				for (UserLeave userLeave : userLeaveList) {
					//				System.out.println(userCourseStateVo.getId());
					//				System.out.println(userLeave.getCourseId());
					if (userCourseStateVo.getId().equals(userLeave.getCourseId())) {
						if (userLeave.getStatus() == 2 || userLeave.getStatus() == 1) {
							userCourseStateVo.setListId(userLeave.getId());
							userCourseStateVo.setStatus(userLeave.getStatus());
							break;
						}
					} else {
						userCourseStateVo.setStatus(0);
					}
				}
			} else {
				userCourseStateVo.setStatus(0);
			}

		}
//		List<UserLeaveVo> userLeaveVos = userLeaveMapper.listPage(id, pageNumber, pageSize);
		UserLeavesVo userLeavesVo = new UserLeavesVo();
		//查询总条数
		userLeavesVo.setTotal(userLeaveMapper.querAll(id));
		userLeavesVo.setUserCourseStateVos(list);
		return userLeavesVo;
	}

	@Override
	public UserLeaveDetailsVo getUserLeaveDetails(String id,String leaveId) {
		//得到请假的显示的时间
		List<DayTime> courseTime = userLeaveMapper.getTime(id,leaveId);
		for (DayTime time : courseTime) {
			List<Day> day = userLeaveMapper.getDay(time.getId());
			time.setDays(day);
		}
		UserLeaveDetailsVo userLeaveDetailsVo = new UserLeaveDetailsVo();
		userLeaveDetailsVo.setCourseTimes(courseTime);
		return userLeaveDetailsVo;
	}

	@Override
	public UserLeave queryByCid(String ucid) {

		return userLeaveMapper.queryByCid(ucid);
	}

	@Override
	public UserLeavesVo listBukkePage(String id, Integer pageNumber, Integer pageSize) {
		//得到我的课程的主键id和名称
		List<UserCourseStateVo> list = userLeaveMapper.queryCourse(id, pageNumber, pageSize);
		for (UserCourseStateVo userCourseStateVo : list) {
			//通过我的课程id查看这个课程的请假状态
			List<Integer> states = userLeaveMapper.queryState(id,userCourseStateVo.getId());
			if (states.size() == 0) {
				userCourseStateVo.setStatus(0);
			}
			for (Integer state : states) {
				if (state == 0){
					userCourseStateVo.setStatus(0);
				} else if (state == 2) {
					userCourseStateVo.setStatus(2);
					break;
				} else {
					if (state == 0) {
						System.out.println("不变");
					} else {
						userCourseStateVo.setStatus(1);
					}
				}
			}
		}
		List<UserBukeVo> userBukeVos = new ArrayList<UserBukeVo>();
		UserBukeVo userBukeVo = new UserBukeVo();
		for (UserCourseStateVo userCourseStateVo : list) {
			if (userCourseStateVo.getStatus() == 2) {
				userBukeVo.setId(userCourseStateVo.getId());
				userBukeVo.setCourseName(userCourseStateVo.getCourseName());
				userBukeVo.setEndtime(userCourseStateVo.getEndtime());
				userBukeVo.setStarttime(userBukeVo.getStarttime());
				userBukeVo.setStatus(0);
			}
		}

		//		List<UserLeaveVo> userLeaveVos = userLeaveMapper.listPage(id, pageNumber, pageSize);
		UserLeavesVo userLeavesVo = new UserLeavesVo();
		//查询总条数
		userLeavesVo.setTotal(userLeaveMapper.querAll(id));
		userLeavesVo.setUserCourseStateVos(list);
		return userLeavesVo;
	}


}
