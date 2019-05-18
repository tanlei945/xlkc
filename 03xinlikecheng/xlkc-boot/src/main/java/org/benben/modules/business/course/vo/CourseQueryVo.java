package org.benben.modules.business.course.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程搜索vo
 *
 * @author jx
 * @create 2019-05-18 15:28
 */
@ApiModel("课程查询参数")
@Data
public class CourseQueryVo {
    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String courseName;
    /**
     * 课程类型
     */
    @ApiModelProperty("课程类型")
    private String courseType;

}
