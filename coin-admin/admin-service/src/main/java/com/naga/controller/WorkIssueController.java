package com.naga.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.WorkIssue;
import com.naga.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workIssues")
@Api(tags = "工单管理")
public class WorkIssueController {

    @Autowired
    private WorkIssueService workIssueService;

    @GetMapping
    @ApiOperation("分页查询工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "pageNo"),
            @ApiImplicitParam(name = "size", value = "pageSize"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    @PreAuthorize("hasAuthority('work_issue_query')")
    public R<Page<WorkIssue>> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(workIssueService.findByPage(page, status, startTime, endTime));
    }

    @PostMapping
    @ApiOperation("新增工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workIssue", value = "workIssue Json Data"),
    })
    @PreAuthorize("hasAuthority('work_issue_create')")
    public R createWorkIssue(@RequestBody @Validated WorkIssue workIssue) {
        workIssue.setStatus(1);
        return workIssueService.save(workIssue) ? R.ok() : R.fail();
    }

    @PutMapping("/answer")
    @ApiOperation("回复工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工单主键"),
            @ApiImplicitParam(name = "answer", value = "回答的内容")
    })
    @PreAuthorize("hasAuthority('work_issue_update')")
    public R answerWorkIssue(Long id, String answer) {
        WorkIssue workIssue = WorkIssue.builder()
                .id(id)
                .answer(answer)
                .answerUserId(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()))
                .build();
        return workIssueService.updateById(workIssue) ? R.ok() : R.fail();
    }
}
