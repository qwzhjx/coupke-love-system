package com.love.couplelovesystem.module.privilege.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.constant.CommonConstants;
import com.love.couplelovesystem.entity.*;
import com.love.couplelovesystem.mybatis.mapper.*;
import com.love.couplelovesystem.module.interaction.service.InteractionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 偏爱特权服务
 */
@Service
public class PrivilegeService {

    @Resource
    private ExemptionCardDefMapper cardDefMapper;
    @Resource
    private ExemptionCardHoldMapper cardHoldMapper;
    @Resource
    private ExemptionCardUseMapper cardUseMapper;
    @Resource
    private DailySurpriseMapper dailySurpriseMapper;
    @Resource
    private SystemConfigMapper configMapper;
    @Resource
    private InteractionService interactionService;

    // ========== 豁免卡 ==========

    /**
     * 获取我的豁免卡列表
     */
    public List<ExemptionCardHold> getMyCards() {
        QueryWrapper<ExemptionCardHold> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        List<ExemptionCardHold> holds = cardHoldMapper.selectList(qw);
        for (ExemptionCardHold hold : holds) {
            ExemptionCardDef def = cardDefMapper.selectById(hold.getCardDefId());
            if (def != null) {
                hold.setCardName(def.getName());
                hold.setCardDescription(def.getDescription());
                hold.setCardStyle(def.getCardStyle());
                hold.setRemainCount(hold.getQuantity() - hold.getUsedCount());
            }
        }
        return holds;
    }

    /**
     * 使用豁免卡
     */
    @Transactional
    public synchronized void useCard(Integer holdId) {
        ExemptionCardHold hold = cardHoldMapper.selectById(holdId);
        if (hold == null) {
            throw new RuntimeException("该豁免卡不存在~");
        }
        if (hold.getExpireTime() != null && new Date().after(hold.getExpireTime())) {
            throw new RuntimeException("该豁免卡已过期~");
        }
        if (hold.getUsedCount() >= hold.getQuantity()) {
            throw new RuntimeException("该豁免卡已用完啦~");
        }

        hold.setUsedCount(hold.getUsedCount() + 1);
        cardHoldMapper.updateById(hold);

        ExemptionCardUse use = new ExemptionCardUse();
        use.setHoldId(holdId);
        use.setUseTime(new Date());
        cardUseMapper.insert(use);
    }

    // ========== 每日惊喜 ==========

    /**
     * 获取今日惊喜
     */
    public DailySurprise getDailySurprise() {
        // 严格按日期(不含时间)检查今日是否已触发
        QueryWrapper<DailySurprise> qw = new QueryWrapper<>();
        qw.apply("DATE(trigger_date) = CURDATE()");
        qw.last("LIMIT 1");
        List<DailySurprise> todayList = dailySurpriseMapper.selectList(qw);
        if (todayList != null && !todayList.isEmpty()) {
            return todayList.get(0);
        }

        // 生成新的惊喜：优先用后台配置的惊喜库，否则用默认
        List<DailySurprise> pool = dailySurpriseMapper.selectList(null);
        DailySurprise surprise = new DailySurprise();
        if (pool != null && !pool.isEmpty()) {
            // 从惊喜库随机选一条内容，创建新记录（不能直接用pool里的对象，有id冲突）
            DailySurprise picked = pool.get(new Random().nextInt(pool.size()));
            surprise.setType(picked.getType());
            surprise.setContent(picked.getContent());
            surprise.setPoints(picked.getPoints());
        } else {
            // 默认惊喜
            int type = new Random().nextInt(4) + 1;
            surprise.setType(type);
            switch (type) {
                case CommonConstants.SURPRISE_TYPE_WORD: surprise.setContent(getRandomSurpriseWord()); break;
                case CommonConstants.SURPRISE_TYPE_POINTS:
                    int points = new Random().nextInt(16) + 5;
                    surprise.setPoints(points);
                    surprise.setContent("获得随机爱心积分红包 " + points + " 分！");
                    break;
                case CommonConstants.SURPRISE_TYPE_DISCOUNT: surprise.setContent("今日积分商城全场8折！限时宠爱~"); break;
                case CommonConstants.SURPRISE_TYPE_EGG: surprise.setContent(getRandomEgg()); break;
            }
        }
        // 只存日期部分
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0);
        surprise.setTriggerDate(cal.getTime());
        if (surprise.getType() != null && surprise.getType() == CommonConstants.SURPRISE_TYPE_POINTS && surprise.getPoints() != null && surprise.getPoints() > 0) {
            interactionService.addPoints(CommonConstants.ROLE_GIRL, surprise.getPoints(), "每日偏爱惊喜");
        }
        dailySurpriseMapper.insert(surprise);
        return surprise;
    }

    private String getRandomSurpriseWord() {
        String[] words = {
                "你是我的满心欢喜，也是我的独家记忆 💕",
                "遇见你是我这辈子最幸运的事 🌟",
                "你的笑容，是我每天最期待的风景 🌸",
                "在所有人事已非的景色里，我最喜欢你 💗",
                "我想和你一起，从心动到古稀 🕊️"
        };
        return words[new Random().nextInt(words.length)];
    }

    private String getRandomEgg() {
        String[] eggs = {
                "发现一个藏起来的拥抱 🫂",
                "解锁今日专属甜蜜语音 💌",
                "悄悄告诉你：宝宝今天特别想你 💭",
                "掉落一颗幸运小星星 ⭐"
        };
        return eggs[new Random().nextInt(eggs.length)];
    }

    // ========== 专属标语 ==========

    public String getSlogan() {
        QueryWrapper<SystemConfig> qw = new QueryWrapper<>();
        qw.eq("config_key", "page_slogan");
        SystemConfig config = configMapper.selectOne(qw);
        if (config == null || config.getConfigValue() == null) {
            return "💕 专属宠爱 · 只属于你 💕";
        }
        return config.getConfigValue();
    }

    // ========== 豁免卡管理(后台) ==========

    public Page<ExemptionCardDef> cardDefPage(Integer page, Integer size) {
        return cardDefMapper.selectPage(new Page<>(page, size), null);
    }

    public void addCardDef(ExemptionCardDef def) {
        cardDefMapper.insert(def);
    }

    public void deleteCardDef(Integer id) {
        cardDefMapper.deleteById(id);
    }

    @Transactional
    public void grantCard(Integer cardDefId, Integer quantity) {
        ExemptionCardDef def = cardDefMapper.selectById(cardDefId);
        if (def == null) throw new RuntimeException("豁免卡类型不存在");

        ExemptionCardHold hold = new ExemptionCardHold();
        hold.setCardDefId(cardDefId);
        hold.setQuantity(quantity);
        hold.setUsedCount(0);
        if (def.getValidDays() != null && def.getValidDays() > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, def.getValidDays());
            hold.setExpireTime(cal.getTime());
        }
        cardHoldMapper.insert(hold);
    }

    public Page<ExemptionCardUse> cardUsePage(Integer page, Integer size) {
        return cardUseMapper.selectPage(new Page<>(page, size), null);
    }
}
