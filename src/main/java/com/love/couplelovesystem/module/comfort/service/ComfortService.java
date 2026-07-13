package com.love.couplelovesystem.module.comfort.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.entity.ApologyLetter;
import com.love.couplelovesystem.entity.BedtimeStory;
import com.love.couplelovesystem.entity.ComfortWord;
import com.love.couplelovesystem.mybatis.mapper.ApologyLetterMapper;
import com.love.couplelovesystem.mybatis.mapper.BedtimeStoryMapper;
import com.love.couplelovesystem.mybatis.mapper.ComfortWordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 情绪急救服务
 */
@Service
public class ComfortService {

    @Resource
    private ComfortWordMapper comfortWordMapper;

    @Resource
    private ApologyLetterMapper apologyLetterMapper;

    @Resource
    private BedtimeStoryMapper bedtimeStoryMapper;

    // ========== 哄宠话术 ==========

    /**
     * 根据场景随机获取一条话术
     */
    public ComfortWord randomByCategory(Integer category) {
        QueryWrapper<ComfortWord> qw = new QueryWrapper<>();
        qw.eq("category", category).eq("status", 1);
        List<ComfortWord> list = comfortWordMapper.selectList(qw);
        if (list == null || list.isEmpty()) {
            return null;
        }
        ComfortWord word = list.get((int) (Math.random() * list.size()));
        // 更新展示次数
        word.setShowCount(word.getShowCount() + 1);
        comfortWordMapper.updateById(word);
        return word;
    }

    /**
     * 话术分页列表
     */
    public Page<ComfortWord> wordPage(Integer page, Integer size, Integer category) {
        QueryWrapper<ComfortWord> qw = new QueryWrapper<>();
        if (category != null) {
            qw.eq("category", category);
        }
        qw.orderByDesc("create_time");
        return comfortWordMapper.selectPage(new Page<>(page, size), qw);
    }

    /**
     * 新增话术
     */
    public void addWord(ComfortWord word) {
        comfortWordMapper.insert(word);
    }

    /**
     * 编辑话术
     */
    public void updateWord(ComfortWord word) {
        comfortWordMapper.updateById(word);
    }

    /**
     * 删除话术
     */
    public void deleteWord(Integer id) {
        comfortWordMapper.deleteById(id);
    }

    /**
     * 切换话术状态
     */
    public void toggleWordStatus(Integer id) {
        ComfortWord word = comfortWordMapper.selectById(id);
        if (word != null) {
            word.setStatus(word.getStatus() == 1 ? 0 : 1);
            comfortWordMapper.updateById(word);
        }
    }

    // ========== 认错保证书 ==========

    /**
     * 获取启用的保证书
     */
    public ApologyLetter getActiveLetter() {
        QueryWrapper<ApologyLetter> qw = new QueryWrapper<>();
        qw.eq("status", 1).orderByDesc("create_time").last("LIMIT 1");
        return apologyLetterMapper.selectOne(qw);
    }

    public Page<ApologyLetter> letterPage(Integer page, Integer size) {
        QueryWrapper<ApologyLetter> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return apologyLetterMapper.selectPage(new Page<>(page, size), qw);
    }

    public void saveLetter(ApologyLetter letter) {
        if (letter.getId() != null) {
            apologyLetterMapper.updateById(letter);
        } else {
            apologyLetterMapper.insert(letter);
        }
    }

    // ========== 睡前故事 ==========

    /**
     * 随机获取一篇故事
     */
    public BedtimeStory randomStory() {
        QueryWrapper<BedtimeStory> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        List<BedtimeStory> list = bedtimeStoryMapper.selectList(qw);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get((int) (Math.random() * list.size()));
    }

    public Page<BedtimeStory> storyPage(Integer page, Integer size) {
        QueryWrapper<BedtimeStory> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return bedtimeStoryMapper.selectPage(new Page<>(page, size), qw);
    }

    public BedtimeStory getStory(Integer id) {
        return bedtimeStoryMapper.selectById(id);
    }

    public void addStory(BedtimeStory story) {
        bedtimeStoryMapper.insert(story);
    }

    public void updateStory(BedtimeStory story) {
        // 部分更新：只更新非空字段，避免覆盖已有数据
        BedtimeStory existing = bedtimeStoryMapper.selectById(story.getId());
        if (existing == null) return;
        if (story.getTitle() != null) existing.setTitle(story.getTitle());
        if (story.getContent() != null) existing.setContent(story.getContent());
        if (story.getSource() != null) existing.setSource(story.getSource());
        if (story.getStatus() != null) existing.setStatus(story.getStatus());
        if (story.getVoiceUrl() != null) existing.setVoiceUrl(story.getVoiceUrl());
        bedtimeStoryMapper.updateById(existing);
    }

    public void deleteStory(Integer id) {
        bedtimeStoryMapper.deleteById(id);
    }
}
