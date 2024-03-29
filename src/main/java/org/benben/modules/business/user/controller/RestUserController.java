package org.benben.modules.business.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.constant.CommonConstant;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.api.ISysBaseAPI;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.RedisUtil;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.bbuservideo.entity.BbUserVideo;
import org.benben.modules.business.bbuservideo.service.IBbUserVideoService;
import org.benben.modules.business.commen.dto.SmsDTO;
import org.benben.modules.business.commen.service.*;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.entity.UserThird;
import org.benben.modules.business.user.mapper.UserMapper;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.user.service.IUserThirdService;
import org.benben.modules.business.userwallet.entity.UserWallet;
import org.benben.modules.business.userwallet.service.IUserWalletService;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import org.benben.modules.business.userwalletevdence.service.IUserWalletEvdenceService;
import org.benben.modules.business.video.service.IVideoService;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 用户
 * @author： jeecg-boot
 * @date： 2019-04-19
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = {"用户接口"})
@Slf4j
public class RestUserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserThirdService userThirdService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IVideoService videoService;

    @Autowired
    private IWxService iWxService;

    @Autowired
    private IWbService iWbService;

    @Autowired
    private IQqService iQqService;

    @Autowired
    private ICommonService commonService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserMapper userMapper;

    @Autowired
	private IhuyiService ihuyiService;

    @Autowired
	private IBbUserVideoService bbUserVideoService;

	@Autowired
	private IUserWalletService userWalletService;

	@Autowired
	private IUserWalletEvdenceService walletEvdenceService;

	@PostMapping(value = "/verify_user")
//	@ApiOperation(value = "验证是否完善个人资料", tags = "用户接口",notes = "验证是否完善个人资料")
	public RestResponseBean verifyUser(@RequestParam String chinaname, @RequestParam String englishname, @RequestParam String referrer) {
		if (!StringUtils.isNotBlank(chinaname) || !StringUtils.isNotBlank(englishname) || !StringUtils.isNotBlank(referrer)) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		User user = userService.verifyUser(chinaname, englishname, referrer);
		if (user != null) {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"已完善");
		}
		return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
	}

	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/updateUser")
	@ApiOperation(value = "更新用户信息",tags = "用户接口",notes = "更新用户信息")
	public RestResponseBean updateUser(@RequestBody User user){
		User user1 = (User) SecurityUtils.getSubject().getPrincipal();

		if(user1==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		//判断用户信息是否为空
		if(user==null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
		}
		user.setId(user1.getId());
		if (userService.updateById(user)){
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),"更新用户成功！");
		}
		return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
	}
    /**
     * 通过id查询
     * @return
     */
    @GetMapping(value = "/queryById")
