/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.coolplay.system.system.model;

import com.coolplay.system.common.handler.Sortable;

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
@Table(name = "d_circle_post")
public class CirclePostModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "circle_id")
	private Integer circleId;//"酷玩圈ID"

	@Column(name = "post_id")
	private Integer postId;//"帖子ID"

	@Column(name = "is_top")
	private Integer isTop;//"是否置顶"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	//columns END

	@Transient
	private String circleName;//"酷玩圈名称"
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setCircleId(Integer circleId) {
		this.circleId = circleId;
	}

	public Integer getCircleId() {
		return this.circleId;
	}
		
	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getPostId() {
		return this.postId;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
}

