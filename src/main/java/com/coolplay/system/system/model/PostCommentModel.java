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
@Table(name = "d_post_comment")
public class PostCommentModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "post_id")
	private Integer postId;//"圈子ID"

	@Column(name = "comment_user_id")
	private Integer commentUserId;//"评论人"

	@Column(name = "comment_content")
	private String commentContent;//"评论内容"

	@Column(name = "comment_level")
	private Integer commentLevel;//"评论级别 1：直接评论帖子，2：回复帖子评论"

	@Column(name = "post_comment_id")
	private Integer postCommentId;//"当评论级别为2时，指定回复帖子ID"

	@Column(name = "is_del")
	private Integer isDel;//"是否删除"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	//columns END

	@Transient
	private String commentUserName;//"评论人名称"
		
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getPostId() {
		return this.postId;
	}
		
	public void setCommentUserId(Integer commentUserId) {
		this.commentUserId = commentUserId;
	}

	public Integer getCommentUserId() {
		return this.commentUserId;
	}
		
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentContent() {
		return this.commentContent;
	}
		
	public void setCommentLevel(Integer commentLevel) {
		this.commentLevel = commentLevel;
	}

	public Integer getCommentLevel() {
		return this.commentLevel;
	}
		
	public void setPostCommentId(Integer postCommentId) {
		this.postCommentId = postCommentId;
	}

	public Integer getPostCommentId() {
		return this.postCommentId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public String getCommentUserName() {
		return commentUserName;
	}

	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
}

