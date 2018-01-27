package gai.mooc.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import gai.mooc.bean.pojo.Product;
import gai.mooc.bean.pojo.User;
import gai.mooc.bean.vo.ProductDetail;
import gai.mooc.common.Constants;
import gai.mooc.common.ResponseCode;
import gai.mooc.common.ServerResponse;
import gai.mooc.service.IFileService;
import gai.mooc.service.IProductService;
import gai.mooc.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/26.
 */
@Controller
@RequestMapping("/backend/product")
public class ProductManagerController {

    @Autowired
    IProductService productService;

    @Autowired
    IFileService fileService;

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

    @RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
//        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
//        if (currentUser == null){
//            return ServerResponse.createError(ResponseCode.NEED_LOGIN.getCode(),"需要登录在使用");
//        }
//        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
//            return ServerResponse.createError("只有管理员才能操作，您无权限");
//        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String uploadName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+uploadName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri",uploadName);
        fileMap.put("url",url);
        return ServerResponse.createSuccess(fileMap);

    }

    @RequestMapping("richtextImgUpload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User currentUser = (User) session.getAttribute(Constants.CURRENT_USER);
        if (currentUser == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if (currentUser.getRole() != Constants.Role.ROLE_ADMIN){
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }


}
