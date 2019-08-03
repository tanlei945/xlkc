package org.benben.modules.business.courseperinformation.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CoursePerInformationVo {

	private Video videoUrl;
	private BigDecimal totalprice;
	private List<String> imgUrl;
}
