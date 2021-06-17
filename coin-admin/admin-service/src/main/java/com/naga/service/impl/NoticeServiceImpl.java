package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.NoticeMapper;
import com.naga.domain.Notice;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{

    @Override
    public Page<Notice> findByPage(Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        LambdaQueryWrapper<Notice> lambdaQueryWrapper = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.isNotEmpty(title), Notice::getTitle, title)
                .between(!StringUtils.isAnyEmpty(startTime, endTime), Notice::getCreated, startTime, endTime + " 23:59:59")
                .eq(Objects.nonNull(status), Notice::getStatus, status);
        return page(page, lambdaQueryWrapper);
    }
}
