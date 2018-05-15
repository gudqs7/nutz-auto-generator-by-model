package cn.oa.cyb.base;

import cn.oa.cyb.base.pojo.Page;
import cn.oa.cyb.base.pojo.ParamVo;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.sql.Criteria;

import java.util.List;

/**
 * @author wq
 * @date 2018/5/9
 */
public class BaseServiceImpl<T> implements IBaseService<T> {

    protected IBaseDao<T> baseDao;

    @Override
    public T add(T model) throws Exception {
        return baseDao.add(model);
    }

    @Override
    public int update(T model) throws Exception {
        return baseDao.update(model);
    }

    @Override
    public int delete(Integer id) throws Exception {
        return baseDao.delete(Long.valueOf(id));
    }

    @Override
    public int delete(Object[] ids) throws Exception {
        return baseDao.delete(ids);
    }

    @Override
    public T findById(Integer id) throws Exception {
        return baseDao.findById(Long.valueOf(id));
    }

    @Override
    public List<T> findAll() throws Exception {
        return baseDao.findAll(Cnd.cri());
    }

    @Override
    public List<T> findAll(Condition condition) throws Exception {
        return baseDao.findAll(condition);
    }

    @Override
    public Page<T> findAll(Page<T> page) throws Exception {
        return findAll(page, Cnd.cri());
    }

    @Override
    public Page<T> findAll(Page<T> page, Condition condition) throws Exception {
        return baseDao.findAll(page, condition);
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, Page<T> page) throws Exception {
        return findAll(querySqlId, countSqlId, page, Cnd.cri());
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, Page<T> page, Condition condition) throws Exception {
        return baseDao.findAll(querySqlId, countSqlId, page, condition);
    }

    @Override
    public Page<T> findAll(ParamVo<T> paramVo) {
        return findAll(paramVo, Cnd.cri());
    }

    @Override
    public Page<T> findAll(ParamVo<T> paramVo, Criteria condition) {
        return baseDao.findAll(paramVo, condition);
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, ParamVo<T> paramVo) {
        return findAll(querySqlId, countSqlId, paramVo, Cnd.cri());
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, ParamVo<T> paramVo, Criteria condition) {
        return baseDao.findAll(querySqlId, countSqlId, paramVo, condition);
    }

    @Override
    public List<T> findAll(String querySqlId, Condition condition) throws Exception {
        return baseDao.findAll(querySqlId, condition);
    }

    @Override
    public List<T> findAll(String querySqlId) throws Exception {
        return findAll(querySqlId, Cnd.cri());
    }
}
