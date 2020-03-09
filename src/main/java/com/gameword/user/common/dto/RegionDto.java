package com.gameword.user.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by majiancheng on 2019/12/22.
 */
public class RegionDto implements Serializable {

    private static final long serialVersionUID = -7606925834901668336L;

    private Integer regionId;

    private String regionName;

    private List<RegionDto> subRegions;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<RegionDto> getSubRegions() {
        return subRegions;
    }

    public void setSubRegions(List<RegionDto> subRegions) {
        this.subRegions = subRegions;
    }

}