//   @ApiOperation(value = "通过id查询用户", tags = {"用户接口"}, notes = "通过id查询用户")
	@ApiOperation(value = "验证是否完善个人资料", tags = "用户接口",notes = "验证是否完善个人资料")
    public RestResponseBean queryById() {
		User user1 = (User) SecurityUtils.getSubject().getPrincipal();
        User user = userService.getById(user1.getId());

		if (!StringUtils.isNotBlank(user.getChinaname()) || !StringUtils.isNotBlank(user.getEnglishname()) || !StringUtils.isNotBlank(user.getReferrer())) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
    }


    /**
     * 用户注册
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册", tags = {"用户接口"}, notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean register(@RequestParam String mobile, @RequestParam String password,@RequestParam String areaname, @RequestParam String areacode, @RequestParam Integer verify,@RequestParam String event) {

		if (!ihuyiService.check(mobile, event, verify)) {
			return new RestResponseBean(ResultEnum.VERIFY_FAIL.getValue(),ResultEnum.VERIFY_FAIL.getDesc(),null);
		}
		JSONObject obj = new JSONObject();

		//检查手机号是否注册
		User user1 = userService.queryByMobile(mobile);
		if (user1 != null) {
			return new RestResponseBean(ResultEnum.MOBILE_EXIST_REGISTER.getValue(),ResultEnum.MOBILE_EXIST_REGISTER.getDesc(),null);
		}
        User user = new User();

        if(org.apache.commons.lang3.StringUtils.equals(mobile,"")|| org.apache.commons.lang3.StringUtils.equals(password,"")){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        try {
            //保存用户信息
            user.setMobile(mobile);
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(password, password, salt);
            user.setPassword(passwordEncode);
            user.setAreacode(areacode);
            user.setAreaname(areaname);
            user.setAvatar("https://xinlikecheng1.oss-cn-beijing.aliyuncs.com/1564199833898.png");
            user.setNickname(mobile);
            userService.save(user);
            String uid = userService.queryById(mobile);

			//注册用户的同时，绑定钱包
			UserWallet userWallet = new UserWallet();
			userWallet.setUserId(uid);
			userWallet.setMoney(new BigDecimal(0));
			userWalletService.save(userWallet);
			//绑定钱包凭证地址
			UserWalletEvdence userWalletEvdence = new UserWalletEvdence();
			userWalletEvdence.setUid(uid);
			walletEvdenceService.save(userWalletEvdence);
			//钱包地址绑定到我的钱包
			userWallet.setWalletEvdenceId(userWalletEvdence.getId());
			userWalletService.updateById(userWallet);

			System.out.println(uid);
			List<String> vids = videoService.query();
			for (int i = 0; i<vids.size() ; i++) {
				BbUserVideo bbUserVideo = new BbUserVideo();
				bbUserVideo.setUid(uid);
				bbUserVideo.setVid(vids.get(i));
				bbUserVideo.setState(1);
				bbUserVideoService.save(bbUserVideo);
			}


			obj = tokenBuild(user);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),obj);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }



    /**
     * 账户密码登录
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "账户密码登录", tags = {"用户接口"}, notes = "账户密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean login(@RequestParam String mobile, @RequestParam String password){
        JSONObject obj = new JSONObject();
        User user = userService.queryByMobile(mobile);

        if (user == null) {
            sysBaseAPI.addLog("登录失败，用户名:" + mobile + "不存在！", CommonConstant.LOG_TYPE_1, null);
            return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(), null);
        } else {
            //密码验证
            String userpassword = PasswordUtil.encrypt(password, password, user.getSalt());
            String syspassword = user.getPassword();
            if (!syspassword.equals(userpassword)) {
                return new RestResponseBean(ResultEnum.USER_PWD_ERROR.getValue(), ResultEnum.USER_PWD_ERROR.getDesc(), null);
            }
            //调用公共方法
            obj = tokenBuild(user);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), obj);
    }

    @PostMapping(value = "/mobileLogin")
	@ApiOperation(value = "验证码登录",tags = "用户接口",notes = "验证码登录")
	public RestResponseBean mobileLogin(@RequestParam String mobile, @RequestParam Integer verify, @RequestParam String event) {

		if (!ihuyiService.check(mobile,event,verify)) {
			return new RestResponseBean(ResultEnum.VERIFY_FAIL.getValue(),ResultEnum.VERIFY_FAIL.getDesc(),null);
		}
		if (StringUtils.isBlank(mobile)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
		}

		User user = userService.queryByMobile(mobile);

		if (user == null) {
			return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(), null);
		}

		return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), tokenBuild(user));
	}

    /**
     * 用户修改
     * @param user
     * @return
     */
    @PostMapping(value = "/edit")
  //  @ApiOperation(value = "用户修改", tags = {"用户接口"}, notes = "用户修改")
    public RestResponseBean edit(@RequestBody User user) {

        User userEntity = userService.getById(user.getId());

        if(userEntity==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {

            if(org.apache.commons.lang3.StringUtils.isNotBlank(user.getMobile())&& org.apache.commons.lang3.StringUtils.isNotBlank(user.getPassword())){
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getMobile(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
            }

            boolean ok = userService.updateById(user);

            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),user);
    }



    @PostMapping(value = "/updateAvatar")
    @ApiOperation(value = "修改头像", tags = {"用户接口"}, notes = "修改头像")
 /*   @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "file",value = "待上传图片",dataType = "MultipartFile",required = true)
    })*/
    public RestResponseBean changeAvatar(@RequestParam String userId,@RequestParam(value = "file") MultipartFile[] file){

       /* if(org.apache.commons.lang3.StringUtils.isBlank(userId) || org.apache.commons.lang3.StringUtils.isBlank(file.getOriginalFilename())){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }*/

        User user = userService.getById(userId);
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }

		String result = "";
		try {
			result = OSSClientUtils.fileUpload(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
        user.setAvatar(result);

        if(userService.updateById(user)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }

  /*  @PostMapping(value = "/updateUsername")
    @ApiOperation(value = "修改用户名", tags = {"用户接口"}, notes = "修改用户名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "username",value = "用户名",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean changeUsername(@RequestParam String userId,@RequestParam String username){

        if(org.apache.commons.lang3.StringUtils.isBlank(userId) || org.apache.commons.lang3.StringUtils.isBlank(username)){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        UserPostsVos user = userService.getById(userId);
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }
        user.setUsername(username);

        if(userService.updateById(user)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }
*/
    /**
     * 修改手机号
     * @param mobile
     * @return
     */
    @PostMapping(value = "/updateMobile")
    @ApiOperation(value = "修改手机号", tags = {"用户接口"}, notes = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
//            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean changeMobile(@RequestParam String mobile) {
    	User user = (User)SecurityUtils.getSubject().getPrincipal();

        if(StringUtils.isBlank(user.getId()) || org.apache.commons.lang3.StringUtils.isBlank(mobile)){

            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.desc(),null);
        }
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile",mobile);

		User user1 = userMapper.selectOne(queryWrapper);
		if (user1 != null) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.desc(),"該手機號已經註冊過！");
		}
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }
        user.setMobile(mobile);
        if(userService.updateById(user)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),"修改成功");
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),"修改失败");
    }

	/**
	 * 通过id查询
	 * @param
	 * @return
	 */
	@GetMapping(value = "/getUser")
	@ApiOperation(value = "根据id获取用户详情",tags = {"用户接口"},notes = "根据id获取用户详情")
	public RestResponseBean getUser() {
		User user1 = (User) SecurityUtils.getSubject().getPrincipal();
		Result<User> result = new Result<User>();
		User user = userService.getById(user1.getId());
		if(user==null) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),"未找到对应实体");
		}else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
		}
	}

    @PostMapping(value = "/forgetPassword")
    @ApiOperation(value = "忘记密码/修改密码", tags = {"用户接口"}, notes = "忘记密码/修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String"),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String"),
            @ApiImplicitParam(name = "verify",value = "验证码",dataType = "String")
    })
    public RestResponseBean forgetPassword(@RequestParam String mobile,@RequestParam String password, @RequestParam Integer verify,@RequestParam String event) {

		if (!ihuyiService.check(mobile, event, verify)) {
			return new RestResponseBean(ResultEnum.VERIFY_FAIL.getValue(),ResultEnum.VERIFY_FAIL.getDesc(),null);
		}

        if(org.apache.commons.lang3.StringUtils.equals(mobile,"")|| org.apache.commons.lang3.StringUtils.equals(password,"")){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        //首先检查是否和原密码一样
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile",mobile);

		User user = userMapper.selectOne(queryWrapper);
		String pw = PasswordUtil.encrypt(password, password, user.getSalt());
		if (pw.equals(user.getPassword())) {
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.desc(),"該密碼和原來一致！");
		}

		if(userService.forgetPassword(mobile,password) == 0){
            return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

    /**
     *   通过id删除
     * @param id
     * @return
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "用户删除", tags = {"用户接口"}, notes = "用户删除")
    public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {

        User user = userService.getById(id);

        if(user==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        userService.delMain(id);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
    }


    /**
     * 根据姓名查找
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/queryByName")
    public User queryByName(@RequestParam String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userService.getOne(userQueryWrapper);
        return user;
    }


    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logOut")
    @ApiOperation(value = "退出", tags = {"用户接口"}, notes = "退出")
    public RestResponseBean logOut() {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String token = JwtUtil.sign(CommonConstant.SIGN_MEMBER_USER + user.getMobile(), user.getPassword());
        redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }


    /**
     * qq登录回调
     * @param request
     * @return
     */
    @GetMapping("/qq_login_callback")
    public RestResponseBean qqLoginCallback(HttpServletRequest request) {

        //获取回调
        String openid = iQqService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"0");
    }

    /**
     * 微信回调
     * @param request
     * @return
     */
    @GetMapping(value = "/wx_login_callBack")
    public RestResponseBean callBack(HttpServletRequest request) {

        User userEntity = null;
        //获取回调
        String openid = iWxService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"1");
    }



    /**
     * 微博回调
     * @param request
     * @return
     */
    @GetMapping(value = "/wb_login_callback")
    public RestResponseBean wbCallBack(HttpServletRequest request) {

        User userEntity = null;
        //获取回调
        String openid = iWbService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"2");
    }


    @GetMapping(value = "/isExistMobile")
    @ApiOperation(value = "手机号是否已被注册",tags = {"用户接口"},notes = "手机号是否已被注册")
    public RestResponseBean isExistMobile(@RequestParam  String mobile, @RequestParam Integer type){

        User user = userService.queryByMobile(mobile);

        if (type != null) {
        	if (type == 0){
				if(user == null){
					return new RestResponseBean(ResultEnum.MOBILE1_NOT_EXIST.getValue(),ResultEnum.MOBILE_NOT_EXIST.getDesc(),null);
				} else {
					return new RestResponseBean(ResultEnum.MOBILE1_EXIST.getValue(),ResultEnum.MOBILE_EXIST.getDesc(),null);
				}
			} else{
				if(user == null){
					return new RestResponseBean(ResultEnum.MOBILE_NOT_EXIST.getValue(),ResultEnum.MOBILE_NOT_EXIST.getDesc(),null);
				} else {
					return new RestResponseBean(ResultEnum.MOBILE_EXIST.getValue(),ResultEnum.MOBILE_EXIST.getDesc(),null);
				}
			}

		}else {
			return new RestResponseBean(ResultEnum.MOBILE_EXIST.getValue(),ResultEnum.MOBILE_EXIST.getDesc(),"n");

		}


    }

    /**
     * 公共方法,三方登录公共回调
     * @param openid
     * @param mobile
     * @param type
     * @return
     */
    private RestResponseBean publicCallBack(String openid,String mobile,String type){

        User userEntity = null;

        //返回空则表明回调异常
        if (StringUtils.isBlank(openid)) {
            return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
        }
        //查询登录状态
        User user = userService.queryByMobile(mobile);
        //查询是否已有账户绑定该微博
        UserThird userThird = userThirdService.queryByOpenid(openid);
        if(userThird != null){
            //查询绑定的用户信息
            userEntity = userService.getById(userThird.getUserId());
        }
        //未登录
        if (user == null) {
            //未登录,未绑定
            if (userEntity == null) {
                return new RestResponseBean(ResultEnum.UNBOUND_OPENID.getValue(), ResultEnum.UNBOUND_OPENID.getDesc(), null);
            }
            //未登录,已绑定,返回登录信息token等
            return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), tokenBuild(userEntity));
        }
        //已登录,未绑定
        if (userEntity == null) {

            switch (type){
                case "0":
                    //绑定QQ
                    if (userService.bindingThird(openid,user.getId(),"0") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
                case "1":
                    //绑定微信
                    if (userService.bindingThird(openid,user.getId(),"1") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
                default :
                    //绑定微博
                    if (userService.bindingThird(openid,user.getId(),"2") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
            }

            //绑定失败
            return new RestResponseBean(ResultEnum.BINDING_FAIL.getValue(), ResultEnum.BINDING_FAIL.getDesc(), null);
        }
        //已登录,禁止重复绑定
        return new RestResponseBean(ResultEnum.REPEATED_BINDING.getValue(), ResultEnum.REPEATED_BINDING.getDesc(), null);
    }

    /**
     * 公共方法,三方登录返回信息token
     *
     * @param user 根据传参查询到的实体
     * @return
     */
    private JSONObject tokenBuild(User user) {

        JSONObject obj = new JSONObject();
        //生成token
        String token = JwtUtil.signUser(CommonConstant.SIGN_MEMBER_USER + user.getMobile(), user.getPassword());
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        //设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);

        obj.put("token", token);
        obj.put("user", user);

        sysBaseAPI.addLog("手机号: " + user.getMobile() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return obj;
    }

    /**
     * 三方登录(后端调起授权)
     * @param platform
     * @param mobile
     */
    @GetMapping(value = "/third")
//    @ApiOperation(value = "三方登录", tags = {"用户接口"}, notes = "三方登录")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "platform",value = "平台类型('1':QQ,'2':微信,'3':微博)",dataType = "String",defaultValue = "1",required = true),
//            @ApiImplicitParam(name = "mobile",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
//    })
    public void third(@RequestParam String platform, @RequestParam String mobile, HttpServletResponse response){

        switch (platform){
            case "1":
                iQqService.login(mobile,response);
                break;
            case "2":
                iWxService.wxLogin(mobile, response);
                break;
            default:
                iWbService.login(mobile, response);
                break;
        }

    }

}
