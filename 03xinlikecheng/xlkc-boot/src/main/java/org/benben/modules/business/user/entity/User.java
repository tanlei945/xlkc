package org.benben.modules.business.user.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: sadfas
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Data
@TableName("user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/**ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**中文姓名*/
	@Excel(name = "中文姓名", width = 15)
	private java.lang.String chinaname;
	/**英文姓名*/
	@Excel(name = "英文姓名", width = 15)
	private java.lang.String englishname;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
	private java.lang.String nickname;
	/**密码*/
	@Excel(name = "密码", width = 15)
	private java.lang.String password;
	/**numberid*/
	@Excel(name = "numberid", width = 15)
	private java.lang.Integer numberid;
	/**密码盐*/
	@Excel(name = "密码盐", width = 15)
	private java.lang.String salt;
	/**电子邮箱*/
	@Excel(name = "电子邮箱", width = 15)
	private java.lang.String email;
	/**区号名称*/
	@Excel(name = "区号名称", width = 15)
	private java.lang.String areaname;
	/**号码归属地区号*/
	@Excel(name = "号码归属地区号", width = 15)
	private java.lang.String areacode;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
	private java.lang.String mobile;
	/**推荐人*/
	@Excel(name = "推荐人", width = 15)
	private java.lang.String referrer;
	/**所在地区*/
	@Excel(name = "所在地区", width = 15)
	private java.lang.String address;
	/**所在公司*/
	@Excel(name = "所在公司", width = 15)
	private java.lang.String company;
	/**头像*/
	@Excel(name = "头像", width = 15)
	private java.lang.String avatar;
	/**等级*/
	@Excel(name = "等级", width = 15)
	private java.lang.Integer level;
	/**性别  0/男,1/女*/
	@Excel(name = "性别  0/男,1/女", width = 15)
	private java.lang.Integer sex;
	/**生日*/
	@Excel(name = "生日", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date birthday;
	/**格言*/
	@Excel(name = "格言", width = 15)
	private java.lang.String bio;
	/**余额*/
	@Excel(name = "余额", width = 15)
	private java.math.BigDecimal money;
	/**积分*/
	@Excel(name = "积分", width = 15)
	private java.lang.Integer score;
	/**连续登录天数*/
	@Excel(name = "连续登录天数", width = 15)
	private java.lang.Integer successIons;
	/**最大连续登录天数*/
	@Excel(name = "最大连续登录天数", width = 15)
	private java.lang.Integer maxsuccessIons;
	/**上次登录时间*/
	@Excel(name = "上次登录时间", width = 15)
	private java.lang.Integer prevTime;
	/**登录时间*/
	@Excel(name = "登录时间", width = 15)
	private java.lang.Integer loginTime;
	/**登录IP*/
	@Excel(name = "登录IP", width = 15)
	private java.lang.String loginip;
	/**失败次数*/
	@Excel(name = "失败次数", width = 15)
	private java.lang.Integer loginfailure;
	/**加入IP*/
	@Excel(name = "加入IP", width = 15)
	private java.lang.String joinip;
	/**加入时间*/
	@Excel(name = "加入时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date joinTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**编辑人*/
	@Excel(name = "编辑人", width = 15)
	private java.lang.String updateBy;
	/**Token*/
	@Excel(name = "Token", width = 15)
	private java.lang.String token;
	/**状态(1：正常  2：冻结 ）*/
	@Excel(name = "状态(1：正常  2：冻结 ）", width = 15)
	private java.lang.Integer status;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
	private java.lang.String delFlag;
	/**验证*/
	@Excel(name = "验证", width = 15)
	private java.lang.String verification;
	/**微信id*/
	@Excel(name = "微信id", width = 15)
	private java.lang.String wxId;
}
