package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.dto.UserDto;
import com.naga.feign.UserServiceFeign;
import com.naga.service.WorkIssueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.WorkIssueMapper;
import com.naga.domain.WorkIssue;

@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService {

    @Autowired
    private UserServiceFeign userServiceFeign;

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
        Page<WorkIssue> iPage = page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(Objects.nonNull(status), WorkIssue::getStatus, status)
                .between(!StringUtils.isAnyEmpty(startTime, endTime), WorkIssue::getCreated, startTime, endTime + " 23:59:59"));
        List<WorkIssue> records = iPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return iPage;
        }

        List<Long> userIdList = records.stream().map(WorkIssue::getUserId).collect(Collectors.toList());
        List<UserDto> userDtoList = userServiceFeign.getBasicUsers(userIdList);
        Map<Long, UserDto> userDtoMap = userDtoList.stream().collect(Collectors.toMap(UserDto::getId, item -> item));
        records.forEach(workIssue -> {
            UserDto userDto = userDtoMap.get(workIssue.getUserId());
            workIssue.setUsername(userDto.getUsername());
            workIssue.setReadName(userDto.getRealName());
        });
        return iPage;
    }
}
