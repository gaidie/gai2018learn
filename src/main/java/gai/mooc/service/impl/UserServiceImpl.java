package gai.mooc.service.impl;

import gai.mooc.bean.pojo.User;
import gai.mooc.common.ServerResponse;
import gai.mooc.dao.UserMapper;
import gai.mooc.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/1/15.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    public static final String USER_NAME_EXIST_MSG = "用户名已经存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String LOGIN_SUCCESS = "登录成功";
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        int userCount = userMapper.checkUserName(userName);
        if (userCount > 0){
            return ServerResponse.createError(USER_NAME_EXIST_MSG);
        }
        //密码MD5校验
        //todo
        User user = userMapper.selectLoginInfo(userName, password);
        if (user == null){
            return ServerResponse.createError(PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createSuccess(LOGIN_SUCCESS, user);
    }
}
