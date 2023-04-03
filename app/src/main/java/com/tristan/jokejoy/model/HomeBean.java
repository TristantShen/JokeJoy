package com.tristan.jokejoy.model;

/**
 * @author : create by  szh
 * @date : 2023/3/31 17:06
 * @desc :
 */
public class HomeBean {

    private JokeBean joke;
    private InfoBean info;
    private UserBean user;

    public JokeBean getJoke() {
        return joke;
    }

    public void setJoke(JokeBean joke) {
        this.joke = joke;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class JokeBean {
        private int jokesId;
        private String addTime;
        private String content;
        private int userId;
        private int type;
        private String imageUrl;
        private boolean hot;
        private String latitudeLongitude;
        private String showAddress;
        private String thumbUrl;
        private String videoUrl;
        private int videoTime;
        private String videoSize;
        private String imageSize;
        private String audit_msg;

        public int getJokesId() {
            return jokesId;
        }

        public void setJokesId(int jokesId) {
            this.jokesId = jokesId;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public String getLatitudeLongitude() {
            return latitudeLongitude;
        }

        public void setLatitudeLongitude(String latitudeLongitude) {
            this.latitudeLongitude = latitudeLongitude;
        }

        public String getShowAddress() {
            return showAddress;
        }

        public void setShowAddress(String showAddress) {
            this.showAddress = showAddress;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(int videoTime) {
            this.videoTime = videoTime;
        }

        public String getVideoSize() {
            return videoSize;
        }

        public void setVideoSize(String videoSize) {
            this.videoSize = videoSize;
        }

        public String getImageSize() {
            return imageSize;
        }

        public void setImageSize(String imageSize) {
            this.imageSize = imageSize;
        }

        public String getAudit_msg() {
            return audit_msg;
        }

        public void setAudit_msg(String audit_msg) {
            this.audit_msg = audit_msg;
        }
    }

    public static class InfoBean {
        private int likeNum;
        private int shareNum;
        private int commentNum;
        private int disLikeNum;
        private boolean isLike;
        private boolean isUnlike;
        private boolean isAttention;

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getShareNum() {
            return shareNum;
        }

        public void setShareNum(int shareNum) {
            this.shareNum = shareNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getDisLikeNum() {
            return disLikeNum;
        }

        public void setDisLikeNum(int disLikeNum) {
            this.disLikeNum = disLikeNum;
        }

        public boolean isIsLike() {
            return isLike;
        }

        public void setIsLike(boolean isLike) {
            this.isLike = isLike;
        }

        public boolean isIsUnlike() {
            return isUnlike;
        }

        public void setIsUnlike(boolean isUnlike) {
            this.isUnlike = isUnlike;
        }

        public boolean isIsAttention() {
            return isAttention;
        }

        public void setIsAttention(boolean isAttention) {
            this.isAttention = isAttention;
        }
    }

    public static class UserBean {
        private int userId;
        private String nickName;
        private String signature;
        private String avatar;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
