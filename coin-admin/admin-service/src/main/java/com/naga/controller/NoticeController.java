package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.Notice;
import com.naga.model.R;
import com.naga.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Objects;

@RestController
@Api(tags = "公告管理控制器")
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    @ApiOperation(value = "分页查询公告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "pageSize"),
            @ApiImplicitParam(name = "title", value = "标题"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "status", value = "状态"),
    })
    @PreAuthorize("hasAuthority('notice_query')")
    public R<Page<Notice>> findByPage(@ApiIgnore Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<Notice> result = noticeService.findByPage(page, title, startTime, endTime, status);
        return R.ok(result);
    }

    @DeleteMapping
    @ApiOperation(value = "删除公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除的id的数组")
    })
    @PreAuthorize("hasAuthority('notice_delete')")
    public R deleteNotice(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("请选择要删除的内容");
        }
        return noticeService.removeByIds(Arrays.asList(ids)) ? R.ok() : R.fail();
    }

    @PostMapping("/switch/status")
    @ApiOperation(value = "启用/禁用公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要修改的公告的id"),
            @ApiImplicitParam(name = "status", value = "状态")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R updateStatus(Long id, Integer status) {
        Notice notice = Notice.builder().id(id).status(status).build();
        return noticeService.updateById(notice) ? R.ok() : R.fail();
    }

    @PostMapping
    @ApiOperation(value = "新增一个公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice", value = "Notice Json Data")
    })
    @PreAuthorize("hasAuthority('notice_create')")
    public R createNotice(@RequestBody @Validated Notice notice) {
        notice.setStatus(1);
        if (Objects.isNull(notice.getSort()))
            notice.setSort(0);
        boolean saveResult = noticeService.save(notice);
        return saveResult ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改一个公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice", value = "Notice Json Data")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R updateNotice(@RequestBody @Validated Notice notice) {
        return noticeService.updateById(notice) ? R.ok() : R.fail();
    }
}

