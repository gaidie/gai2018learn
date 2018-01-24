package gai.mooc.controller;

import gai.mooc.bean.pojo.User;
import gai.mooc.common.Constants;
import gai.mooc.common.ServerResponse;
import gai.mooc.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/1/15.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(userName, password);
        if (response.isSuccess()){
            session.setAttribute(Constants.CURRENT_USER, response.getData());
        }

        return response;
    }

    @RequestMapping(value = "/loginOut.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginOut(HttpSession session){
        session.removeAttribute(Constants.CURRENT_USER);
        return ServerResponse.createSuccess();
    }

    /**
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "/checkIfExists.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkIfExist(String userOrEamil, String type){
        return iUserService.checkIfExists(userOrEamil, type);
    }

    /**
     * 获取登录用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "/getUser.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user != null){
            return ServerResponse.createSuccess(user);
        }
        return ServerResponse.createError("未获取到登录用户信息");
    }

    @RequestMapping(value="/getForgetQuestion.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getForgetQuestion(String userName){
        return iUserService.getForgetQuestion(userName);
    }

    @RequestMapping(value="/answerForgetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getForgetQuestion(String userName, String question, String answer){
        return iUserService.answerForgetQuestion(userName, question, answer);
    }

    @RequestMapping(value="/resetPasswordByToken.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswordByToken(String userName, String token, String passwordNew){
        return iUserService.resetPasswordByToken(userName, token, passwordNew);
    }

    @RequestMapping(value="/resetPasswordByToken.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        return iUserService.resetPassword(passwordOld,passwordNew, user);
    }

    @RequestMapping(value="/updateUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateUserInfo(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        user.setId(currentUser.getId());
        return iUserService.updateUserInfo(user);
    }

}
