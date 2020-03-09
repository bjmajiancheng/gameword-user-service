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
@Table(name = "d_station_detail")
public class StationDetailModel extends Sortable {
	private static final long serialVersionUID = 1L;

	//columns START
	@Column(name = "id")
	private Integer id;//"主键"

	@Column(name = "station_id")
	private Integer stationId;//"驿站ID"

	@Column(name = "type")
	private Integer type;//"类型 1：中文 2：英文"

	@Column(name = "city_title")
	private String cityTitle;//"城市标题"

	@Column(name = "topic_img")
	private String topicImg;//"主题图"

	@Column(name = "thumb_img")
	private String thumbImg;//"缩略图"

	@Column(name = "desc")
	private String desc;//"简介"

	@Column(name = "business_desc")
	private String businessDesc;//"工商业"

	@Column(name = "travel_desc")
	private String travelDesc;//"旅游"

	@Column(name = "education_desc")
	private String educationDesc;//"教育"

	@Column(name = "medical_desc")
	private String medicalDesc;//"医疗"

	@Column(name = "specialty_desc")
	private String specialtyDesc;//"特产"

	@Column(name = "holiday_desc")
	private String holidayDesc;//"节庆"

	@Column(name = "culture_desc")
	private String cultureDesc;//"文化"

	@Column(name = "food_desc")
	private String foodDesc;//"美食"

	@Column(name = "sport_desc")
	private String sportDesc;//"体育"

	@Column(name = "climate_desc")
	private String climateDesc;//"气候"

	@Column(name = "celebrity_desc")
	private String celebrityDesc;//"名人"

	@Column(name = "tips_desc")
	private String tipsDesc;//"锦囊"

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
		
	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getStationId() {
		return this.stationId;
	}
		
	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return this.type;
	}
		
	public void setCityTitle(String cityTitle) {
		this.cityTitle = cityTitle;
	}

	public String getCityTitle() {
		return this.cityTitle;
	}
		
	public void setTopicImg(String topicImg) {
		this.topicImg = topicImg;
	}

	public String getTopicImg() {
		return this.topicImg;
	}
		
	public void setThumbImg(String thumbImg) {
		this.thumbImg = thumbImg;
	}

	public String getThumbImg() {
		return this.thumbImg;
	}
		
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}
		
	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	public String getBusinessDesc() {
		return this.businessDesc;
	}
		
	public void setTravelDesc(String travelDesc) {
		this.travelDesc = travelDesc;
	}

	public String getTravelDesc() {
		return this.travelDesc;
	}
		
	public void setEducationDesc(String educationDesc) {
		this.educationDesc = educationDesc;
	}

	public String getEducationDesc() {
		return this.educationDesc;
	}
		
	public void setMedicalDesc(String medicalDesc) {
		this.medicalDesc = medicalDesc;
	}

	public String getMedicalDesc() {
		return this.medicalDesc;
	}
		
	public void setSpecialtyDesc(String specialtyDesc) {
		this.specialtyDesc = specialtyDesc;
	}

	public String getSpecialtyDesc() {
		return this.specialtyDesc;
	}
		
	public void setHolidayDesc(String holidayDesc) {
		this.holidayDesc = holidayDesc;
	}

	public String getHolidayDesc() {
		return this.holidayDesc;
	}
		
	public void setCultureDesc(String cultureDesc) {
		this.cultureDesc = cultureDesc;
	}

	public String getCultureDesc() {
		return this.cultureDesc;
	}
		
	public void setFoodDesc(String foodDesc) {
		this.foodDesc = foodDesc;
	}

	public String getFoodDesc() {
		return this.foodDesc;
	}
		
	public void setSportDesc(String sportDesc) {
		this.sportDesc = sportDesc;
	}

	public String getSportDesc() {
		return this.sportDesc;
	}
		
	public void setClimateDesc(String climateDesc) {
		this.climateDesc = climateDesc;
	}

	public String getClimateDesc() {
		return this.climateDesc;
	}
		
	public void setCelebrityDesc(String celebrityDesc) {
		this.celebrityDesc = celebrityDesc;
	}

	public String getCelebrityDesc() {
		return this.celebrityDesc;
	}
		
	public void setTipsDesc(String tipsDesc) {
		this.tipsDesc = tipsDesc;
	}

	public String getTipsDesc() {
		return this.tipsDesc;
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

