package com.naga.maps;


import com.naga.domain.User;
import com.naga.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // 用户转换的对象
    UserDtoMapper INSTANCE =  Mappers.getMapper(UserDtoMapper.class);

    /**
     * 将entity转化为dto
     * @param user    User对象
     * @return        UserDto对象
     */
    UserDto convert2Dto(User user);

    /**
     * 将dto转化为entity
     * @param userDto  UserDto对象
     * @return        User对象
     */
    User convert2Entity(UserDto userDto);

    /**
     * 将entity转化为dto
     * @param source    User对象 集合
     * @return          UserDto对象 集合
     */
    List<UserDto> convert2Dto(List<User> source) ;

    /**
     * 将dto对象转化为entity对象
     * @param source    UserDto对象 集合
     * @return          User对象 集合
     */
    List<User> convert2Entity(List<UserDto> source) ;
}
