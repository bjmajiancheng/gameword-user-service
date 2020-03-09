package com.gameword.user.common.handler;

import com.gameword.user.common.utils.CamelUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by majiancheng on 2019/9/15.
 */
public class Sortable implements Serializable {

    private static final long serialVersionUID = 9213896339757763626L;

    @Transient
    @JsonIgnore
    private String sort_;

    @Transient
    @JsonIgnore
    private Integer pageNum;

    @Transient
    @JsonIgnore
    private Integer pageSize;

    public String getSort_() {
        if (StringUtils.isNotEmpty(getSortWithOutOrderBy())) return "order by " + getSortWithOutOrderBy();
        return "";
    }

    public void setSort_(String sort_) {
        this.sort_ = sort_;
    }

    public String getSortWithOutOrderBy() {
        String sortStr = "";
        if (!StringUtils.isEmpty(sort_)) {
            String[] sortArr = sort_.split(",");
            StringBuffer sb = new StringBuffer();
            for(String sort : sortArr) {
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                if (sort.indexOf("_desc") != -1) {
                    String filed = CamelUtil.camelToUnderline(sort.replace("_desc", ""));
                    sb.append("`" + filed + "`" + " desc");
                } else if (sort.indexOf("_asc") != -1) {
                    String filed = CamelUtil.camelToUnderline(sort.replace("_asc", ""));
                    sb.append("`" + filed + "`" + " asc");
                }
            }

            sortStr = sb.toString();
        }
        return sortStr;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void initPageInfo() {
        if(this.getPageNum() == null) {
            this.setPageNum(1);
        }

        if(this.getPageSize() == null) {
            this.setPageSize(15);
        }
    }

}
