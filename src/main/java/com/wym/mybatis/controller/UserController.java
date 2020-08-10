package com.wym.mybatis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wym.common.model.User;
import com.wym.mybatis.param.UserQueryParam;
import com.wym.common.response.Response;
import com.wym.mybatis.service.IUserService;
import com.wym.mybatis.validate.ICreate;
import com.wym.mybatis.validate.IQuery;
import com.wym.mybatis.validate.IUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getSimpleName());

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "获取用户")
    @PostMapping("getUser.do")
    public String getUser(@RequestBody @Validated(IQuery.class) UserQueryParam param) throws JsonProcessingException {
        List<User> list = userService.getList();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(list);
        logger.info(s);
        return s;
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("getUsers.do")
    public String getUser() throws JsonProcessingException {
//        List<User> list = userService.getList();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String s = objectMapper.writeValueAsString(list);
//        logger.info(s);
        return "Success";
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("updateUser.do")
    public String updateUser(@RequestBody @Validated(IUpdate.class) UserQueryParam param) throws JsonProcessingException {
        List<User> list = userService.getList();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(list);
        logger.info(s);
        return s;
    }

    @ApiOperation(value = "获取用户")
    @PostMapping("getUserList.do")
    public Response<List<User>> getUserList(@Validated String id) throws JsonProcessingException {
        List<User> list = userService.getList();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(list);
        logger.info(s);
        return Response.ok(list, "");
    }

    @ApiOperation(value = "新增用户")
    @GetMapping("insertUser.do")
    public Response<User> insertUser(@Validated(ICreate.class) UserQueryParam param) throws JsonProcessingException {
        logger.info("insert thread id:{}", Thread.currentThread().getId());
        User user = new User();
        BeanUtils.copyProperties(param, user);
        userService.insert(user);
        return Response.ok(user, "");
    }
}
