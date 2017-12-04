package com.mobile.tiandy.asset.common.vo;

/**
 * Created by 78326 on 2017.9.9.
 */
public class User {
    private Long id;//序号
    private String jobId;//工号
    private String name;//姓名
    private String password;//密码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
