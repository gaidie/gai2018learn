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
            TokenCache.setKey(TokenCache.TOKEN_PREFIX +userName, token);
            return ServerResponse.createSuccess(token);
        }
        return ServerResponse.createError("密码问题答案回答错误");
    }

    @Override
    public ServerResponse<String> resetPasswordByToken(String userName, String token, String passwordNew) {
        if (StringUtils.isEmpty(token)){
            return ServerResponse.createError("验证token不能为空");
        }
        if (StringUtils.isEmpty(passwordNew)){
            return ServerResponse.createError("重置密码不能为空");
        }
        ServerResponse<String> response = checkIfExists(userName, Constants.USERNAME_TYPE);
        if (response.isSuccess()){
            return ServerResponse.createError("用户名不存在");
        }
        String cacheToken = TokenCache.getValue(TokenCache.TOKEN_PREFIX + userName);
        if (StringUtils.isEmpty(cacheToken)){
            return ServerResponse.createError("token不存在或者已经失效,请重新获取token");
        }
        if (StringUtils.equals(token, cacheToken)){
            int updateCount = userMapper.updatePasswordByUserName(userName, passwordNew);
            if (updateCount > 0){
                return ServerResponse.createSuccess("密码重置成功");
            }
            return ServerResponse.createError("密码重置修改失败");
        }else {
            return ServerResponse.createError("重置密码token不一致");
        }
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        if (StringUtils.isEmpty(passwordOld) || StringUtils.isEmpty(passwordNew)){
            return ServerResponse.createError("用户名或者密码为空");
        }
        //防止横向越权
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (count < 0){
            return ServerResponse.createError("用户旧密码不匹配");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordOld));
        int resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0){
            return ServerResponse.createSuccess("密码修改成功");
        }else {
         return ServerResponse.createError("密码修改失败");
        }
    }

    @Override
    public ServerResponse<String> updateUserInfo(User user) {
        //用户名不能修改 email也不能重复
        int count = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (count >0 ){
            return ServerResponse.createError("邮箱已经被占用");
        }
        User updateUser =  new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setAnswer(user.getQuestion());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        int result = userMapper.updateByPrimaryKeySelective(updateUser);
        if (result > 0){
            return ServerResponse.createSuccess("用户信息修改成功");
        }else {
            return ServerResponse.createError("用户信息修改失败");
        }

    }
}
