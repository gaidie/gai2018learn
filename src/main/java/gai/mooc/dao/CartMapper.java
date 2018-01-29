package gai.mooc.dao;

import gai.mooc.bean.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    void deleteByUserIdAndProductIds(Integer userId, List<String> productList);

    void checkedOrUnCheckedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId,
                                   @Param("checked") Integer checked);
}