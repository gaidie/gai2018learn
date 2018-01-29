package gai.mooc.service;

import gai.mooc.bean.vo.CartVo;
import gai.mooc.common.ServerResponse;

/**
 * Created by Administrator on 2018/1/29.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer id, Integer count, Integer productId);

    ServerResponse<CartVo> delete(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, int checked);
}
