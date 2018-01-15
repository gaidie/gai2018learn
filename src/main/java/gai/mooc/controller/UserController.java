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

}
