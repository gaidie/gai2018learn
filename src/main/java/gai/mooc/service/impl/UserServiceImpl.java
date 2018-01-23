package gai.mooc.service.impl;

import gai.mooc.bean.pojo.User;
import gai.mooc.common.Constants;
import gai.mooc.common.ServerResponse;
import gai.mooc.common.TokenCache;
import gai.mooc.dao.UserMapper;
import gai.mooc.service.IUserService;
import gai.mooc.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Administrator on 2018/1/15.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    public static final String USER_NAME_EXIST_MSG = "用户名已经存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String USER_NOT_EXIST = "用户名不存在";
    public static final String EMAIL_EXISTS_MSG = "邮箱已经存在";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String REGISTER_FAILED = "注册失败";
    public static final String ERR_PARAMTERS = "参数错误";
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        int userCount = userMapper.checkUserName(userName);
        if (userCount == 0){
            return ServerResponse.createError(USER_NOT_EXIST);
        }
        //密码MD5校验
        password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLoginInfo(userName, password);
        if (user == null){
            return ServerResponse.createError(PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createSuccess(LOGIN_SUCCESS, user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        int userCount = userMapper.checkUserName(user.getUsername());
        if (userCount > 0){
            return ServerResponse.createError(USER_NAME_EXIST_MSG);
        }
        int emailCount = userMapper.checkEmail(user.getEmail());
        if (emailCount > 0){
            return ServerResponse.createError(EMAIL_EXISTS_MSG);
        }
        user.setRole(Constants.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        userCount = userMapper.insert(user);
        if (userCount == 0){
            return ServerResponse.createError(REGISTER_FAILED);
        }
        return ServerResponse.createSuccess(REGISTER_SUCCESS);
    }

    @Override
    public ServerResponse<String> checkIfExists(String userNameOrEmail, String type){
        if (StringUtils.isNoneBlank(type)){
            if (Constants.EMAIL_TYPE.equals(type)){
                int emailCount = userMapper.checkEmail(userNameOrEmail);
                if (emailCount > 0){
                    return ServerResponse.createError(EMAIL_EXISTS_MSG);
                }
            }
            if (Constants.USERNAME_TYPE.equals(type)){
                int userCount = userMapper.checkUserName(userNameOrEmail);
                if (userCount > 0){
                    return ServerResponse.createError(USER_NAME_EXIST_MSG);
                }
            }
        }else{
            return ServerResponse.createError(ERR_PARAMTERS);
        }
        return ServerResponse.createSuccess("校验成功");
    }

    /**
     * 获取忘记密码提示问题
     * @param userName
     * @return
     */
    @Override
    public ServerResponse<String> getForgetQuestion(String userName) {
        ServerResponse<String> response = checkIfExists(userName, Constants.USERNAME_TYPE);
        if (response.isSuccess()){
            return ServerResponse.createError("用户存不存在");
        }
        String question = userMapper.selectQuestionByUserName(userName);
        if (StringUtils.isNoneBlank(question)){
            return ServerResponse.createSuccess(question);
        }
        return ServerResponse.createError("忘记密码提示问题为空");
    }

    @Override
    public ServerResponse<String> answerForgetQuestion(String userName, String question, String answer) {
        int resultCount = userMapper.checkQuestionAnswer(userName, question, answer);
        if (resultCount > 0){
            //回答正确
            String token = UUID.randomUUID().toString();
            TokenCache.setKey("token_"+userName, token);
            return ServerResponse.createSuccess(token);
        }
        return ServerResponse.createError("密码问题答案回答错误");

    }
}
