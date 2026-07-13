package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("t_message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private Integer role;
    private String content;
    private Date createTime;

    private transient String roleName;
    private transient String roleAvatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleAvatar() {
        return roleAvatar;
    }

    public void setRoleAvatar(String roleAvatar) {
        this.roleAvatar = roleAvatar;
    }
}
