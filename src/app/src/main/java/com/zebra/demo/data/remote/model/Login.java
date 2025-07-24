package com.zebra.demo.data.remote.model;

public class Login {
    private boolean Result;
    private String ErrorCode = null;
    private String Message;
    UserData Data;


    // Getter Methods

    public boolean getResult() {
        return Result;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public String getMessage() {
        return Message;
    }

    public UserData getData() {
        return Data;
    }

    // Setter Methods

    public void setResult( boolean Result ) {
        this.Result = Result;
    }

    public void setErrorCode( String ErrorCode ) {
        this.ErrorCode = ErrorCode;
    }

    public void setMessage( String Message ) {
        this.Message = Message;
    }

    public void setData( UserData Data ) {
        this.Data = Data;
    }
}
