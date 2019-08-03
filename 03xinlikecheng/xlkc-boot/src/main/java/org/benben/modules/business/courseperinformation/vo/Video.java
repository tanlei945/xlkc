package org.benben.modules.business.courseperinformation.vo;

import lombok.Data;

import java.util.List;

@Data
public class Video {
	private List<String> videoUrl;
	private List<String> videoName;
	private String videoImg;
}
