package org.benben.modules.business.userposts.service.impl;

import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.mapper.PostsMapper;
import org.benben.modules.business.userposts.service.IPostsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 帖子表的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
