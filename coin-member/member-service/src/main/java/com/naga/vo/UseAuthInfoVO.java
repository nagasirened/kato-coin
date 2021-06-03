package com.naga.vo;


import com.naga.domain.User;
import com.naga.domain.UserAuthAuditRecord;
import com.naga.domain.UserAuthInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class UseAuthInfoVO {

    @ApiModelProperty(name = "用户详情")
    private User user;

    @ApiModelProperty(name = "用户实名认证信息 用户提交的照片之类的")
    private List<UserAuthInfo> userAuthInfoList;

    @ApiModelProperty(name = "用户实名认证审核信息 审核结果，审核人等")
    private List<UserAuthAuditRecord> userAuthAuditRecordList;

}
