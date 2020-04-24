/*
 * 北京果敢时代科技有限公司
 * 北京市朝阳区望京SOHO T3 B座1607
 * 邮编：100022
 * 网址：www.davdian.com
 */

package com.gameword.user.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gameword.user.common.baseservice.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gameword.user.user.model.CityModel;
import tk.mybatis.mapper.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import com.gameword.user.user.dao.*;
import com.gameword.user.user.service.*;

/**
 * @author davdian
 * @version 1.0
 * @since 1.0
 */

@Service("cityService")
public class CityServiceImpl extends BaseService<CityModel> implements ICityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public CityModel findById(Integer id) {
        if (id == null) {
            return null;
        }
        return cityMapper.findById(id);
    }

    public List<CityModel> find(Map<String, Object> param) {
        return cityMapper.find(param);
    }

    @Override
    public PageInfo<CityModel> selectByFilterAndPage(CityModel cityModel, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CityModel> list = this.selectByFilter(cityModel);
        return new PageInfo<>(list);
    }

    @Override
    public List<CityModel> selectByFilter(CityModel cityModel) {
        Example example = new Example(CityModel.class);
        Example.Criteria criteria = example.createCriteria();

        if(cityModel.getCountryId() != null) {
            criteria.andEqualTo("countryId", cityModel.getCountryId());
        }

        criteria.andEqualTo("isDel", 0);
        criteria.andEqualTo("isOnline", 1);

        if (StringUtils.isNotEmpty(cityModel.getSortWithOutOrderBy())) {
            example.setOrderByClause(cityModel.getSortWithOutOrderBy());
        }
        return getMapper().selectByExample(example);
    }

    public Map<Integer,CityModel> findMapByCityIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

        List<CityModel> cityModels = this.find(Collections.singletonMap("ids", ids));
        Map<Integer, CityModel> cityMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(cityModels)) {
            for(CityModel cityModel : cityModels) {
                cityMap.put(cityModel.getId(), cityModel);
            }
        }

        return cityMap;
    }
}
