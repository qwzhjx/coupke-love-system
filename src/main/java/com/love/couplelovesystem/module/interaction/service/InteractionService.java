package com.love.couplelovesystem.module.interaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.constant.CommonConstants;
import com.love.couplelovesystem.entity.*;
import com.love.couplelovesystem.mybatis.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 互动升温服务
 */
@Service
public class InteractionService {

    @Resource
    private CheckInMapper checkInMapper;
    @Resource
    private PointsRecordMapper pointsRecordMapper;
    @Resource
    private TruthQuestionMapper truthQuestionMapper;
    @Resource
    private TruthAnswerMapper truthAnswerMapper;
    @Resource
    private ChemistryTestMapper chemistryTestMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private BenefitGoodsMapper benefitGoodsMapper;
    @Resource
    private BenefitExchangeMapper benefitExchangeMapper;

    // ========== 打卡 ==========

    /**
     * 每日打卡
     */
    @Transactional
    public synchronized Map<String, Object> checkIn(Integer role) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        QueryWrapper<CheckIn> qw = new QueryWrapper<>();
        qw.eq("role", role).eq("check_date", today);
        if (checkInMapper.selectOne(qw) != null) {
            throw new RuntimeException("今天已经打过卡啦~💗");
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setRole(role);
        checkIn.setCheckDate(new Date());
        checkInMapper.insert(checkIn);

        // 加积分
        addPoints(role, CommonConstants.CHECK_IN_POINTS, "每日甜蜜打卡");

        Map<String, Object> result = new HashMap<>();
        result.put("totalCheckIns", getTotalCheckIns(role));
        result.put("today", today);
        return result;
    }

    /**
     * 获取打卡日历
     */
    public List<Date> getCheckInCalendar(Integer role, String yearMonth) {
        String startDate = yearMonth + "-01";
        String endDate = yearMonth + "-31";
        return checkInMapper.getCheckInDates(role, startDate, endDate);
    }

