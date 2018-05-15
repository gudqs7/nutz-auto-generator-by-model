package cn.oa.cyb.base;

import cn.oa.cyb.base.pojo.*;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wq
 * @date 2018/5/9
 */
public class BaseDaoImpl<T> implements IBaseDao<T> {

    @Inject
    protected Dao dao;

    private Class<T> getTClass() {
        Type t = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) t).getActualTypeArguments();
        return (Class<T>) params[0];
    }

    @Override
    public T add(T model) throws Exception {
        return dao.insert(model);
    }

    @Override
    public int update(T model) throws Exception {
        return dao.update(model);
    }

    @Override
    public int delete(Long id) throws Exception {
        return dao.delete(getTClass(), id);
    }

    @Override
    public int delete(Object[] ids) throws Exception {
        int sum = 0;
        for (Object id : ids) {
            sum += dao.delete(getTClass(), (Long) id);
        }
        return sum;
    }

    @Override
    public T findById(Long id) throws Exception {
        return dao.fetch(getTClass(), id);
    }

    @Override
    public List<T> findAll(Condition condition) throws Exception {
        return dao.query(getTClass(), condition);
    }

    @Override
    public Page<T> findAll(Page<T> page, Condition condition) throws Exception {
        Pager pager = dao.createPager(page.getPageNo(), page.getPageSize());
        List<T> list = dao.query(getTClass(), condition, pager);
        page.setResult(list);
        if (page.isAutoCount()) {
            page.setTotalItems(dao.count(getTClass(), condition));
        }
        return page;
    }

    @Override
    public List<T> findAll(String querySqlId, Condition condition) throws Exception {
        Sql sql = Sqls.queryEntity(dao.sqls().get(querySqlId));
        sql.setCondition(condition);
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        return sql.getList(getTClass());
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, Page<T> page, Condition condition) throws Exception {
        Sql sql = Sqls.queryEntity(dao.sqls().get(querySqlId));
        sql.setCondition(condition);
        sql.setPager(dao.createPager(page.getPageNo(), page.getPageSize()));
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<T> list = sql.getList(getTClass());
        page.setResult(list);
        addTotalCount(countSqlId, condition, page);
        return page;
    }

    @Override
    public Page<T> findAll(ParamVo<T> paramVo, Criteria condition) {
        Pager pager = dao.createPager(paramVo.getPageNo(), paramVo.getPageSize());
        Page<T> page = paramVo.getPageVo();
        setCondition(paramVo, condition);
        List<T> list = dao.query(getTClass(), condition, pager);
        page.setResult(list);
        if (page.isAutoCount()) {
            page.setTotalItems(dao.count(getTClass(), condition));
        }
        return page;
    }

    @Override
    public Page<T> findAll(String querySqlId, String countSqlId, ParamVo<T> paramVo, Criteria condition) {
        Page<T> page = paramVo.getPageVo();
        setCondition(paramVo, condition);
        Sql sql = Sqls.queryEntity(dao.sqls().get(querySqlId));
        sql.setCondition(condition);
        sql.setPager(dao.createPager(page.getPageNo(), page.getPageSize()));
        sql.setCallback(Sqls.callback.records());
        dao.execute(sql);
        List<T> list = sql.getList(getTClass());
        page.setResult(list);
        addTotalCount(countSqlId, condition, page);
        return page;
    }


    private void setCondition(ParamVo<T> paramVo, Criteria condition) {
        if (paramVo.getSort() != null && paramVo.getSort().size() > 0) {
            for (SortVo sortVo : paramVo.getSort()) {
                if (Strings.isEmpty(sortVo.getDirection()) || Strings.isEmpty(sortVo.getField())) {
                    continue;
                }
                if (SortVo.DESC.equalsIgnoreCase(sortVo.getDirection())) {
                    condition.getOrderBy().desc(sortVo.getField());
                } else {
                    condition.getOrderBy().asc(sortVo.getField());
                }
            }
        }
        if (paramVo.getFilter() != null && paramVo.getFilter().size() > 0) {
            for (FilterVo filterVo : paramVo.getFilter()) {
                if (filterVo == null ||
                        filterVo.getRight() == 0 ||
                        Strings.isEmpty(filterVo.getField()) ||
                        Strings.isEmpty(filterVo.getOperator()) ||
                        Strings.isEmpty(filterVo.getValue0().toString())) {
                    continue;
                }
                condition.where().and(filterVo.getField(), filterVo.getOperator(), filterVo.getValue0());
            }
        }
    }

    private void addTotalCount(String countSqlId, Condition condition, Page<T> page) {
        Sql sql;
        if (page.isAutoCount()) {
            sql = Sqls.queryEntity(dao.sqls().get(countSqlId));
            sql.setCondition(condition);
            sql.setCallback(Sqls.callback.longValue());
            dao.execute(sql);
            page.setTotalItems(sql.getObject(Long.class));
        }
    }

}
