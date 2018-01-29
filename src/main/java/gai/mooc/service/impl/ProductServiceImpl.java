package gai.mooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import gai.mooc.bean.pojo.Category;
import gai.mooc.bean.pojo.Product;
import gai.mooc.bean.vo.ProductDetailVo;
import gai.mooc.bean.vo.ProductListVo;
import gai.mooc.common.ServerResponse;
import gai.mooc.dao.CategoryMapper;
import gai.mooc.dao.ProductMapper;
import gai.mooc.service.IProductService;
import gai.mooc.util.DateTimeUtil;
import gai.mooc.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    public static final int DEFAULT_PARENT_CATEGORY_ID = 0;
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addProduct(Product product) {
        if (product != null){
            if (StringUtils.isNotEmpty(product.getSubImages())){
                String[] split = product.getSubImages().split(",");
                if (split.length > 0){
                    product.setMainImage(split[0]);
                }
            }
            if (product.getId() != 0){
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i>0){
                    return ServerResponse.createSuccess("产品更新成功。。。");
                }else {
                    return ServerResponse.createError("产品更新失败。。。");
                }
            }else {
                int i = productMapper.insertSelective(product);
                if (i >0){
                    return ServerResponse.createSuccess("产品新增成功");
                }else {
                    return ServerResponse.createError("产品更新失败");
                }
            }
        }else {
            return ServerResponse.createError("添加产品参数不能为空");
        }
    }


    /**
     * 根据产品ID查询产品详情
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> getProduct(Integer productId) {
        if (productId == null || productId == 0){
            return ServerResponse.createError("产品ID不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createError("未找到产品详情数据");
        }
        ProductDetailVo productDetailVo = fillProduct(product);
        return ServerResponse.createSuccess(productDetailVo);
    }

    private ProductDetailVo fillProduct(Product product){
        ProductDetailVo detail = new ProductDetailVo();
        detail.setId(product.getId());
        detail.setCategoryId(product.getCategoryId());
        detail.setName(product.getName());
        detail.setSubTitle(product.getSubtitle());
        detail.setMainImage(product.getMainImage());
        detail.setSubImage(product.getSubImages());
        detail.setDetail(product.getDetail());
        detail.setPrice(product.getPrice());
        detail.setStock(product.getStock());
        detail.setStatus(product.getStatus());
        detail.setCreateTime(DateTimeUtil.dateToString(product.getCreateTime()));
        detail.setUpdateTime(DateTimeUtil.dateToString(product.getUpdateTime()));
        detail.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            detail.setParentCategoryId(DEFAULT_PARENT_CATEGORY_ID);
        }else {
            detail.setParentCategoryId(category.getParentId());
        }
        return detail;
    }


    @Override
    public ServerResponse<String> updateSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            return ServerResponse.createError("产品参数和状态不能为空");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int i = productMapper.updateByPrimaryKeySelective(product);
        if (i>0){
            return ServerResponse.createSuccess("状态更新成功");
        }else {
            return ServerResponse.createError("状态更新失败");
        }
    }

    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectList();
        List<ProductListVo> productListVo = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductListVo item = fillProductList(products.get(i));
            productListVo.add(item);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVo);
        return ServerResponse.createSuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId,
                                                  Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%")
                    .append(productName)
                    .append("%")
                    .toString();
        }
        List<Product> products = productMapper.selectListByNameAndId(productName, productId);
        List<ProductListVo> productListVo = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductListVo item = fillProductList(products.get(i));
            productListVo.add(item);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVo);
        return ServerResponse.createSuccess(pageResult);
    }

    private ProductListVo fillProductList(Product product){
        ProductListVo list = new ProductListVo();
        list.setId(product.getId());
        list.setName(product.getName());
        list.setSubTitle(product.getSubtitle());
        list.setMainImage(product.getMainImage());
        list.setPrice(product.getPrice());
        list.setStatus(product.getStatus());
        list.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return list;
    }
}
