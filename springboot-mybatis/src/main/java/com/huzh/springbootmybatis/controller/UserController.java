package com.huzh.springbootmybatis.controller;

import com.huzh.springbootmybatis.dao.UserMapper;
import com.huzh.springbootmybatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Date 2019/8/21 12:23
 * @Author huzh
 * @Version 1.0
 */
@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    //http://localhost:8888/getUser?username=xiaoli2
    @RequestMapping("/getUser")
    public String getUser(String username) {
        User user = userMapper.findUserByUsername(username);
        return user != null ? username + "的密码是：" + user.getPassword() : "不存在用户名为" + username + "的用户";
    }

    //http://localhost:8888/updateUser?username=xiaoli2&password=123
    @RequestMapping("/updateUser")
    public String updateUser(String password, String username) {
        User user = new User(username, password);
        userMapper.updateUserByUsername(user);
        return "success!";
    }


    //http://localhost:8888/addUser?username=xiaoli2&password=123
    @RequestMapping("/addUser")
    public String addUser(String username, String password) {
        User user = new User(username, password);
        userMapper.saveUser(user);
        return "success!";
    }

    //http://localhost:8888/addUser?username=xiaoli2
    @RequestMapping("/deleteUser")
    public String deleteUser(String username) {
        userMapper.deleteUserByUsername(username);
        return "success!";
    }

    //http://localhost:8888/getUserList
    @RequestMapping("/getUserList")
    public List<User> getUserList(String username, String password) {
        return userMapper.getUserList();
    }
}
