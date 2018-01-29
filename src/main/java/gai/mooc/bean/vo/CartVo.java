package gai.mooc.bean.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */
public class CartVo {

    private List<CartProductVo> cartProductVoVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    //是否已经都勾选
    private String imageHost;

    public List<CartProductVo> getCartProductVoVoList() {
        return cartProductVoVoList;
    }

    public void setCartProductVoVoList(List<CartProductVo> cartProductVoVoList) {
        this.cartProductVoVoList = cartProductVoVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
