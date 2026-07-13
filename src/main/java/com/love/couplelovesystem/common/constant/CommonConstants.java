package com.love.couplelovesystem.common.constant;

/**
 * 公共常量
 */
public interface CommonConstants {

    /** 成功状态码 */
    int CODE_SUCCESS = 200;
    /** 失败状态码 */
    int CODE_ERROR = 500;
    /** 未授权 */
    int CODE_UNAUTHORIZED = 401;
    /** 未找到 */
    int CODE_NOT_FOUND = 404;

    /** 角色-男主 */
    int ROLE_BOY = 1;
    /** 角色-女主 */
    int ROLE_GIRL = 2;

    /** 话术场景-轻微生气 */
    int COMFORT_SCENE_LIGHT = 1;
    /** 话术场景-超级生气 */
    int COMFORT_SCENE_HEAVY = 2;
    /** 话术场景-委屈难过 */
    int COMFORT_SCENE_SAD = 3;

    /** 积分类型-获取 */
    int POINTS_TYPE_GAIN = 1;
    /** 积分类型-消费 */
    int POINTS_TYPE_SPEND = 2;

    /** 纪念日类型-年度重复 */
    int ANNIVERSARY_REPEAT_YEARLY = 1;
    /** 纪念日类型-仅一次 */
    int ANNIVERSARY_REPEAT_ONCE = 2;

    /** 情绪日记类型-自动记录 */
    int MOOD_TYPE_AUTO = 1;
    /** 情绪日记类型-手动记录 */
    int MOOD_TYPE_MANUAL = 2;

    /** 惊喜类型-情话卡片 */
    int SURPRISE_TYPE_WORD = 1;
    /** 惊喜类型-积分红包 */
    int SURPRISE_TYPE_POINTS = 2;
    /** 惊喜类型-临时折扣 */
    int SURPRISE_TYPE_DISCOUNT = 3;
    /** 惊喜类型-彩蛋 */
    int SURPRISE_TYPE_EGG = 4;

    /** 每日打卡积分 */
    int CHECK_IN_POINTS = 10;
    /** 情绪平复积分 */
    int COMFORT_POINTS = 5;
    /** 真心话积分 */
    int TRUTH_POINTS = 5;
    /** 默契测试积分 */
    int CHEMISTRY_POINTS = 8;
    /** 每日首次访问积分 */
    int DAILY_VISIT_POINTS = 2;
    /** 每日积分上限 */
    int DAILY_POINTS_MAX = 50;
}
