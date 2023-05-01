package cn.mybatis.session;

public interface SqlSession {

    /**
     * 根据指定的SqlID获取一条记录的封装对象
     * @param statement     封装之后的对象类型
     * @param <T>           sqlID
     * @return              封装之后的对象
     */
    <T> T selectOne(String statement);

    /**
     * 根据SqlID获取一条记录的封装记录，只不过这个方法允许为sql传递一些参数
     * 在实际使用时，这个参数传递的一般时pojo、Map成ImmutableMap
     * @param statement
     * @param parameter
     * @param <T>
     * @return
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 得到映射器，巧妙地使用了泛型，使类型安全
     * @param <T>
     * @param type
     * @return
     */
    <T> T getMapper(Class<T> type);

}
