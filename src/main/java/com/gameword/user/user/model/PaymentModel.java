/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.model;

import com.gameword.user.common.handler.Sortable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_payment")
public class PaymentModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "pay_type")
	private Integer payType;//"支付类型 1、微信；2、支付宝；3、PayPal"

	@Column(name = "pay_money")
	private BigDecimal payMoney;//"充值金额"

	@Column(name = "user_id")
	private Integer userId;//"支付用户ID"

	@Column(name = "pay_time")
	private Date payTime;//"支付时间"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	@Column(name = "u_time")
	private Date utime;//"更新时间"

	//columns END
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayType() {
		return this.payType;
	}
		
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public BigDecimal getPayMoney() {
		return this.payMoney;
	}
		
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return this.userId;
	}
		
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getPayTime() {
		return this.payTime;
	}
		
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}
		
	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Date getUtime() {
		return this.utime;
	}

}

