package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.WorkIssue;
import com.baomidou.mybatisplus.extension.service.IService;
public interface WorkIssueService extends IService<WorkIssue>{

    /**
     * 分页查询工单信息
     * @param page
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime);
}
