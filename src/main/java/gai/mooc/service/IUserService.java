package gai.mooc.service;

import gai.mooc.bean.pojo.User;
import gai.mooc.common.ServerResponse;


/**
 * Created by Administrator on 2018/1/15.
 */
public interface IUserService {

    ServerResponse<User> login(String userName, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkIfExists(String userNameOrEmail, String type);

    ServerResponse<String> getForgetQuestion(String userName);

    ServerResponse<String> answerForgetQuestion(String userName, String question, String answer);


}
