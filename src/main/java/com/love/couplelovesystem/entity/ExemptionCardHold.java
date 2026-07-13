package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("t_exemption_card_hold")
public class ExemptionCardHold {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cardDefId;
    private Integer quantity;
    private Integer usedCount;
    private Date expireTime;
    private Date createTime;

    private transient String cardName;
    private transient String cardDescription;
    private transient Integer cardStyle;
    private transient Integer remainCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCardDefId() {
        return cardDefId;
    }

    public void setCardDefId(Integer cardDefId) {
        this.cardDefId = cardDefId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public Integer getCardStyle() {
        return cardStyle;
    }

    public void setCardStyle(Integer cardStyle) {
        this.cardStyle = cardStyle;
    }

    public Integer getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(Integer remainCount) {
        this.remainCount = remainCount;
    }
}
