package org.benben.modules.business.notice.service.impl;

import org.benben.modules.business.notice.entity.Notice;
import org.benben.modules.business.notice.mapper.NoticeMapper;
import org.benben.modules.business.notice.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 关于通知的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Autowired
	private NoticeMapper noticeMapper;

	@Override
	public List<Notice> queryNotice() {
		List<Notice> notices = noticeMapper.queryNotice();
		List<Notice> noticeList = new ArrayList<>();
		for (int i = 0 ; i < 2; i++) {
			noticeList.add(notices.get(i));
		}
		return noticeList;
	}
}
