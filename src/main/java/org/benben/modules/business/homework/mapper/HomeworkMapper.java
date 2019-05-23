package org.benben.modules.business.homework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.benben.modules.business.homework.entity.Homework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface HomeworkMapper extends BaseMapper<Homework> {


	Homework queryByUidAndCourseId(String uid, String courseId);
}
