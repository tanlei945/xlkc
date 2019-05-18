package org.benben.modules.business.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.benben.modules.business.course.entity.CourseDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.course.vo.CourseQueryVo;

/**
 * @Description: 课程模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
public interface ICourseDTOService extends IService<CourseDTO> {

    /**
     * 搜索课程列表
     * @param pageNo
     * @param pageSize
     * @param courseQueryVo
     * @return
     */
    IPage<CourseDTO> searchNotice(Integer pageNo, Integer pageSize, CourseQueryVo courseQueryVo);
}
