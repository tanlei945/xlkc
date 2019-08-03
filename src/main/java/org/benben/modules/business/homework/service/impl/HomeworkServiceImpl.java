package org.benben.modules.business.homework.service.impl;

import org.benben.modules.business.homework.entity.Homework;
import org.benben.modules.business.homework.mapper.HomeworkMapper;
import org.benben.modules.business.homework.service.IHomeworkService;
import org.benben.modules.business.homework.vo.HomeWorkVo;
import org.benben.modules.business.homework.vo.MyHomeWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

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
	 * @param courseId
	 * @param uid
	 * @return
	 */
	@Override
	public List<MyHomeWork> queryByUidAndCourseId(String uid) {
		List<HomeWorkVo> homework = homeworkMapper.queryByUidAndCourseId(uid);
		List<MyHomeWork> myHomeWorks = new ArrayList<>();
		for (HomeWorkVo homeWorkVo : homework) {
			MyHomeWork myHomeWork = homeworkMapper.getCourse(homeWorkVo.getCourseId());
			if (myHomeWork == null) {
				continue;
			}
			myHomeWork.setHomeworkId(homeWorkVo.getId());
			myHomeWorks.add(myHomeWork);
		}
		return myHomeWorks;
	}

	@Override
	public List<String> queryGroupName(String courseId) {
		List<String> s = homeworkMapper.queryGroupName(courseId);
		return s;
	}
}
