package gai.mooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import gai.mooc.bean.pojo.Shipping;
import gai.mooc.common.ServerResponse;
import gai.mooc.dao.ShippingMapper;
import gai.mooc.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/29.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer currentUserId, Shipping shipping) {
        if (shipping == null){
            return ServerResponse.createError("产品参数为空");
        }
        shipping.setUserId(currentUserId);
        int insert = shippingMapper.insert(shipping);
        if (insert > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());
            return ServerResponse.createSuccess("地址添加成功", result);
        }
        return ServerResponse.createError("地址添加失败");
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdAndUserId(userId, shippingId);
        if (resultCount > 0){
            return ServerResponse.createSuccess("地址删除成功");
        }
        return ServerResponse.createError("地址删除失败");
    }

    @Override
    public ServerResponse update(Integer currentUserId, Shipping shipping) {
        if (shipping == null){
            return ServerResponse.createError("产品参数为空");
        }
        shipping.setUserId(currentUserId);
        int insert = shippingMapper.updateByShipping(shipping);
        if (insert > 0){
            return ServerResponse.createSuccess("地址更新成功");
        }
        return ServerResponse.createError("地址更新失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createError("无法查询到该地址");
        }
        return ServerResponse.createSuccess(shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo =new PageInfo(shippingList);
        return ServerResponse.createSuccess(pageInfo);
    }

}
