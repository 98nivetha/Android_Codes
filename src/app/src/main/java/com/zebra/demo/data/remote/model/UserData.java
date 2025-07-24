package com.zebra.demo.data.remote.model;

public class UserData {
    UserDetails UserDetails;
    /*ArrayList<Object> SideMenuItems = new ArrayList<Object>();
    ArrayList<Object> UserwiseAction = new ArrayList<Object>();
    ArrayList<Object> ScreenListdata = new ArrayList<Object>();
    ArrayList<Object> D_RoleData = new ArrayList<Object>();*/
    private int notificationCount;
    private int notificationAuditCount;
    private String Token;


    // Getter Methods

    public UserDetails getUserDetails() {
        return UserDetails;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public int getNotificationAuditCount() {
        return notificationAuditCount;
    }

    public String getToken() {
        return Token;
    }

    // Setter Methods

    public void setUserDetails( UserDetails UserDetails ) {
        this.UserDetails = UserDetails;
    }

    public void setNotificationCount( int notificationCount ) {
        this.notificationCount = notificationCount;
    }

    public void setNotificationAuditCount( int notificationAuditCount ) {
        this.notificationAuditCount = notificationAuditCount;
    }

    public void setToken( String Token ) {
        this.Token = Token;
    }
}
