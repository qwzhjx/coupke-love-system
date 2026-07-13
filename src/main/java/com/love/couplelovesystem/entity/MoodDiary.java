package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("t_mood_diary")
public class MoodDiary {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer type;
    private Integer mood;
    private Integer scene;
    private Integer resolveTime;
    private String note;
    private Integer isPrivate;
    private Date createTime;

    private transient String moodText;
    private transient String sceneText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public Integer getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Integer resolveTime) {
        this.resolveTime = resolveTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMoodText() {
        return moodText;
    }

    public void setMoodText(String moodText) {
        this.moodText = moodText;
    }

    public String getSceneText() {
        return sceneText;
    }

    public void setSceneText(String sceneText) {
        this.sceneText = sceneText;
    }
}
