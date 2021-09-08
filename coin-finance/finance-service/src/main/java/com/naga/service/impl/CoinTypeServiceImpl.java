package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.CoinType;
import com.naga.service.CoinTypeService;
import com.naga.mapper.CoinTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class CoinTypeServiceImpl extends ServiceImpl<CoinTypeMapper, CoinType> implements CoinTypeService{

    /**
     * 分页查询
     * @param page      分页信息
     * @param code      币种类型Code
     * @return  币种类型的分页数据
     */
    @Override
    public Page<CoinType> findByPage(Page<CoinType> page, String code) {
        return page(page, new LambdaQueryWrapper<CoinType>()
                .like(StringUtils.isNotEmpty(code), CoinType::getCode, code));
    }


    /**
     * 使用币种类型的状态查询所有的币种类型值
     * @param status    状态
     * @return          币种集合
     */
    @Override
    public List<CoinType> listByStatus(Byte status) {
        return list(new LambdaQueryWrapper<CoinType>().eq(Objects.nonNull(status), CoinType::getStatus, status));
    }
}




