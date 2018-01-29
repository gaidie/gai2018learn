package gai.mooc.controller;

import gai.mooc.bean.pojo.User;
import gai.mooc.bean.vo.CartVo;
import gai.mooc.common.Constants;
import gai.mooc.common.ResponseCode;
import gai.mooc.common.ServerResponse;
import gai.mooc.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/1/29.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;


    @RequestMapping("/add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        return cartService.add(user.getId(), count, productId);
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        return cartService.update(user.getId(), count, productId);
    }


    @RequestMapping("/delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        return cartService.delete(user.getId(), productIds);
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user = (User) session.getAttribute(Constants.CURRENT_USER);
        if (user == null){
            return ServerResponse.createError("用户未登录。。。");
        }
        return cartService.list(user.getId());
    }

    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping("selectAll.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), null, Constants.Cart.CHECKED);
    }

    /**
     * 全反选
     * @param session
     * @return
     */
    @RequestMapping("unSelectAll.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), null, Constants.Cart.UN_CHECKED);
    }

    /**
     * 单独选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session, Integer productId) {
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), productId, Constants.Cart.CHECKED);
    }

    /**
     * 单独反选
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession session, Integer productId) {
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(currentUser.getId(), productId, Constants.Cart.UN_CHECKED);
    }



}
