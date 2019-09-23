package life.majiang.community.community.dto;

import java.util.ArrayList;
import java.util.List;

public class PaginationDto {
    //封装页面包裹的元素
    private List<QuestionDto> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer>pages;


    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
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

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        Integer totalPage;
        if(totalCount % size==0){
            totalPage=totalCount/size;
        }
        else{
            totalPage=totalCount/size+1;
        }
        List pages=new ArrayList();
        pages.add(page);
        for(int i=1;i<=3;i++){
            //左右最多三个元素，因此只需要循环三次
            if(page-i>0){
                //在前面加
                pages.add(0,page-i);
            }
            if(page+i<=totalCount){
                //向后加
                pages.add(page+1);
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
