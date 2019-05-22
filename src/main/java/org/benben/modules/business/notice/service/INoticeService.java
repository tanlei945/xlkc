package org.benben.modules.business.notice.service;

import org.benben.modules.business.notice.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 通知表管理
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface INoticeService extends IService<Notice> {

	Notice queryNotice();

}
