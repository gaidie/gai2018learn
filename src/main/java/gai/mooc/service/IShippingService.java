package gai.mooc.service;

import com.github.pagehelper.PageInfo;
import gai.mooc.bean.pojo.Shipping;
import gai.mooc.common.ServerResponse;

/**
 * Created by Administrator on 2018/1/29.
 */
public interface IShippingService {

    ServerResponse add(Integer currentUserId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer currentUserId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);

}
