package com.zebra.demo.data.remote.model;

public class TidelLoginUserInfoResponse {

        private String user_id;
        private String email_id;
        private String mobile_no;
        private String user_name;
        private boolean is_admin;
        private boolean is_super_admin;
        private boolean can_raise_jr;
        private boolean can_forward_jr;
        private boolean can_approve_pass;
        private boolean can_forward_permit;
        private boolean can_issue_pass;
        private String dept_name;
        private String msg;

        public String getUser_id() {
                return user_id;
        }

        public void setUser_id(String user_id) {
                this.user_id = user_id;
        }

        public String getEmail_id() {
                return email_id;
        }

        public void setEmail_id(String email_id) {
                this.email_id = email_id;
        }

        public String getMobile_no() {
                return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
                this.mobile_no = mobile_no;
        }

        public String getUser_name() {
                return user_name;
        }

        public void setUser_name(String user_name) {
                this.user_name = user_name;
        }

        public boolean isIs_admin() {
                return is_admin;
        }

        public void setIs_admin(boolean is_admin) {
                this.is_admin = is_admin;
        }

        public boolean isIs_super_admin() {
                return is_super_admin;
        }

        public void setIs_super_admin(boolean is_super_admin) {
                this.is_super_admin = is_super_admin;
        }

        public boolean isCan_raise_jr() {
                return can_raise_jr;
        }

        public void setCan_raise_jr(boolean can_raise_jr) {
                this.can_raise_jr = can_raise_jr;
        }

        public boolean isCan_forward_jr() {
                return can_forward_jr;
        }

        public void setCan_forward_jr(boolean can_forward_jr) {
                this.can_forward_jr = can_forward_jr;
        }

        public boolean isCan_approve_pass() {
                return can_approve_pass;
        }

        public void setCan_approve_pass(boolean can_approve_pass) {
                this.can_approve_pass = can_approve_pass;
        }

        public boolean isCan_forward_permit() {
                return can_forward_permit;
        }

        public void setCan_forward_permit(boolean can_forward_permit) {
                this.can_forward_permit = can_forward_permit;
        }

        public boolean isCan_issue_pass() {
                return can_issue_pass;
        }

        public void setCan_issue_pass(boolean can_issue_pass) {
                this.can_issue_pass = can_issue_pass;
        }

        public String getDept_name() {
                return dept_name;
        }

        public void setDept_name(String dept_name) {
                this.dept_name = dept_name;
        }

        public String getMsg() {
                return msg;
        }

        public void setMsg(String msg) {
                this.msg = msg;
        }
}
