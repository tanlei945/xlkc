package org.benben.modules.business.notice.service;

import org.benben.modules.business.notice.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 关于通知的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface INoticeService extends IService<Notice> {

	List<Notice> queryNotice();

}
