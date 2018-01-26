package gai.mooc.service;

import com.github.pagehelper.PageInfo;
import gai.mooc.bean.pojo.Product;
import gai.mooc.bean.vo.ProductDetail;
import gai.mooc.common.ServerResponse;

/**
 * Created by Administrator on 2018/1/26.
 */
public interface IProductService {

    ServerResponse<String> addProduct(Product product);

    ServerResponse<ProductDetail> getProduct(Integer productId);

    ServerResponse<String> updateSaleStatus(Integer productId, Integer status);

    ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId,
                                           Integer pageNum, Integer pageSize);

}
