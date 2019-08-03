package org.benben.modules.business.courseassistant.service;

import org.benben.modules.business.courseassistant.entity.CourseAssistant;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.courseassistant.vo.HomeWorkAchieveVo;
import org.benben.modules.business.coursetime.vo.CourseTimeVo;

import java.util.List;

/**
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
public interface ICourseAssistantService extends IService<CourseAssistant> {

	CourseTimeVo queryId(String id, String id1);

	List<HomeWorkAchieveVo> homeworkAchieve(String id);

	HomeWorkAchieveVo homeworkById(String courseId);
}
