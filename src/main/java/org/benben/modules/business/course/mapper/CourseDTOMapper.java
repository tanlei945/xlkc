package org.benben.modules.business.course.mapper;

import java.util.List;
import java.util.Map;

import org.benben.modules.business.course.entity.CourseDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 课程模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
public interface CourseDTOMapper extends BaseMapper<CourseDTO> {

    /**
     * 搜索课程列表
     *
     * @param paramMap@return
     */
    List<CourseDTO> searchNoticeByPage(Map paramMap);

    /**
     * 搜索课程列表总数
     *
     * @param paramMap@return
     */
    int countNotice(Map paramMap);
}
