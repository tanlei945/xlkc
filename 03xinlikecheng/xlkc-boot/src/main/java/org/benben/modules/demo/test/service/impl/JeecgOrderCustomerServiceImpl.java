package org.benben.modules.demo.test.service.impl;

import java.util.List;

import org.benben.modules.demo.test.entity.JeecgOrderCustomer;
import org.benben.modules.demo.test.mapper.JeecgOrderCustomerMapper;
import org.benben.modules.demo.test.service.IJeecgOrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单客户
 * @author： benben-boot
 * @date：   2019-02-15
 * @version： V1.0
 */
@Service
public class JeecgOrderCustomerServiceImpl extends ServiceImpl<JeecgOrderCustomerMapper, JeecgOrderCustomer> implements IJeecgOrderCustomerService {

	@Autowired
	private JeecgOrderCustomerMapper jeecgOrderCustomerMapper;
	
	@Override
	public List<JeecgOrderCustomer> selectCustomersByMainId(String mainId) {
		return jeecgOrderCustomerMapper.selectCustomersByMainId(mainId);
	}

}
