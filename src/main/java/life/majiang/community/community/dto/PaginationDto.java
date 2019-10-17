package life.majiang.community.community.dto;

import java.util.ArrayList;
import java.util.List;

public class PaginationDto<T> {
    //封装页面包裹的元素
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer>pages=new ArrayList<>();
    private Integer totalPage;

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(boolean showFirstPage) {
        this.showFirstPage = showFirstPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page=page;
        pages.add(page);

        for(int i=1;i<=3;i++){
            //左右最多三个元素，因此只需要循环三次
            if(page-i>0){
                //在前面加
                pages.add(0,page-i);
            }
            if(page+i<=totalPage){
                //向后加
                pages.add(page+i);
            }
        }

        if(page==1){
            //第一页时不展示上一页按钮
            showPrevious=false;
        }
        else{
            showPrevious=true;
        }

        if(page==totalPage){
            //最后一页不展示后一页按钮
            showNext=false;
        }
        else{
            showNext=true;
        }

        if(pages.contains(1)){
            //是否展示第一页
            showFirstPage=false;
        }
        else{
            showFirstPage=true;
        }

        if(pages.contains(totalPage)){
            showEndPage=false;
        }
        else{
            showEndPage=true;
        }
    }
}
