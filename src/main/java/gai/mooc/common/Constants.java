package gai.mooc.common;

/**
 * Created by Administrator on 2018/1/15.
 */
public class Constants {

    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String EMAIL_TYPE = "email";
    public static final String USERNAME_TYPE = "userName";

    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public interface Cart {
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMIT_NUM_SUCCESS = "limit_num_success";
        String LIMIT_NUM_FAIL = "limit_num_success";
    }

}
