package org.benben.modules.business.verrifycode.service;

import org.benben.modules.business.verrifycode.entity.Verifycode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 验证码
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IVerifycodeService extends IService<Verifycode> {

	Verifycode queryByVerify(int verify);
}
