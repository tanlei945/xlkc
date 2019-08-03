package org.benben.modules.business.homework.service;

import org.benben.modules.business.homework.entity.Homework;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.homework.vo.HomeWorkVo;
import org.benben.modules.business.homework.vo.MyHomeWork;

import java.util.List;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IHomeworkService extends IService<Homework> {

	List<MyHomeWork> queryByUidAndCourseId(String uid);

	List<String> queryGroupName(String courseId);
}
