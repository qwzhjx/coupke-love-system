package com.love.couplelovesystem.module.comfort.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.constant.CommonConstants;
import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.entity.ApologyLetter;
import com.love.couplelovesystem.entity.BedtimeStory;
import com.love.couplelovesystem.entity.ComfortWord;
import com.love.couplelovesystem.entity.MoodDiary;
import com.love.couplelovesystem.module.comfort.service.ComfortService;
import com.love.couplelovesystem.module.memory.service.MemoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 情绪急救-前端控制器
 */
@RestController
@RequestMapping("/api/comfort")
public class ComfortController {

    @Resource
    private ComfortService comfortService;
    @Resource
    private MemoryService memoryService;

    /** 消气进度（内存存储，简化实现） */
    private int angerProgress = 0;

    /**
     * 随机获取哄宠话术
     */
    @GetMapping("/word/random")
    public R randomWord(@RequestParam(defaultValue = "1") Integer category) {
        ComfortWord word = comfortService.randomByCategory(category);
        if (word == null) {
            return R.ok("宝宝正在马不停蹄地为你准备专属情话～", null);
        }
        angerProgress = Math.min(100, angerProgress + (int)(Math.random() * 11) + 5);
        return R.ok(word);
    }

    /**
     * 获取消气进度
     */
    @GetMapping("/anger-progress")
    public R getAngerProgress() {
        Map<String, Object> data = new HashMap<>();
        data.put("progress", angerProgress);
        data.put("isComplete", angerProgress >= 100);
        return R.ok(data);
    }

    /**
     * 增加消气进度
     */
    @PostMapping("/anger-progress/add")
    public R addAngerProgress(@RequestBody Map<String, Integer> params) {
        int amount = params.getOrDefault("amount", 5);
        angerProgress = Math.min(100, angerProgress + amount);
        Map<String, Object> data = new HashMap<>();
        data.put("progress", angerProgress);
        data.put("isComplete", angerProgress >= 100);
        return R.ok(data);
    }

    /**
     * 重置消气进度
     */
    @PostMapping("/anger-progress/reset")
    public R resetAngerProgress() {
        angerProgress = 0;
        return R.ok("消消气进度已重置~");
    }

    /**
     * 获取认错保证书
     */
    @GetMapping("/apology-letter")
    public R getApologyLetter() {
        ApologyLetter letter = comfortService.getActiveLetter();
        if (letter == null) {
            return R.ok("宝宝正在认真写保证书，请稍候...", null);
        }
        return R.ok(letter);
    }

    /**
     * 原谅他
     */
    @PostMapping("/apology/forgive")
    public R forgive() {
        memoryService.recordMoodEvent(1);
        angerProgress = 0;
        return R.ok("已原谅 💚 希望他好好表现~");
    }

    /**
     * 再罚一次
     */
    @PostMapping("/apology/punish")
    public R punish() {
        memoryService.recordMoodEvent(2);
        return R.ok("已记录，需要更多补偿 💪");
    }

    /**
     * 随机睡前故事
     */
    @GetMapping("/story/random")
    public R randomStory() {
        BedtimeStory story = comfortService.randomStory();
        if (story == null) {
            return R.ok("暂无睡前故事，宝宝正在努力创作中...", null);
        }
        return R.ok(story);
    }

    /**
     * 故事列表
     */
    @GetMapping("/story/list")
    public R storyList(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        Page<BedtimeStory> result = comfortService.storyPage(page, size);
        return R.ok(result);
    }

    /**
     * 故事详情
     */
    @GetMapping("/story/{id}")
    public R storyDetail(@PathVariable Integer id) {
        BedtimeStory story = comfortService.getStory(id);
        if (story == null) {
            return R.error("故事不存在");
        }
        return R.ok(story);
    }
}
