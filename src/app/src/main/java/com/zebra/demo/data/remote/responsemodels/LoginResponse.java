package com.zebra.demo.data.remote.responsemodels;

public class LoginResponse {
    private boolean Result;
    private String ErrorCode;
    private String Message;
    private Data Data;

    // Getters and Setters
    public boolean isResult() { return Result; }
    public void setResult(boolean result) { this.Result = result; }

    public String getErrorCode() { return ErrorCode; }
    public void setErrorCode(String errorCode) { this.ErrorCode = errorCode; }

    public String getMessage() { return Message; }
    public void setMessage(String message) { this.Message = message; }

    public Data getData() { return Data; }
    public void setData(Data data) { this.Data = data; }

    // Inner class: Data
    public static class Data {
        private UserDetails UserDetails;

        public UserDetails getUserDetails() { return UserDetails; }
        public void setUserDetails(UserDetails userDetails) { this.UserDetails = userDetails; }
    }

    // Inner class: UserDetails
    public static class UserDetails {
        private int Userid;
        private String Usercode;
        private String Username;
        private String Loginid;
        private String Password;
        private String Emailid;
        private String Mobileno;
        private String Gender;
        private String Designation;
        private int Status;
        private String Statusname;
        private String Createdby;
        private String Createdon;
        private String Createddate;
        private String Modifiedby;
        private String Modifiedon;
        private String Modifieddate;

        // Getters and Setters
        public int getUserid() { return Userid; }
        public void setUserid(int userid) { this.Userid = userid; }

        public String getUsercode() { return Usercode; }
        public void setUsercode(String usercode) { this.Usercode = usercode; }

        public String getUsername() { return Username; }
        public void setUsername(String username) { this.Username = username; }

        public String getLoginid() { return Loginid; }
        public void setLoginid(String loginid) { this.Loginid = loginid; }

        public String getPassword() { return Password; }
        public void setPassword(String password) { this.Password = password; }

        public String getEmailid() { return Emailid; }
        public void setEmailid(String emailid) { this.Emailid = emailid; }

        public String getMobileno() { return Mobileno; }
        public void setMobileno(String mobileno) { this.Mobileno = mobileno; }

        public String getGender() { return Gender; }
        public void setGender(String gender) { this.Gender = gender; }

        public String getDesignation() { return Designation; }
        public void setDesignation(String designation) { this.Designation = designation; }

        public int getStatus() { return Status; }
        public void setStatus(int status) { this.Status = status; }

        public String getStatusname() { return Statusname; }
        public void setStatusname(String statusname) { this.Statusname = statusname; }

        public String getCreatedby() { return Createdby; }
        public void setCreatedby(String createdby) { this.Createdby = createdby; }

        public String getCreatedon() { return Createdon; }
        public void setCreatedon(String createdon) { this.Createdon = createdon; }

        public String getCreateddate() { return Createddate; }
        public void setCreateddate(String createddate) { this.Createddate = createddate; }

        public String getModifiedby() { return Modifiedby; }
        public void setModifiedby(String modifiedby) { this.Modifiedby = modifiedby; }

        public String getModifiedon() { return Modifiedon; }
        public void setModifiedon(String modifiedon) { this.Modifiedon = modifiedon; }

        public String getModifieddate() { return Modifieddate; }
        public void setModifieddate(String modifieddate) { this.Modifieddate = modifieddate; }
    }
}

