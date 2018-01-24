package gai.mooc.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * Created by ga on 2018/1/23.
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现 调用get   取值的时候 如果key没有对应的值, 就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String vlaue){
        localCache.put(key, vlaue);
    }

    public static String getValue(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error("[获取缓存的出现异常]key："+key, e);
        }
        return value;
    }
}
