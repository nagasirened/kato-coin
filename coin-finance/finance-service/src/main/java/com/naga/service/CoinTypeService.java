package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.CoinType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 币种类型管理Service
 */
public interface CoinTypeService extends IService<CoinType> {

    /**
     * 分页查询
     * @param page      分页信息
     * @param code      币种类型Code
     * @return  币种类型的分页数据
     */
    Page<CoinType> findByPage(Page<CoinType> page, String code);

    /**
     * 使用币种类型的状态查询所有的币种类型值
     * @param status    状态
     * @return          币种集合
     */
    List<CoinType> listByStatus(Byte status);
}
