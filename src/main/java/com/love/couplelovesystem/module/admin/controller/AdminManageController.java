package com.love.couplelovesystem.module.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.entity.*;
import com.love.couplelovesystem.module.comfort.service.ComfortService;
import com.love.couplelovesystem.module.interaction.service.InteractionService;
import com.love.couplelovesystem.module.memory.service.MemoryService;
import com.love.couplelovesystem.module.privilege.service.PrivilegeService;
import com.love.couplelovesystem.module.system.service.SystemConfigService;
import com.love.couplelovesystem.mybatis.mapper.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 后台管理-内容管理控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminManageController {

    @Resource
    private ComfortService comfortService;
    @Resource
    private InteractionService interactionService;
    @Resource
    private MemoryService memoryService;
    @Resource
    private PrivilegeService privilegeService;
    @Resource
    private SystemConfigService configService;
    @Resource
    private BenefitGoodsMapper benefitGoodsMapper;
    @Resource
    private AnniversaryMapper anniversaryMapper;
    @Resource
    private CheckInMapper checkInMapper;

    // ==================== 话术管理 ====================

    @GetMapping("/comfort-words")
    public R comfortWordList(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer size,
                             @RequestParam(required = false) Integer category) {
        Page<ComfortWord> result = comfortService.wordPage(page, size, category);
        return R.ok(result);
    }

    @PostMapping("/comfort-word")
    public R addComfortWord(@RequestBody ComfortWord word) {
        comfortService.addWord(word);
        return R.ok("添加成功");
    }

    @PutMapping("/comfort-word/{id}")
    public R updateComfortWord(@PathVariable Integer id, @RequestBody ComfortWord word) {
        word.setId(id);
        comfortService.updateWord(word);
        return R.ok("编辑成功");
    }

    @DeleteMapping("/comfort-word/{id}")
    public R deleteComfortWord(@PathVariable Integer id) {
        comfortService.deleteWord(id);
        return R.ok("删除成功");
    }

    @PutMapping("/comfort-word/{id}/status")
    public R toggleComfortWordStatus(@PathVariable Integer id) {
        comfortService.toggleWordStatus(id);
        return R.ok("状态已切换");
    }

    // ==================== 保证书管理 ====================

    @GetMapping("/apology-letters")
    public R apologyLetterList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(comfortService.letterPage(page, size));
    }

    @PostMapping("/apology-letter")
    public R saveApologyLetter(@RequestBody ApologyLetter letter) {
        comfortService.saveLetter(letter);
        return R.ok("保存成功");
    }

    // ==================== 故事管理 ====================

    @GetMapping("/stories")
    public R storyList(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(comfortService.storyPage(page, size));
    }

    @PostMapping("/story")
    public R addStory(@RequestBody BedtimeStory story) {
        comfortService.addStory(story);
        return R.ok("添加成功");
    }

    @PutMapping("/story/{id}")
    public R updateStory(@PathVariable Integer id, @RequestBody BedtimeStory story) {
        story.setId(id);
        comfortService.updateStory(story);
        return R.ok("编辑成功");
    }

    @DeleteMapping("/story/{id}")
    public R deleteStory(@PathVariable Integer id) {
        comfortService.deleteStory(id);
        return R.ok("删除成功");
    }

    // ==================== 福利管理 ====================

    @GetMapping("/benefits")
    public R benefitList(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(interactionService.getBenefitList(page, size));
    }

    @PostMapping("/benefit")
    public R addBenefit(@RequestBody BenefitGoods goods) {
        goods.setStatus(1);
        benefitGoodsMapper.insert(goods);
        return R.ok("添加成功");
    }

    @PutMapping("/benefit/{id}")
    public R updateBenefit(@PathVariable Integer id, @RequestBody BenefitGoods goods) {
        goods.setId(id);
        benefitGoodsMapper.updateById(goods);
        return R.ok("编辑成功");
    }

    @PutMapping("/benefit/{id}/status")
    public R toggleBenefitStatus(@PathVariable Integer id) {
        BenefitGoods goods = benefitGoodsMapper.selectById(id);
        if (goods != null) {
            goods.setStatus(goods.getStatus() == 1 ? 0 : 1);
            benefitGoodsMapper.updateById(goods);
        }
        return R.ok("状态已切换");
    }

    // ==================== 兑换记录 ====================

    @GetMapping("/exchange-records")
    public R exchangeRecords(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "20") Integer size) {
        return R.ok(interactionService.getExchangeRecords(page, size));
    }

    @PutMapping("/exchange-record/{id}/fulfill")
    public R fulfillExchange(@PathVariable Integer id) {
        interactionService.fulfillExchange(id);
        return R.ok("已标记兑现");
    }

    // ==================== 豁免卡管理 ====================

    @GetMapping("/exemption-cards")
    public R exemptionCardList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(privilegeService.cardDefPage(page, size));
    }

    @PostMapping("/exemption-card")
    public R addExemptionCard(@RequestBody ExemptionCardDef def) {
        privilegeService.addCardDef(def);
        return R.ok("添加成功");
    }

    @DeleteMapping("/exemption-card/{id}")
    public R deleteExemptionCard(@PathVariable Integer id) {
        privilegeService.deleteCardDef(id);
        return R.ok("删除成功");
    }

    @PostMapping("/exemption-card/grant")
    public R grantCard(@RequestBody Map<String, Integer> params) {
        Integer cardDefId = params.get("cardDefId");
        Integer quantity = params.getOrDefault("quantity", 1);
        privilegeService.grantCard(cardDefId, quantity);
        return R.ok("授予成功");
    }

    @GetMapping("/exemption-card/usage")
    public R cardUsage(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "20") Integer size) {
        return R.ok(privilegeService.cardUsePage(page, size));
    }

    // ==================== 纪念日管理 ====================

    @GetMapping("/anniversaries")
    public R anniversaryList() {
        return R.ok(memoryService.getAnniversaries());
    }

    @PostMapping("/anniversary")
    public R addAnniversary(@RequestBody Anniversary anniversary) {
        anniversaryMapper.insert(anniversary);
        return R.ok("添加成功");
    }

    @PutMapping("/anniversary/{id}")
    public R updateAnniversary(@PathVariable Integer id, @RequestBody Anniversary anniversary) {
        anniversary.setId(id);
        anniversaryMapper.updateById(anniversary);
        return R.ok("编辑成功");
    }

    @DeleteMapping("/anniversary/{id}")
    public R deleteAnniversary(@PathVariable Integer id) {
        anniversaryMapper.deleteById(id);
        return R.ok("删除成功");
    }

    // ==================== 数据查看 ====================

    @GetMapping("/mood-records")
    public R moodRecords(@RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "20") Integer size) {
        return R.ok(memoryService.getMoodDiaries(page, size, null));
    }

    @DeleteMapping("/mood-record/{id}")
    public R deleteMoodRecord(@PathVariable Integer id) {
        moodDiaryMapper.deleteById(id);
        return R.ok("删除成功");
    }

    @GetMapping("/check-in-records")
    public R checkInRecords(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "20") Integer size) {
        QueryWrapper<CheckIn> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return R.ok(checkInMapper.selectPage(new Page<>(page, size), qw));
    }

    @GetMapping("/points-records")
    public R pointsRecords(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "20") Integer size,
                           @RequestParam(defaultValue = "2") Integer role) {
        return R.ok(interactionService.getPointsRecords(role, page, size));
    }

    @GetMapping("/messages")
    public R messages(@RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "20") Integer size) {
        return R.ok(interactionService.getMessages(page, size));
    }

    @DeleteMapping("/message/{id}")
    public R deleteMessage(@PathVariable Integer id) {
        messageMapper.deleteById(id);
        return R.ok("删除成功");
    }

    @GetMapping("/truth-answers")
    public R truthAnswers(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "20") Integer size) {
        return R.ok(interactionService.getTruthAnswers(page, size));
    }

    @GetMapping("/chemistry-tests")
    public R chemistryTests(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return R.ok(interactionService.getChemistryHistory(page, size));
    }

    // ==================== 真心话管理 ====================

    @Resource
    private TruthQuestionMapper truthQuestionMapper;
    @Resource
    private DailySurpriseMapper dailySurpriseMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private MoodDiaryMapper moodDiaryMapper;

    // ==================== 默契题管理 ====================

    @Resource
    private ChemistryQuestionMapper chemistryQuestionMapper;

    @GetMapping("/chemistry-questions")
    public R chemistryQuestionList() {
        return R.ok(chemistryQuestionMapper.selectList(null));
    }

    @PostMapping("/chemistry-question")
    public R addChemistryQuestion(@RequestBody ChemistryQuestion q) {
        chemistryQuestionMapper.insert(q);
        return R.ok("添加成功");
    }

    @DeleteMapping("/chemistry-question/{id}")
    public R delChemistryQuestion(@PathVariable Integer id) {
        chemistryQuestionMapper.deleteById(id);
        return R.ok("删除成功");
    }

    @GetMapping("/truth-questions")
    public R truthQuestionList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "100") Integer size,
                                @RequestParam(required = false) Integer category) {
        QueryWrapper<TruthQuestion> qw = new QueryWrapper<>();
        if (category != null && category > 0) qw.eq("category", category);
        qw.orderByDesc("id");
        return R.ok(truthQuestionMapper.selectPage(new Page<>(page, size), qw));
    }

    @PostMapping("/truth-question")
    public R addTruthQuestion(@RequestBody TruthQuestion q) {
        q.setSource(2); // 自定义
        q.setStatus(1);
        truthQuestionMapper.insert(q);
        return R.ok("添加成功");
    }

    @DeleteMapping("/truth-question/{id}")
    public R delTruthQuestion(@PathVariable Integer id) {
        truthQuestionMapper.deleteById(id);
        return R.ok("删除成功");
    }

    // ==================== 每日惊喜管理 ====================

    @GetMapping("/daily-surprises")
    public R dailySurpriseList() {
        QueryWrapper<DailySurprise> qw = new QueryWrapper<>();
        qw.orderByDesc("create_time");
        return R.ok(dailySurpriseMapper.selectPage(new Page<>(1, 50), qw));
    }

    @PostMapping("/daily-surprise")
    public R addDailySurprise(@RequestBody DailySurprise s) {
        s.setTriggerDate(new Date());
        dailySurpriseMapper.insert(s);
        return R.ok("添加成功");
    }

    @DeleteMapping("/daily-surprise/{id}")
    public R delDailySurprise(@PathVariable Integer id) {
        dailySurpriseMapper.deleteById(id);
        return R.ok("删除成功");
    }

    // ==================== 系统设置 ====================

    @GetMapping("/config")
    public R getAllConfig() {
        return R.ok(configService.getAllConfig());
    }

    @PutMapping("/config")
    public R updateConfig(@RequestBody Map<String, String> configs) {
        configService.updateConfig(configs);
        return R.ok("配置已更新");
    }
}
