package rara.board;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@Getter
public class Paging {
    private final int firstPage = 1;
    private int currentPage;
    private int limit;
    private int totalPage;
    private Integer previous;
    private Integer next;
    private Long totalCount;
    private final int perList = 5; //페이지 목록에서 보여줄 최대 페이지
    private final List pageList = new ArrayList();

    public Paging() {
        this.currentPage = 1;
        this.limit = 10;
    }

    public Paging(int currentPage) {
        this.currentPage = currentPage;
        this.limit = 10;
    }

    public Paging(int currentPage, int limit) {
        this.currentPage = currentPage;
        this.limit = limit;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTotalPage() {
        Long LongTotalPage = (totalCount / limit);
        if (totalCount % limit > 0L) {
            LongTotalPage++;
        }
        this.totalPage = LongTotalPage.intValue();

        setPageList();
    }

    public int getOffset() {
        return (this.currentPage - 1) * this.limit;
    }

    private void setPageList() {
        // 9 -> 2page 4 / 6,7,8,9,10   13 -> 3page 3 11,12,13,14,15

        int firstElement = 0;
        int endElement = 0;

        if (totalCount == 0) {
            return;
        }

        if ((currentPage % perList) == 0) {
            firstElement = currentPage - perList + 1;
            endElement = currentPage;
        } else  {
            if ((currentPage % perList) == 1) {
                firstElement = currentPage;
            } else {
                firstElement = (currentPage / perList) * perList + 1;
            }
            endElement = (firstElement + perList - 1) > totalPage ? totalPage : (firstElement + perList - 1);
        }

        for (int i = firstElement; i <= endElement; i++) {
            pageList.add(i);
        }

        Collections.sort(pageList);

        if (pageList.get(0).equals(firstPage)) {
            previous = null;
        } else {
            previous = (Integer) pageList.get(0) - 1;
        }

        if (pageList.get(pageList.size() - 1).equals(totalPage)) {
            next = null;
        } else {
            next = (Integer) pageList.get(pageList.size() - 1) + 1;
        }
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
