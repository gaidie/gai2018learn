package gai.mooc.controller.backend;

import gai.mooc.bean.pojo.Category;
import gai.mooc.bean.pojo.User;
import gai.mooc.common.Constants;
import gai.mooc.common.ResponseCode;
import gai.mooc.common.ServerResponse;
import gai.mooc.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */
@Controller
@RequestMapping("/backend/category")
public class CategoryManagerController {


    @Autowired
    ICategoryService categoryService;


    @RequestMapping(value = "/getCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Category> getCategory(HttpSession session,
                                                @RequestParam(value = "categoryId", defaultValue = "0")
                                                        Integer categoryId){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return categoryService.getCategory(categoryId);
    }

    @RequestMapping(value = "/getChildCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getChildCategory(HttpSession session,
                                                           @RequestParam(value = "categoryId", defaultValue = "0")
                                                        Integer categoryId){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return categoryService.getChildCategory(categoryId);
    }

    @RequestMapping(value = "/getAllChildCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getAllChildCategory(HttpSession session,
                                                     @RequestParam(value = "categoryId", defaultValue = "0")
                                                             Integer categoryId){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return categoryService.getAllChildCategory(categoryId);
    }


    @RequestMapping(value = "/addCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Category> addCatetory(HttpSession session,
                                                @RequestParam(value = "parentId", defaultValue = "0")
                                                        Integer parentId,String categoryName){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录后才能进行操作");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return categoryService.addCategory(parentId, categoryName);
    }

    @RequestMapping(value = "/editCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Category> editCatetory(HttpSession session,
                                                        Integer categoryId,String categoryName){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录后才能进行操作");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return categoryService.editCategory(categoryId, categoryName);
    }



}
