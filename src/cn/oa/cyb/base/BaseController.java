package cn.oa.cyb.base;

import cn.oa.cyb.base.pojo.MapBean;
import cn.oa.cyb.base.pojo.Page;
import cn.oa.cyb.base.pojo.ParamVo;

/**
 * @author wq
 * @date 2018/5/9
 */
public class BaseController {

    protected Object success(String msg) {
        return success(null, msg);
    }

    protected Object success(Object data) {
        return success(data, "操作成功!");
    }

    protected Object success(Object data, ParamVo paramVo) {
        MapBean mb = new MapBean();
        mb.put("code", 1);
        mb.put("message", "操作成功!");
        if (data == null) {
            mb.put("data", "");
        } else if (data instanceof Page) {
            mb.put("page", data);
            mb.put("param", paramVo);
        } else {
            mb.put("data", data);
        }
        return mb;
    }

    protected Object success(Object data, String msg) {
        MapBean mb = new MapBean();
        mb.put("code", 1);
        mb.put("message", msg);
        if (data == null) {
            mb.put("data", "");
        } else if (data instanceof Page) {
            mb.put("page", data);
        } else {
            mb.put("data", data);
        }
        return mb;
    }

    protected Object success() {
        return success("操作成功");
    }

    protected Object error() {
        return error("操作失败!");
    }

    protected Object error(String msg) {
        MapBean mb = new MapBean();
        mb.put("code", 0);
        mb.put("message", msg);
        return mb;
    }


}
