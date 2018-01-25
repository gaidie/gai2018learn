package gai.mooc.service;

import gai.mooc.bean.pojo.Category;
import gai.mooc.common.ServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */
public interface ICategoryService {

    ServerResponse<Category> getCategory(Integer categoryId);

    ServerResponse<Category> addCategory(Integer parentId, String categoryName);

    ServerResponse<Category> editCategory(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildCategory(Integer categoryId);

    ServerResponse<List<Category>> getAllChildCategory(Integer categoryId);
}
