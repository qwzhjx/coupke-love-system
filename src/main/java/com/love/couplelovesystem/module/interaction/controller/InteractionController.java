package com.love.couplelovesystem.module.interaction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.constant.CommonConstants;
import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.entity.*;
import com.love.couplelovesystem.module.interaction.service.InteractionService;
import com.love.couplelovesystem.mybatis.mapper.ChemistryQuestionMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 互动升温-前端控制器
 */
@RestController
@RequestMapping("/api/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    // ========== 打卡 ==========

    @PostMapping("/check-in")
    public R checkIn(@RequestBody Map<String, Integer> params) {
        Integer role = params.getOrDefault("role", CommonConstants.ROLE_GIRL);
        try {
            Map<String, Object> result = interactionService.checkIn(role);
            return R.ok("打卡成功！今天也很爱你 💗", result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/check-in/calendar")
    public R checkInCalendar(@RequestParam Integer role, @RequestParam String month) {
        List<Date> dates = interactionService.getCheckInCalendar(role, month);
        return R.ok(dates);
    }

    @GetMapping("/check-in/stats")
    public R checkInStats(@RequestParam Integer role) {
        return R.ok(interactionService.getCheckInStats(role));
    }

    // ========== 积分 ==========

    @GetMapping("/points/balance")
    public R pointsBalance(@RequestParam Integer role) {
        return R.ok(interactionService.getBalance(role));
    }

    @GetMapping("/points/records")
    public R pointsRecords(@RequestParam Integer role,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "20") Integer size) {
        Page<PointsRecord> records = interactionService.getPointsRecords(role, page, size);
        return R.ok(records);
    }

    // ========== 真心话 ==========

    @GetMapping("/truth/random")
    public R randomTruth() {
        TruthQuestion question = interactionService.randomTruthQuestion();
        if (question == null) {
            return R.ok("暂无真心话问题，请等待后台添加~", null);
        }
        return R.ok(question);
    }

    @PostMapping("/truth/answer")
    public R answerTruth(@RequestBody TruthAnswer answer) {
        interactionService.answerTruth(answer);
        return R.ok("回答已记录 💗");
    }

    // ========== 默契测试 ==========

    @Resource
    private ChemistryQuestionMapper chemistryQuestionMapper;

    @GetMapping("/chemistry/questions")
    public R chemistryQuestions() {
        return R.ok(chemistryQuestionMapper.selectList(null));
    }

    @PostMapping("/chemistry/submit")
    public R submitChemistry(@RequestBody Map<String, Object> params) {
        Integer score = (Integer) params.get("score");
        if (score == null) {
            return R.error("请提交测试分数");
        }
        Map<String, Object> result = interactionService.submitChemistryTest(score);
        return R.ok(result);
    }

    @GetMapping("/chemistry/history")
    public R chemistryHistory(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        Page<ChemistryTest> history = interactionService.getChemistryHistory(page, size);
        return R.ok(history);
    }

    // ========== 悄悄话 ==========

    @GetMapping("/messages")
    public R messages(@RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "20") Integer size) {
        Page<Message> messages = interactionService.getMessages(page, size);
        return R.ok(messages);
    }

    @PostMapping("/message")
    public R addMessage(@RequestBody Message message) {
        try {
            interactionService.addMessage(message);
            return R.ok("悄悄话已发送 💌");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PostMapping("/message/reply")
    public R replyMessage(@RequestBody Message message) {
        try {
            interactionService.addMessage(message);
            return R.ok("回复已发送 💌");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    // ========== 积分商城 ==========

    @GetMapping("/benefit/list")
    public R benefitList(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "20") Integer size) {
        Page<BenefitGoods> goods = interactionService.getBenefitList(page, size);
        return R.ok(goods);
    }

    @PostMapping("/benefit/exchange/{id}")
    public R exchangeBenefit(@PathVariable Integer id, @RequestBody Map<String, Integer> params) {
        Integer role = params.getOrDefault("role", CommonConstants.ROLE_GIRL);
        try {
            Map<String, Object> result = interactionService.exchangeBenefit(id, role);
            return R.ok("兑换成功！快去兑现吧~", result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
