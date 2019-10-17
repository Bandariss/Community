package life.majiang.community.community.dto;

import life.majiang.community.community.model.User;

public class CommentDto {

        private Long id;
        private Long parentId;
        private Integer type;
        private Long commentor;
        private Long gmtCreate;
        private Long gmtModified;
        private Long likeCount;
        private String content;
        private User user;
        private Integer commentCount;

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
            return id;
        }


        public void setId(Long id) {
            this.id = id;
        }


        public Long getParentId() {
            return parentId;
        }


        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }


        public Integer getType() {
            return type;
        }


        public void setType(Integer type) {
            this.type = type;
        }


        public Long getCommentor() {
            return commentor;
        }


        public void setCommentor(Long commentor) {
            this.commentor = commentor;
        }

        public Long getGmtCreate() {
            return gmtCreate;
        }


        public void setGmtCreate(Long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }


        public Long getGmtModified() {
            return gmtModified;
        }


        public void setGmtModified(Long gmtModified) {
            this.gmtModified = gmtModified;
        }


        public Long getLikeCount() {
            return likeCount;
        }


        public void setLikeCount(Long likeCount) {
            this.likeCount = likeCount;
        }


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content == null ? null : content.trim();
        }
    }

