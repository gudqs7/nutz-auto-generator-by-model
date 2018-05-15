package cn.oa.cyb.base.pojo;

/**
 * @author wq
 * @date 2018/5/10
 */
public class SortVo {

    public final static String DESC = "desc";

    private String field;
    private String direction;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
