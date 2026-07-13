package com.love.couplelovesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

@TableName("t_chemistry_question")
public class ChemistryQuestion {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String question;
    private String opts;
    private Integer answer;
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getOpts() { return opts; }
    public void setOpts(String opts) { this.opts = opts; }
    public Integer getAnswer() { return answer; }
    public void setAnswer(Integer answer) { this.answer = answer; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
