package com.naga.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel
public class RolePrivilegeParam {

    @ApiModelProperty(value = "角色ID")
    @NotNull
    private Long roleId;

    @ApiModelProperty(value = "权限ID的集合")
    private List<Long> privilegeIdList;
}
