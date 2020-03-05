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
import java.util.List;
import java.util.Map;
import javax.persistence.*;

/**
 * @author  shawn
 * @version 1.0
 * @since 1.0
 */
@Table(name = "d_post")
public class PostModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//"主键"

	@Column(name = "post_title")
	private String postTitle;//"帖子标题"

	@Column(name = "post_content")
	private String postContent;//"帖子内容（支持富文本）"

	@Column(name = "user_id")
	private Integer userId;//"帖子创建人"

	@Column(name = "img_urls")
	private String imgUrls;//"图片地址"

	@Column(name = "video_url")
	private String videoUrl;//"视频链接"

	@Column(name = "videl_path")
	private String videlPath;//"视频存储路径"

	@Column(name = "is_top")
	private Integer isTop;//"是否置顶"

	@Column(name = "top_time")
	private String topTime;//"置顶时间"

	@Column(name = "like_cnt")
	private Integer likeCnt;//"点赞人数"

	@Column(name = "share_cnt")
	private Integer shareCnt;//"分享人数"

	@Column(name = "comment_cnt")
	private Integer commentCnt;//"评论人数"

	@Column(name = "read_cnt")
	private Integer readCnt;//"阅读人数"

	@Column(name = "is_del")
	private Integer isDel;//"是否删除（0：未删除，1：已删除）"

	@Column(name = "c_time")
	private Date ctime;//"创建时间"

	//columns END

	@Transient
	private Integer labelId;//"标签Id"

	@Transient
	private String publicUserName;//"发布用户名"

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}
		
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostTitle() {
		return this.postTitle;
	}
		
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	public String getPostContent() {
		return this.postContent;
	}
		
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return this.userId;
	}
		
	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getImgUrls() {
		return this.imgUrls;
	}
		
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoUrl() {
		return this.videoUrl;
	}
		
	public void setVidelPath(String videlPath) {
		this.videlPath = videlPath;
	}

	public String getVidelPath() {
		return this.videlPath;
	}
		
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getIsTop() {
		return this.isTop;
	}
		
	public void setLikeCnt(Integer likeCnt) {
		this.likeCnt = likeCnt;
	}

	public Integer getLikeCnt() {
		return this.likeCnt;
	}
		
	public void setShareCnt(Integer shareCnt) {
		this.shareCnt = shareCnt;
	}

	public Integer getShareCnt() {
		return this.shareCnt;
	}
		
	public void setCommentCnt(Integer commentCnt) {
		this.commentCnt = commentCnt;
	}

	public Integer getCommentCnt() {
		return this.commentCnt;
	}
		
	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	public Integer getReadCnt() {
		return this.readCnt;
	}
		
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getIsDel() {
		return this.isDel;
	}
		
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getCtime() {
		return this.ctime;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public String getPublicUserName() {
		return publicUserName;
	}

	public void setPublicUserName(String publicUserName) {
		this.publicUserName = publicUserName;
	}

	public void setTopTime(String topTime) {
		this.topTime = topTime;
	}

	public String getTopTime() {
		return topTime;
	}
}

