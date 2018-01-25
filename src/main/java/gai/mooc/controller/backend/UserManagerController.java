package gai.mooc.controller.backend;

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
 * Created by Administrator on 2018/1/24.
 */
@Controller
@RequestMapping("/manager/user")
public class UserManagerController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session){
        ServerResponse<User> response = userService.login(userName, password);
        if (response.isSuccess()){
            if (response.getData().getRole().equals(Constants.Role.ROLE_ADMIN)){
                session.setAttribute(Constants.CURRENT_USER, response.getData());
            }else{
                return ServerResponse.createError("非管理员角色登录");
            }
        }
        return response;
    }

}
