package gai.mooc.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import gai.mooc.bean.pojo.Cart;
import gai.mooc.bean.pojo.Product;
import gai.mooc.bean.vo.CartProductVo;
import gai.mooc.bean.vo.CartVo;
import gai.mooc.common.Constants;
import gai.mooc.common.ResponseCode;
import gai.mooc.common.ServerResponse;
import gai.mooc.dao.CartMapper;
import gai.mooc.dao.ProductMapper;
import gai.mooc.service.ICartService;
import gai.mooc.util.BigDecimalUtil;
import gai.mooc.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == 0 || count == 0){
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENTS.getCode(), ResponseCode.ILLEGAL_ARGUMENTS.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Constants.Cart.CHECKED);
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartMapper.insert(cart);
        }else {
            cart.setQuantity(cart.getQuantity() + count);
        }
        return this.list(userId);
    }


    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = getCartVoLimit(userId);
        return ServerResponse.createSuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, int checked) {
        cartMapper.checkedOrUnCheckedProduct(userId, productId, checked);
        return this.list(userId);
    }



    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setProductTotalPrice(new BigDecimal("0"));
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    //判断库存
                    Integer buylimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //库存充足
                        cartProductVo.setLimitQuantity(Constants.Cart.LIMIT_NUM_SUCCESS);
                        buylimitCount = cartItem.getQuantity();
                    } else {
                        //库存不充足
                        buylimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Constants.Cart.LIMIT_NUM_FAIL);
                        Cart cartForQuatity = new Cart();
                        cartForQuatity.setId(cartItem.getId());
                        cartForQuatity.setQuantity(buylimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuatity);
                    }
                    cartProductVo.setQuantity(buylimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Constants.Cart.CHECKED) {
                    //如果已勾选，增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoVoList(cartProductVoList);
        cartVo.setAllChecked(getAllCheckdStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private Boolean getAllCheckdStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)){
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENTS.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENTS.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productList);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> update(Integer id, Integer count, Integer productId) {
        if (productId == 0 || count == null){
            return ServerResponse.createError(ResponseCode.ILLEGAL_ARGUMENTS.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENTS.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(id, productId);
        if (cart!= null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(id);
    }
}
