package com.naga.maps;


import com.naga.domain.SysUser;
import com.naga.dto.SysUserDto;
import com.naga.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysUserDtoMapper {

    // 用户转换的对象
    SysUserDtoMapper INSTANCE =  Mappers.getMapper(SysUserDtoMapper.class);

    /**
     * 将entity转化为dto
     * @param sysUser    SysUser对象
     * @return        SysUserDto对象
     */
    SysUserDto convert2Dto(SysUser sysUser);

    /**
     * 将dto转化为entity
     * @param sysUserDto  SysUserDto对象
     * @return            SysUser对象
     */
    SysUser convert2Entity(SysUserDto sysUserDto);

    /**
     * 将entity转化为dto
     * @param source    User对象 集合
     * @return          UserDto对象 集合
     */
    List<SysUserDto> convert2Dto(List<SysUser> source) ;

    /**
     * 将dto对象转化为entity对象
     * @param source    UserDto对象 集合
     * @return          User对象 集合
     */
    List<SysUser> convert2Entity(List<SysUserDto> source) ;
}
