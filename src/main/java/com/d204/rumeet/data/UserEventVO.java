package com.d204.rumeet.data;

public class UserEventVO {
    public UserEventVO(String timesstamp, String userAgent, String colorName, String userName) {
        this.timesstamp = timesstamp;
        this.userAgent = userAgent;
        this.colorName = colorName;
        this.userName = userName;
    }

    private String timesstamp;
    private String userAgent;
    private String colorName;
    private String userName;

}
