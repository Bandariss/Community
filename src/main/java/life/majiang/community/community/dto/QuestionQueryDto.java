package life.majiang.community.community.dto;

public class QuestionQueryDto {
    private String search;
    private Integer page;
    private Integer size;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public QuestionQueryDto(String search, Integer page, Integer size) {
        this.search = search;
        this.page = page;
        this.size = size;
    }

    public QuestionQueryDto(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public QuestionQueryDto() {
    }
}
