package com.love.couplelovesystem.module.privilege.controller;

import com.love.couplelovesystem.common.utils.R;
import com.love.couplelovesystem.entity.DailySurprise;
import com.love.couplelovesystem.entity.ExemptionCardHold;
import com.love.couplelovesystem.module.privilege.service.PrivilegeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 偏爱特权-前端控制器
 */
@RestController
@RequestMapping("/api/privilege")
public class PrivilegeController {

    @Resource
    private PrivilegeService privilegeService;

    /**
     * 获取我的豁免卡
     */
    @GetMapping("/cards")
    public R getMyCards() {
        List<ExemptionCardHold> cards = privilegeService.getMyCards();
        return R.ok(cards);
    }

    /**
     * 使用豁免卡
     */
    @PostMapping("/card/use/{id}")
    public R useCard(@PathVariable Integer id) {
        try {
            privilegeService.useCard(id);
            return R.ok("豁免卡已使用！这次原谅他啦~ 💚");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 每日惊喜
     */
    @GetMapping("/surprise/daily")
    public R dailySurprise() {
        DailySurprise surprise = privilegeService.getDailySurprise();
        return R.ok(surprise);
    }

    /**
     * 专属标语
     */
    @GetMapping("/slogan")
    public R slogan() {
        String slogan = privilegeService.getSlogan();
        return R.ok(slogan);
    }
}
