package org.benben.modules.business.homework.service;

import org.benben.modules.business.homework.entity.Homework;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 我的功课管理
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
public interface IHomeworkService extends IService<Homework> {

	Homework queryByUidAndCourseId(String uid, String courseId);
}
