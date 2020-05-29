package com.wym.mybatis.param;

import com.wym.mybatis.validate.ICreate;
import com.wym.mybatis.validate.IQuery;
import com.wym.mybatis.validate.IUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class UserQueryParam {

    @NotNull(groups = {IUpdate.class, IQuery.class}, message = "id不能为空")
    private Integer id;

    @NotNull(groups = {ICreate.class}, message = "name不能为空")
    private String name;

    private Integer age;
}
