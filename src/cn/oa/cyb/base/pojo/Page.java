package cn.oa.cyb.base.pojo;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    protected int pageNo = 1;
    protected int pageSize = -1;
    protected boolean autoCount = true;
    protected boolean search = false;
    protected String orderBy = null;
    protected String order = null;

    protected List<T> result = new ArrayList<T>();
    protected long totalItems = -1;

    public Page() {
    }

    public Page(int pageSize) {
        setPageSize(pageSize);
    }

    public Page(int pageNo, int pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    public Page(int pageSize, boolean autoCount) {
        setPageSize(pageSize);
        setAutoCount(autoCount);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAutoCount() {
        return autoCount;
    }

    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(final boolean search) {
        this.search = search;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(final String order) {
        String lowcaseOrder = Strings.lowerCase(order);

        String[] orders = Strings.splitIgnoreBlank(lowcaseOrder, ",");
        for (String orderStr : orders) {
            if (!Strings.equals(DESC, orderStr) && !Strings.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.order = lowcaseOrder;
    }

    public boolean isOrderBySetted() {
        return (!Strings.isBlank(orderBy) && !Strings.isBlank(order));
    }

    public int getOffset() {
        return ((pageNo - 1) * pageSize);
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(final List<T> result) {
        this.result = result;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final long totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isLastPage() {
        return pageNo == getTotalPages();
    }

    public boolean isHasNextPage() {
        return (pageNo + 1 <= getTotalPages());
    }

    public int getNextPage() {
        if (isHasNextPage()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    public boolean isFirstPage() {
        return pageNo == 1;
    }

    public boolean isHasPrePage() {
        return (pageNo - 1 >= 1);
    }

    public int getPrePage() {
        if (isHasPrePage()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }

    public long getTotalPages() {
        if (totalItems < 0) {
            return -1;
        }

        long count = totalItems / pageSize;
        if (totalItems % pageSize > 0) {
            count++;
        }
        return count;
    }

    public List<Long> getSlider(int count) {
        int halfSize = count / 2;
        long totalPage = getTotalPages();

        long startPageNumber = Math.max(pageNo - halfSize, 1);
        long endPageNumber = Math.min(startPageNumber + count - 1, totalPage);

        if (endPageNumber - startPageNumber < count) {
            startPageNumber = Math.max(endPageNumber - count, 1);
        }

        List<Long> result = new ArrayList<Long>();
        for (long i = startPageNumber; i <= endPageNumber; i++) {
            result.add(new Long(i));
        }
        return result;
    }
}
