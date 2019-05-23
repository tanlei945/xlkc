package org.benben.modules.business.homework.service.impl;

import org.benben.modules.business.homework.entity.Homework;
import org.benben.modules.business.homework.mapper.HomeworkMapper;
import org.benben.modules.business.homework.service.IHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {
	@Autowired
	private HomeworkMapper homeworkMapper;

	/**
	 * 根据课程id和用户id查询全部的功课
	 * @param uid
	 * @param courseId
	 * @return
	 */
	@Override
	public Homework queryByUidAndCourseId(String uid, String courseId) {
		Homework homework = homeworkMapper.queryByUidAndCourseId(uid, courseId);
		return homework;
	}
}
