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

}