    /**
     * 打卡统计
     */
    public Map<String, Object> getCheckInStats(Integer role) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCheckIns", getTotalCheckIns(role));
        stats.put("maxConsecutiveDays", checkInMapper.getMaxConsecutiveDays(role));
        return stats;
    }

    private Long getTotalCheckIns(Integer role) {
        QueryWrapper<CheckIn> qw = new QueryWrapper<>();
        qw.eq("role", role);
        return checkInMapper.selectCount(qw);
    }

    // ========== 积分 ==========

    public Integer getBalance(Integer role) {
        Integer balance = pointsRecordMapper.getBalance(role);
        return balance != null ? balance : 0;
    }

    public Page<PointsRecord> getPointsRecords(Integer role, Integer page, Integer size) {
        QueryWrapper<PointsRecord> qw = new QueryWrapper<>();
        qw.eq("role", role).orderByDesc("create_time");
        return pointsRecordMapper.selectPage(new Page<>(page, size), qw);
    }

    public void addPoints(Integer role, Integer amount, String source) {
        PointsRecord record = new PointsRecord();
        record.setRole(role);
        record.setType(CommonConstants.POINTS_TYPE_GAIN);
        record.setAmount(amount);
        record.setSource(source);
        pointsRecordMapper.insert(record);
    }

    public Integer getTodayGain(Integer role) {
        Integer gain = pointsRecordMapper.getTodayGain(role);
        return gain != null ? gain : 0;
    }

    // ========== 真心话 ==========

    public TruthQuestion randomTruthQuestion() {
        QueryWrapper<TruthQuestion> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        List<TruthQuestion> list = truthQuestionMapper.selectList(qw);
        if (list == null || list.isEmpty()) return null;
        return list.get((int) (Math.random() * list.size()));
    }

    public void answerTruth(TruthAnswer answer) {
        truthAnswerMapper.insert(answer);
        addPoints(answer.getRole(), CommonConstants.TRUTH_POINTS, "真心话互动");
    }

    public Page<TruthAnswer> getTruthAnswers(Integer page, Integer size) {
        QueryWrapper<TruthAnswer> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return truthAnswerMapper.selectPage(new Page<>(page, size), qw);
    }

    // ========== 默契测试 ==========

    @Transactional
    public Map<String, Object> submitChemistryTest(Integer score) {
        ChemistryTest test = new ChemistryTest();
        test.setScore(score);
        String level;
        if (score >= 90) level = "💯天生一对";
        else if (score >= 70) level = "💕默契十足";
        else if (score >= 50) level = "💗还需努力";
        else level = "💪继续加油";
        test.setLevel(level);
        chemistryTestMapper.insert(test);

        addPoints(CommonConstants.ROLE_GIRL, CommonConstants.CHEMISTRY_POINTS, "默契测试奖励");

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("level", level);
        return result;
    }

    public Page<ChemistryTest> getChemistryHistory(Integer page, Integer size) {
        QueryWrapper<ChemistryTest> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return chemistryTestMapper.selectPage(new Page<>(page, size), qw);
    }

    // ========== 悄悄话 ==========

    public Page<Message> getMessages(Integer page, Integer size) {
        QueryWrapper<Message> qw = new QueryWrapper<>();
        qw.isNull("parent_id").orderByDesc("create_time");
        Page<Message> result = messageMapper.selectPage(new Page<>(page, size), qw);
        // 查询回复
        for (Message msg : result.getRecords()) {
            QueryWrapper<Message> replyQw = new QueryWrapper<>();
            replyQw.eq("parent_id", msg.getId()).orderByAsc("create_time");
            msg.setRoleName(msg.getRole() == 1 ? "👦 小轩大王" : "👧 女朋友");
        }
        return result;
    }

    public void addMessage(Message message) {
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new RuntimeException("悄悄话不能为空哦~");
        }
        if (message.getContent().length() > 500) {
            throw new RuntimeException("悄悄话不能超过500字哦~");
        }
        messageMapper.insert(message);
    }

    // ========== 积分商城 ==========

    public Page<BenefitGoods> getBenefitList(Integer page, Integer size) {
        QueryWrapper<BenefitGoods> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        return benefitGoodsMapper.selectPage(new Page<>(page, size), qw);
    }

    @Transactional
    public synchronized Map<String, Object> exchangeBenefit(Integer goodsId, Integer role) {
        BenefitGoods goods = benefitGoodsMapper.selectById(goodsId);
        if (goods == null || goods.getStatus() == 0) {
            throw new RuntimeException("该商品已下架~");
        }
        if (goods.getStock() == 0) {
            throw new RuntimeException("该商品已兑换完毕~");
        }
        Integer balance = getBalance(role);
        if (balance < goods.getPoints()) {
            throw new RuntimeException("积分不足哦~ 还差 " + (goods.getPoints() - balance) + " 分");
        }

        // 扣积分
        PointsRecord record = new PointsRecord();
        record.setRole(role);
        record.setType(CommonConstants.POINTS_TYPE_SPEND);
        record.setAmount(goods.getPoints());
        record.setSource("兑换:" + goods.getName());
        pointsRecordMapper.insert(record);

        // 减库存
        if (goods.getStock() > 0) {
            goods.setStock(goods.getStock() - 1);
            benefitGoodsMapper.updateById(goods);
        }

        // 生成兑换记录
        BenefitExchange exchange = new BenefitExchange();
        exchange.setGoodsId(goods.getId());
        exchange.setGoodsName(goods.getName());
        exchange.setPoints(goods.getPoints());
        exchange.setStatus(0);
        benefitExchangeMapper.insert(exchange);

        Map<String, Object> result = new HashMap<>();
        result.put("goodsName", goods.getName());
        result.put("points", goods.getPoints());
        result.put("remainBalance", getBalance(role));
        return result;
    }

    public Page<BenefitExchange> getExchangeRecords(Integer page, Integer size) {
        QueryWrapper<BenefitExchange> qw = new QueryWrapper<>();
        qw.orderByDesc("exchange_time");
        return benefitExchangeMapper.selectPage(new Page<>(page, size), qw);
    }

    public void fulfillExchange(Integer id) {
        BenefitExchange exchange = benefitExchangeMapper.selectById(id);
        if (exchange != null) {
            exchange.setStatus(1);
            exchange.setFulfillTime(new Date());
            benefitExchangeMapper.updateById(exchange);
        }
    }
}
