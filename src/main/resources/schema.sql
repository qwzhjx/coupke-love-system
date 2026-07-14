-- ============================================
-- 情侣亲密度升温系统 - H2 数据库初始化
-- ============================================

-- 系统配置表
CREATE TABLE IF NOT EXISTS t_system_config (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    config_key     VARCHAR(50) NOT NULL UNIQUE,
    config_value   TEXT,
    update_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 管理员表
CREATE TABLE IF NOT EXISTS t_admin (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(128) NOT NULL,
    salt        VARCHAR(32) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 哄宠话术表
CREATE TABLE IF NOT EXISTS t_comfort_word (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    category    TINYINT NOT NULL,
    content     TEXT NOT NULL,
    voice_url   VARCHAR(255),
    status      TINYINT DEFAULT 1,
    show_count  INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 认错保证书表
CREATE TABLE IF NOT EXISTS t_apology_letter (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100) NOT NULL,
    content     TEXT NOT NULL,
    voice_url   VARCHAR(255),
    status      TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 睡前故事表
CREATE TABLE IF NOT EXISTS t_bedtime_story (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100) NOT NULL,
    content     TEXT NOT NULL,
    source      TINYINT DEFAULT 1,
    status      TINYINT DEFAULT 1,
    like_count  INT DEFAULT 0,
    voice_url   VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 打卡记录表
CREATE TABLE IF NOT EXISTS t_check_in (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    role        TINYINT NOT NULL,
    check_date  DATE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_date (role, check_date)
);

-- 积分记录表
CREATE TABLE IF NOT EXISTS t_points_record (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    role        TINYINT NOT NULL,
    type        TINYINT NOT NULL,
    amount      INT NOT NULL,
    source      VARCHAR(50),
    remark      VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 真心话题库表
CREATE TABLE IF NOT EXISTS t_truth_question (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    category    TINYINT DEFAULT 1,
    question    TEXT NOT NULL,
    source      TINYINT DEFAULT 1,
    status      TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 真心话回答记录
CREATE TABLE IF NOT EXISTS t_truth_answer (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT NOT NULL,
    answer      TEXT,
    role        TINYINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 默契测试记录
CREATE TABLE IF NOT EXISTS t_chemistry_test (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    score       INT NOT NULL,
    level       VARCHAR(20),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 默契测试题库
CREATE TABLE IF NOT EXISTS t_chemistry_question (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    question    TEXT NOT NULL,
    opts        TEXT NOT NULL,
    answer      INT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 悄悄话留言表
CREATE TABLE IF NOT EXISTS t_message (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    parent_id   INT,
    role        TINYINT NOT NULL,
    content     VARCHAR(500) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 纪念日表
CREATE TABLE IF NOT EXISTS t_anniversary (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL,
    anniversary_date DATE NOT NULL,
    repeat_type      TINYINT DEFAULT 1,
    remind_days      VARCHAR(20) DEFAULT '3,1',
    icon             VARCHAR(50),
    create_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 甜蜜相册表
CREATE TABLE IF NOT EXISTS t_photo (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    url         VARCHAR(255) NOT NULL,
    thumbnail   VARCHAR(255),
    description VARCHAR(255),
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 情绪日记表
CREATE TABLE IF NOT EXISTS t_mood_diary (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    type         TINYINT NOT NULL,
    mood         TINYINT,
    scene        TINYINT,
    resolve_time INT,
    note         VARCHAR(500),
    is_private   TINYINT DEFAULT 0,
    create_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 豁免卡定义表
CREATE TABLE IF NOT EXISTS t_exemption_card_def (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    card_style  TINYINT DEFAULT 1,
    valid_days  INT,
    status      TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 豁免卡持有表
CREATE TABLE IF NOT EXISTS t_exemption_card_hold (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    card_def_id INT NOT NULL,
    quantity    INT DEFAULT 1,
    used_count  INT DEFAULT 0,
    expire_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 豁免卡使用记录
CREATE TABLE IF NOT EXISTS t_exemption_card_use (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    hold_id  INT NOT NULL,
    use_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 每日惊喜表
CREATE TABLE IF NOT EXISTS t_daily_surprise (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    type         TINYINT NOT NULL,
    content      TEXT,
    points       INT,
    trigger_date DATE NOT NULL,
    create_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 福利商品表
CREATE TABLE IF NOT EXISTS t_benefit_goods (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    image_url   VARCHAR(255),
    points      INT NOT NULL,
    stock       INT DEFAULT -1,
    description VARCHAR(500),
    status      TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 福利兑换记录
CREATE TABLE IF NOT EXISTS t_benefit_exchange (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    goods_id      INT NOT NULL,
    goods_name    VARCHAR(100),
    points        INT NOT NULL,
    status        TINYINT DEFAULT 0,
    exchange_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fulfill_time  TIMESTAMP
);

-- ========== 默认数据 ==========

-- 默认系统配置（管理员由程序自动创建）
INSERT INTO t_system_config (config_key, config_value) VALUES
('access_password', '5201314'),
('page_slogan', '🎀 Hello Kitty · 专属宠爱只属于你 🎀'),
('boy_nickname', '宝宝'),
('girl_nickname', '女朋友'),
('love_start_date', '2024-01-01'),
('theme_color', '#F8A5C2');

-- 系统内置睡前故事（5篇）
INSERT INTO t_bedtime_story (title, content, source) VALUES
('小兔子的睡前故事', '从前，在一片美丽的大森林里，住着一只可爱的小兔子。每天晚上，小兔子都会望着天空中的星星，想着远方的那只小兔子。她们虽然相隔很远，但每晚都会在同一时间看着同一片星空，心里暖暖的。小兔子知道，不管距离多远，真心相爱的两颗心永远都在一起。晚安，我最爱的小兔子。🌟', 1),
('最温柔的月光', '你知道吗？月亮之所以那么温柔，是因为它承载了太多人的思念。今晚的月光特别美，我想把最美的月光都收集起来，送给你。当你闭上眼睛的时候，月光会轻轻落在你的脸上，就像我的吻一样温柔。睡吧，我的宝贝，明天醒来又是一个有我的好天气。💤', 1),
('星星与大海的约定', '在遥远的海边，有一颗最亮的星星和一片最蓝的大海。星星每天晚上都会把光芒洒在海面上，大海则用温柔的波浪回应它。它们约定好，无论多少个夜晚过去，都要这样彼此陪伴。就像我们一样，无论经历什么，都会一直一直在一起。晚安，我的星星。✨', 1),
('永远在一起的小熊', '有一只小熊，它有一个最心爱的蜂蜜罐子。但它最喜欢的事情不是吃蜂蜜，而是每天抱着罐子睡觉。因为罐子里装满了它对另一只小熊的想念。每一滴蜂蜜都是一句"我爱你"，甜到心里去。所以每晚入睡前，小熊都会说："明天也要比今天更爱你一点哦。"🐻', 1),
('花与蝴蝶的梦', '花园里有一朵最美的花，每天都会有一只蝴蝶飞来陪它。蝴蝶告诉花说："世界很大，但我只想停在你的花瓣上。"花笑了，花瓣轻轻摇动。它们一起做了一个很甜很甜的梦，梦里有花、有蝴蝶、还有永远不会结束的春天。晚安，愿你梦里有我。🦋', 1);

-- 系统内置真心话问题
INSERT INTO t_truth_question (category, question, source) VALUES
(1, '我们第一次见面的时候，你心里是怎么想的？', 1),
(1, '你觉得我们的感情里，最让你心动的一个瞬间是什么？', 1),
(1, '如果用一首歌来形容我们的爱情，你会选哪一首？', 1),
(1, '你记忆中我们一起做过最疯狂的事是什么？', 1),
(2, '你最想和我一起去旅行的地方是哪里？', 1),
(2, '你觉得我们十年后的生活会是什么样子？', 1),
(2, '最想和我一起完成的一件事是什么？', 1),
(3, '今天最让你开心的一件事是什么？', 1),
(3, '今天有没有什么小秘密想要告诉我？', 1),
(3, '用三个词来形容今天的我吧~', 1);