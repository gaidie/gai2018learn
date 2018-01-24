package gai.mooc.dao;

import gai.mooc.bean.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    User selectLoginInfo(@Param("userName")String userName,@Param("password") String password);

    int checkEmail(String email);

    String selectQuestionByUserName(String userName);

    int checkQuestionAnswer(@Param("userName") String userName, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUserName(@Param("userName") String userName,@Param("passwordNew") String passwordNew);

    int checkPassword(@Param("passwordOld") String passwordOld, @Param("userId") Integer id);

    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer id);
}