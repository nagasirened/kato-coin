package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.WorkIssueMapper;
import com.naga.domain.WorkIssue;
import com.naga.service.WorkIssueService;
@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService{

    /**
     * 分页查询工单信息
     * @param page
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        return page(page, new LambdaQueryWrapper<WorkIssue>()
                            .eq(Objects.nonNull(status), WorkIssue::getStatus, status)
                            .between(!StringUtils.isAnyEmpty(startTime, endTime), WorkIssue::getCreated, startTime, endTime + " 23:59:59"));
    }
}
