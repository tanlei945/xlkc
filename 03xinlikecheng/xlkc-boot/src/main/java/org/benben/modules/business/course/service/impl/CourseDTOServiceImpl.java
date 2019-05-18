package org.benben.modules.business.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.course.entity.CourseDTO;
import org.benben.modules.business.course.mapper.CourseDTOMapper;
import org.benben.modules.business.course.service.ICourseDTOService;
import org.benben.modules.business.course.vo.CourseQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 课程模块
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@Service
public class CourseDTOServiceImpl extends ServiceImpl<CourseDTOMapper, CourseDTO> implements ICourseDTOService {

    @Autowired
    private CourseDTOMapper courseDTOMapper;

    @Override
    public IPage<CourseDTO> searchNotice(Integer pageNo, Integer pageSize, CourseQueryVo courseQueryVo) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("page", (pageNo-1)*pageSize);
        paramMap.put("perpage", pageSize);
        paramMap.put("courseName", courseQueryVo.getCourseName());
        paramMap.put("courseType", courseQueryVo.getCourseType());
        List<CourseDTO> courseDTOS = courseDTOMapper.searchNoticeByPage(paramMap);
        int count = courseDTOMapper.countNotice(paramMap);
        IPage<CourseDTO> courseDTOIPage = new IPage<CourseDTO>() {
            @Override
            public List<CourseDTO> getRecords() {
                return courseDTOS;
            }

            @Override
            public IPage<CourseDTO> setRecords(List<CourseDTO> records) {
                return null;
            }

            @Override
            public long getTotal() {
                return count;
            }

            @Override
            public IPage<CourseDTO> setTotal(Long total) {
                return null;
            }

            @Override
            public long getSize() {
                return pageSize;
            }

            @Override
            public IPage<CourseDTO> setSize(long size) {
                return null;
            }

            @Override
            public long getCurrent() {
                return pageNo;
            }

            @Override
            public IPage<CourseDTO> setCurrent(long current) {
                return null;
            }
        };
        return courseDTOIPage;
    }
}
