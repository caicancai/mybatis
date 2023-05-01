package cn.mybatis.binding;

import cn.mybatis.session.SqlSession;
import cn.hutool.core.lang.ClassScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry {

    /**
     * 将已添加的映射器代理加入到 HashMap
     */
    private final Map<Class<?> , MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        /* Mapper 必须是接口才会注册 */
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        //使用ClassScanner扫描指定的包packageName下的所有类，得到返回的类集合mapperSet
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        //遍历mapperSet中的每个类mapperClass
        //调用addMapper方法添加每个mapperClass
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

}

