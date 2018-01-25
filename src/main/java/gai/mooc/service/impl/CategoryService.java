package gai.mooc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import gai.mooc.bean.pojo.Category;
import gai.mooc.common.ServerResponse;
import gai.mooc.dao.CategoryMapper;
import gai.mooc.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/25.
 */
@Service("iCategoryService")
public class CategoryService implements ICategoryService {


    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse<Category> getCategory(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.createError("根据分类ID未找到对应的记录");
        }else {
         return ServerResponse.createSuccess(category);
        }
    }

    @Override
    public ServerResponse<Category> addCategory(Integer parentId, String categoryName) {
        if (StringUtils.isEmpty(categoryName)){
            return ServerResponse.createError("分类名称不能为空");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        int i = categoryMapper.insertSelective(category);
        if (i >0){
            return ServerResponse.createSuccess("分类数据插入成功");
        }else {
            return ServerResponse.createError("分类数据保存失败");
        }
    }

    @Override
    public ServerResponse<Category> editCategory(Integer categoryId, String categoryName) {
        if (categoryId == 0 || StringUtils.isEmpty(categoryName)){
            return ServerResponse.createError("请检查参数是否正确");
        }
        Category category =new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        if (i > 0){
            return ServerResponse.createSuccess("修改成功");
        }else {
            return ServerResponse.createError("分类修改失败。。。");
        }
    }

    @Override
    public ServerResponse<List<Category>> getChildCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectByParentKey(categoryId);
        if (categoryList.size() == 0){
            return ServerResponse.createError("根据分类ID未找到子分类");
        }else {
            return ServerResponse.createSuccess(categoryList);
        }
    }

    @Override
    public ServerResponse<List<Category>> getAllChildCategory(Integer categoryId) {
        if (categoryId == null){
            return ServerResponse.createError("参数不能为空");
        }
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Category> list = Lists.newArrayList();
        for (Category category : categorySet) {
            list.add(category);
        }
        if (CollectionUtils.isEmpty(list)){
            return ServerResponse.createError("根据分类ID未查询到子类信息");
        }
        return ServerResponse.createSuccess(list);
    }

    /**
     * 获取所有子节点信息
     * @param categorySet
     * @param categoryId
     */
    private void findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        //查找子节点
        List<Category> categories = categoryMapper.selectByParentKey(categoryId);
        for (Category item : categories) {
            findChildCategory(categorySet, item.getId());
        }

    }
}
