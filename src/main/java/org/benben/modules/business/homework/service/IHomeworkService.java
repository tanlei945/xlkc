package org.benben.modules.business.homework.service;

import org.benben.modules.business.homework.entity.Homework;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IHomeworkService extends IService<Homework> {

	Homework queryByUidAndCourseId(String uid, String courseId);
}
