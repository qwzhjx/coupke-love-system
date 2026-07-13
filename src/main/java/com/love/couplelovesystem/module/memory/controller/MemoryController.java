package com.love.couplelovesystem.module.memory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.entity.Anniversary;
import com.love.couplelovesystem.entity.MoodDiary;
import com.love.couplelovesystem.entity.Photo;
import com.love.couplelovesystem.module.memory.service.MemoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 恋爱记录-前端控制器
 */
@RestController
@RequestMapping("/api/memory")
public class MemoryController {

    @Resource
    private MemoryService memoryService;

    /**
     * 获取恋爱天数
     */
    @GetMapping("/love-days")
    public R getLoveDays() {
        long days = memoryService.getLoveDays();
        Map<String, Object> data = new HashMap<>();
        data.put("days", days);
        data.put("text", "我们已经在一起 " + days + " 天啦 💕");
        return R.ok(data);
    }

    /**
     * 纪念日列表
     */
    @GetMapping("/anniversaries")
    public R anniversaries() {
        List<Anniversary> list = memoryService.getAnniversaries();
        return R.ok(list);
    }

    /**
     * 纪念日详情
     */
    @GetMapping("/anniversary/{id}")
    public R anniversaryDetail(@PathVariable Integer id) {
        // 简化处理，从列表中获取
        List<Anniversary> list = memoryService.getAnniversaries();
        for (Anniversary a : list) {
            if (a.getId().equals(id)) return R.ok(a);
        }
        return R.error("纪念日不存在");
    }

    /**
     * 相册列表
     */
    @GetMapping("/photos")
    public R photos(@RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "20") Integer size) {
        Page<Photo> photos = memoryService.getPhotos(page, size);
        return R.ok(photos);
    }

    /**
     * 上传照片
     */
    @PostMapping("/photo/upload")
    public R uploadPhoto(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "description", required = false) String description) {
        try {
            Photo photo = memoryService.uploadPhoto(file, description);
            return R.ok("上传成功", photo);
        } catch (Exception e) {
            return R.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 情绪日记列表
     */
    @GetMapping("/mood/diary")
    public R moodDiary(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "20") Integer size,
                       @RequestParam(required = false) Integer type) {
        Page<MoodDiary> diaries = memoryService.getMoodDiaries(page, size, type);
        return R.ok(diaries);
    }

    /**
     * 手动记录心情
     */
    @PostMapping("/mood/diary")
    public R addMoodDiary(@RequestBody MoodDiary diary) {
        memoryService.addMoodDiary(diary);
        return R.ok("心情已记录 💗");
    }

    /**
     * 月度情绪趋势
     */
    @GetMapping("/mood/trend")
    public R moodTrend(@RequestParam(defaultValue = "6") Integer months) {
        // 简化处理：返回近6个月的情绪统计数据
        Page<MoodDiary> diaries = memoryService.getMoodDiaries(1, 200, null);
        Map<Integer, Integer> moodCount = new HashMap<>();
        moodCount.put(1, 0); // 开心
        moodCount.put(2, 0); // 一般
        moodCount.put(3, 0); // 难过
        moodCount.put(4, 0); // 生气
        moodCount.put(5, 0); // 幸福

        for (MoodDiary diary : diaries.getRecords()) {
            if (diary.getMood() != null) {
                moodCount.merge(diary.getMood(), 1, Integer::sum);
            }
        }
        return R.ok(moodCount);
    }
}
