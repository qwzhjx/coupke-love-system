package com.love.couplelovesystem.module.memory.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.constant.CommonConstants;
import com.love.couplelovesystem.entity.*;
import com.love.couplelovesystem.mybatis.mapper.*;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 恋爱记录服务
 */
@Service
public class MemoryService {

    @Value("${file.upload.photos}")
    private String photosPath;

    @Resource
    private AnniversaryMapper anniversaryMapper;
    @Resource
    private PhotoMapper photoMapper;
    @Resource
    private MoodDiaryMapper moodDiaryMapper;
    @Resource
    private SystemConfigMapper systemConfigMapper;

    // ========== 恋爱时光轴 ==========

    /**
     * 获取恋爱天数
     */
    public long getLoveDays() {
        QueryWrapper<SystemConfig> qw = new QueryWrapper<>();
        qw.eq("config_key", "love_start_date");
        SystemConfig config = systemConfigMapper.selectOne(qw);
        if (config == null || config.getConfigValue() == null) {
            return 0;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(config.getConfigValue());
            long diff = System.currentTimeMillis() - startDate.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 纪念日列表（含倒计时）
     */
    public List<Anniversary> getAnniversaries() {
        QueryWrapper<Anniversary> qw = new QueryWrapper<>();
        qw.orderByAsc("anniversary_date");
        List<Anniversary> list = anniversaryMapper.selectList(qw);
        Calendar today = Calendar.getInstance();
        for (Anniversary a : list) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(a.getAnniversaryDate());
            cal.set(Calendar.YEAR, today.get(Calendar.YEAR));
            if (cal.before(today) && a.getRepeatType() == CommonConstants.ANNIVERSARY_REPEAT_YEARLY) {
                cal.add(Calendar.YEAR, 1);
            }
            long diff = cal.getTimeInMillis() - today.getTimeInMillis();
            a.setRemainDays(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        }
        return list;
    }

    // ========== 相册 ==========

    public Page<Photo> getPhotos(Integer page, Integer size) {
        QueryWrapper<Photo> qw = new QueryWrapper<>();
        qw.orderByDesc("upload_time");
        return photoMapper.selectPage(new Page<>(page, size), qw);
    }

    public Photo uploadPhoto(MultipartFile file, String description) throws IOException {
        File dir = new File(photosPath);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String ext = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
        String thumbName = "thumb_" + fileName;

        File dest = new File(dir, fileName);
        file.transferTo(dest);

        // 生成缩略图
        Thumbnails.of(dest)
                .width(400)
                .keepAspectRatio(true)
                .toFile(new File(dir, thumbName));

        Photo photo = new Photo();
        photo.setUrl("/upload/photos/" + fileName);
        photo.setThumbnail("/upload/photos/" + thumbName);
        photo.setDescription(description);
        photoMapper.insert(photo);
        return photo;
    }

    // ========== 情绪日记 ==========

    /**
     * 自动记录情绪事件
     */
    public void recordMoodEvent(Integer scene) {
        MoodDiary diary = new MoodDiary();
        diary.setType(CommonConstants.MOOD_TYPE_AUTO);
        diary.setScene(scene);
        diary.setMood(sceneToMood(scene));
        diary.setCreateTime(new Date());
        moodDiaryMapper.insert(diary);
    }

    /**
     * 手动记录心情
     */
    public void addMoodDiary(MoodDiary diary) {
        diary.setType(CommonConstants.MOOD_TYPE_MANUAL);
        diary.setCreateTime(new Date());
        moodDiaryMapper.insert(diary);
    }

    /**
     * 情绪日记列表
     */
    public Page<MoodDiary> getMoodDiaries(Integer page, Integer size, Integer type) {
        QueryWrapper<MoodDiary> qw = new QueryWrapper<>();
        if (type != null) {
            qw.eq("type", type);
        }
        qw.orderByDesc("create_time");
        Page<MoodDiary> result = moodDiaryMapper.selectPage(new Page<>(page, size), qw);
        for (MoodDiary diary : result.getRecords()) {
            diary.setMoodText(getMoodText(diary.getMood()));
            diary.setSceneText(getSceneText(diary.getScene()));
        }
        return result;
    }

    private Integer sceneToMood(Integer scene) {
        if (scene == 1) return 4; // 生气
        if (scene == 2) return 3; // 委屈→难过
        return 3;
    }

    private String getMoodText(Integer mood) {
        if (mood == null) return "";
        switch (mood) {
            case 1: return "😊 开心";
            case 2: return "😐 一般";
            case 3: return "😢 难过";
            case 4: return "😡 生气";
            case 5: return "🥰 幸福";
            default: return "";
        }
    }

    private String getSceneText(Integer scene) {
        if (scene == null) return "";
        switch (scene) {
            case 1: return "生气了";
            case 2: return "委屈了";
            case 3: return "难过了";
            default: return "";
        }
    }
}
