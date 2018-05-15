package cn.oa.cyb.base;

import cn.oa.cyb.base.pojo.Page;
import cn.oa.cyb.base.pojo.ParamVo;
import org.nutz.dao.Condition;
import org.nutz.dao.sql.Criteria;

import java.util.List;

/**
 * @author wq
 * @date 2018/5/9
 */
public interface IBaseDao<T> {

    /**
     * 统一添加
     *
     * @param model 添加对象
     * @return 带 id 的对象
     * @throws Exception 异常外抛
     */
    T add(T model) throws Exception;

    /**
     * 统一修改
     *
     * @param model 修改对象
     * @return 影响条数
     * @throws Exception 异常外抛
     */
    int update(T model) throws Exception;

    /**
     * 统一删除
     *
     * @param id 删除 id
     * @return 影响条数
     * @throws Exception 异常外抛
     */
    int delete(Long id) throws Exception;

    /**
     * 统一删除(多条)
     *
     * @param ids 删除 id 集合
     * @return 影响条数
     * @throws Exception 异常外抛
     */
    int delete(Object[] ids) throws Exception;

    /**
     * 查询 id 对应的对象
     *
     * @param id 对象 id
     * @return 单个对象
     * @throws Exception 异常外抛
     */
    T findById(Long id) throws Exception;

    /**
     * 查询所有(带条件)
     *
     * @param condition 查询条件
     * @return 多个记录
     * @throws Exception 异常外抛
     */
    List<T> findAll(Condition condition) throws Exception;

    /**
     * 查询(分页)
     *
     * @param page      分页参数
     * @param condition 查询条件
     * @return 分页结果
     * @throws Exception 异常外抛
     */
    Page<T> findAll(Page<T> page, Condition condition) throws Exception;

    /**
     * 查询(sql id 方式)
     *
     * @param querySqlId 查询 sql id
     * @param condition  查询条件
     * @return 查询结果
     * @throws Exception 异常外抛
     */
    List<T> findAll(String querySqlId, Condition condition) throws Exception;

    /**
     * 查询(分页 sql id 方式)
     *
     * @param querySqlId 查询 sql id
     * @param countSqlId 总数 sql id
     * @param condition  查询条件
     * @param page       分页参数
     * @return 分页结果
     * @throws Exception 异常外抛
     */
    Page<T> findAll(String querySqlId, String countSqlId, Page<T> page, Condition condition) throws Exception;

    /**
     * 查询 paramVo 方式
     *
     * @param paramVo   分页 及 条件对象
     * @param condition 自定义条件
     * @return 查询结果
     */
    Page<T> findAll(ParamVo<T> paramVo, Criteria condition);

    /**
     * 查询 paramVo sql id 方式
     *
     * @param querySqlId 查询 sql id
     * @param countSqlId 总数 sql id
     * @param paramVo    分页及条件对象
     * @return 查询结果
     */
    Page<T> findAll(String querySqlId, String countSqlId, ParamVo<T> paramVo, Criteria condition);


}
