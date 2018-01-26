package gai.mooc.controller.backend;

import com.github.pagehelper.PageInfo;
import gai.mooc.bean.pojo.Product;
import gai.mooc.bean.pojo.User;
import gai.mooc.bean.vo.ProductDetail;
import gai.mooc.common.Constants;
import gai.mooc.common.ResponseCode;
import gai.mooc.common.ServerResponse;
import gai.mooc.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/1/26.
 */
@Controller
@RequestMapping("/manager/product")
public class ProductManagerController {

    @Autowired
    IProductService productService;

    @RequestMapping(value = "/updateProduct.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateProduct(HttpSession session, Product product){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return productService.addProduct(product);
    }


    @RequestMapping(value = "/getProduct.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetail> getProduct(HttpSession session, Integer productId){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return productService.getProduct(productId);
    }

    @RequestMapping(value = "/getList.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session, Integer pageNum, Integer pageSize){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "/searchProduct.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> searchProduct(HttpSession session,String productName,
                                                  Integer productId,Integer pageNum,
                                                  Integer pageSize){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "/updateSaleStatus.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateSaleStatus(HttpSession session, Integer productId,Integer status){
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            return ServerResponse.createError("只有管理员才能操作，您无权限");
        }
        return productService.updateSaleStatus(productId, status);
    }

}
