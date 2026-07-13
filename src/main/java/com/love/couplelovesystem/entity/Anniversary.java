package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("t_anniversary")
public class Anniversary {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Date anniversaryDate;
    private Integer repeatType;
    private String remindDays;
    private String icon;
    private Date createTime;

    private transient Long remainDays;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(Date anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public Integer getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Integer repeatType) {
        this.repeatType = repeatType;
    }

    public String getRemindDays() {
        return remindDays;
    }

    public void setRemindDays(String remindDays) {
        this.remindDays = remindDays;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(Long remainDays) {
        this.remainDays = remainDays;
    }
}
