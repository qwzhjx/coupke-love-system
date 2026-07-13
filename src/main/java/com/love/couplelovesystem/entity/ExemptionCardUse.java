package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("t_exemption_card_use")
public class ExemptionCardUse {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer holdId;
    private Date useTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHoldId() {
        return holdId;
    }

    public void setHoldId(Integer holdId) {
        this.holdId = holdId;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }
}
