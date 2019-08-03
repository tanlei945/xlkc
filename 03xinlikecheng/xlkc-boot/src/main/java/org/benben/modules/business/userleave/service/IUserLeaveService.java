package org.benben.modules.business.userleave.service;

import org.benben.modules.business.usercourse.vo.UserCoursesVo;
import org.benben.modules.business.userleave.entity.UserLeave;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userleave.vo.UserLeaveDetailsVo;
import org.benben.modules.business.userleave.vo.UserLeavesVo;

/**
 * @Description: 用户请假
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
public interface IUserLeaveService extends IService<UserLeave> {

	UserLeavesVo listPage(String id, Integer pageNumber, Integer pageSize);

	UserLeaveDetailsVo getUserLeaveDetails(String id,String leaveId);

	UserLeave queryByCid(String ucid);

	UserLeavesVo listBukkePage(String id, Integer pageNumber, Integer pageSize);

	UserLeavesVo queryByName(String id, Integer pageNumber, Integer pageSize, String name);
}
