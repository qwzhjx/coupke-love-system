-- ============================================
-- 情侣亲密度升温系统 - 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS couple_love DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE couple_love;

-- ==================== 系统相关 ====================

DROP TABLE IF EXISTS t_admin;
CREATE TABLE t_admin (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(128) NOT NULL COMMENT 'MD5+Salt加密密码',
    salt        VARCHAR(32)  NOT NULL COMMENT '加密盐值',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

DROP TABLE IF EXISTS t_system_config;
CREATE TABLE t_system_config (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    config_key     VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
    config_value   TEXT COMMENT '配置值',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ==================== 话术相关 ====================

DROP TABLE IF EXISTS t_comfort_word;
CREATE TABLE t_comfort_word (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    category    TINYINT NOT NULL COMMENT '1=轻微生气 2=超级生气 3=委屈难过',
    content     TEXT    NOT NULL COMMENT '话术正文',
    voice_url   VARCHAR(255) COMMENT '关联语音文件路径',
    status      TINYINT DEFAULT 1 COMMENT '1=启用 0=停用',
    show_count  INT     DEFAULT 0 COMMENT '展示次数统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='哄宠话术表';

DROP TABLE IF EXISTS t_apology_letter;
CREATE TABLE t_apology_letter (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100) NOT NULL COMMENT '标题',
    content     TEXT         NOT NULL COMMENT '全文内容',
    status      TINYINT DEFAULT 1 COMMENT '1=启用 0=停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='认错保证书表';

DROP TABLE IF EXISTS t_bedtime_story;
CREATE TABLE t_bedtime_story (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100) NOT NULL COMMENT '故事标题',
    content     TEXT         NOT NULL COMMENT '故事内容',
    source      TINYINT DEFAULT 1 COMMENT '1=系统内置 2=男主自定义',
    status      TINYINT DEFAULT 1 COMMENT '1=启用 0=停用',
    like_count  INT     DEFAULT 0 COMMENT '喜欢次数',
    voice_url   VARCHAR(255) COMMENT '关联语音路径',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='睡前故事表';

-- ==================== 互动相关 ====================

DROP TABLE IF EXISTS t_check_in;
CREATE TABLE t_check_in (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    role        TINYINT NOT NULL COMMENT '1=男主 2=女主',
    check_date  DATE    NOT NULL COMMENT '打卡日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_date (role, check_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

DROP TABLE IF EXISTS t_points_record;
CREATE TABLE t_points_record (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    role        TINYINT NOT NULL COMMENT '1=男主 2=女主',
    type        TINYINT NOT NULL COMMENT '1=获取 2=消费',
    amount      INT     NOT NULL COMMENT '积分数量',
    source      VARCHAR(50) COMMENT '来源描述',
    remark      VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

DROP TABLE IF EXISTS t_truth_question;
CREATE TABLE t_truth_question (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    category    TINYINT DEFAULT 1 COMMENT '1=回忆类 2=期待类 3=日常类',
    question    TEXT NOT NULL COMMENT '问题内容',
    source      TINYINT DEFAULT 1 COMMENT '1=系统内置 2=男主自定义',
    status      TINYINT DEFAULT 1 COMMENT '1=启用 0=停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='真心话题库表';

DROP TABLE IF EXISTS t_truth_answer;
CREATE TABLE t_truth_answer (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT  NOT NULL COMMENT '问题ID',
    answer      TEXT COMMENT '回答内容',
    role        TINYINT NOT NULL COMMENT '回答者角色 1=男主 2=女主',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='真心话回答记录';

DROP TABLE IF EXISTS t_chemistry_test;
CREATE TABLE t_chemistry_test (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    score       INT     NOT NULL COMMENT '得分',
    level       VARCHAR(20) COMMENT '等级',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='默契测试记录';

DROP TABLE IF EXISTS t_message;
CREATE TABLE t_message (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    parent_id   INT COMMENT '父留言ID（回复用）',
    role        TINYINT NOT NULL COMMENT '1=男主 2=女主',
    content     VARCHAR(500) NOT NULL COMMENT '留言内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='悄悄话留言表';

-- ==================== 记录相关 ====================

DROP TABLE IF EXISTS t_anniversary;
CREATE TABLE t_anniversary (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL COMMENT '纪念日名称',
    anniversary_date DATE         NOT NULL COMMENT '纪念日日期',
    repeat_type      TINYINT DEFAULT 1 COMMENT '1=年度重复 2=仅一次',
    remind_days      VARCHAR(20) DEFAULT '3,1' COMMENT '提前提醒天数',
    icon             VARCHAR(50) COMMENT '图标名称',
    create_time      DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纪念日表';

DROP TABLE IF EXISTS t_photo;
CREATE TABLE t_photo (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    url         VARCHAR(255) NOT NULL COMMENT '图片路径',
    thumbnail   VARCHAR(255) COMMENT '缩略图路径',
    description VARCHAR(255) COMMENT '图片描述',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='甜蜜相册表';

DROP TABLE IF EXISTS t_mood_diary;
CREATE TABLE t_mood_diary (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    type         TINYINT NOT NULL COMMENT '1=自动记录 2=手动记录',
    mood         TINYINT COMMENT '1=开心 2=一般 3=难过 4=生气 5=幸福',
    scene        TINYINT COMMENT '情绪急救场景 1=生气了 2=委屈了 3=难过了',
    resolve_time INT COMMENT '平复时长(分钟)',
    note         VARCHAR(500) COMMENT '备注',
    is_private   TINYINT DEFAULT 0 COMMENT '0=双方可见 1=仅自己可见',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='情绪日记表';

-- ==================== 特权相关 ====================

DROP TABLE IF EXISTS t_exemption_card_def;
CREATE TABLE t_exemption_card_def (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL COMMENT '卡名称',
    description VARCHAR(255) COMMENT '卡描述',
    card_style  TINYINT DEFAULT 1 COMMENT '1=粉色爱心 2=紫色星月 3=金色皇冠',
    valid_days  INT COMMENT '有效期天数(NULL=永久有效)',
    status      TINYINT DEFAULT 1 COMMENT '1=启用 0=停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='豁免卡定义表';

DROP TABLE IF EXISTS t_exemption_card_hold;
CREATE TABLE t_exemption_card_hold (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    card_def_id INT NOT NULL COMMENT '豁免卡定义ID',
    quantity    INT DEFAULT 1 COMMENT '总数量',
    used_count  INT DEFAULT 0 COMMENT '已使用次数',
    expire_time DATETIME COMMENT '过期时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='豁免卡持有表';

DROP TABLE IF EXISTS t_exemption_card_use;
CREATE TABLE t_exemption_card_use (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    hold_id  INT NOT NULL COMMENT '持有记录ID',
    use_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='豁免卡使用记录';

DROP TABLE IF EXISTS t_daily_surprise;
CREATE TABLE t_daily_surprise (
    id           INT PRIMARY KEY AUTO_INCREMENT,
    type         TINYINT NOT NULL COMMENT '1=情话 2=积分红包 3=临时折扣 4=彩蛋',
    content      TEXT COMMENT '惊喜内容',
    points       INT COMMENT '积分红包额度',
    trigger_date DATE NOT NULL COMMENT '触发日期',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日惊喜表';

DROP TABLE IF EXISTS t_benefit_goods;
CREATE TABLE t_benefit_goods (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL COMMENT '商品名称',
    image_url   VARCHAR(255) COMMENT '图片路径',
    points      INT NOT NULL COMMENT '所需积分',
    stock       INT DEFAULT -1 COMMENT '库存(-1=不限)',
    description VARCHAR(500) COMMENT '商品描述',
    status      TINYINT DEFAULT 1 COMMENT '1=上架 0=下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='福利商品表';

DROP TABLE IF EXISTS t_benefit_exchange;
CREATE TABLE t_benefit_exchange (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    goods_id      INT NOT NULL COMMENT '商品ID',
    goods_name    VARCHAR(100) COMMENT '商品名称',
    points        INT NOT NULL COMMENT '消耗积分',
    status        TINYINT DEFAULT 0 COMMENT '0=待兑现 1=已兑现',
    exchange_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    fulfill_time  DATETIME COMMENT '兑现时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='福利兑换记录表';

-- ==================== 初始化数据 ====================

-- 默认系统配置
INSERT INTO t_system_config (config_key, config_value) VALUES
('access_password', '5201314'),
('page_slogan', '💕 专属宠爱 · 只属于你 💕'),
('boy_nickname', '宝宝'),
('girl_nickname', '女朋友'),
('love_start_date', '2024-01-01'),
('theme_color', '#FFB6C1');

-- 系统内置睡前故事
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
(2, '你希望我们的未来有哪些特别的事？', 1),
(3, '今天最让你开心的一件事是什么？', 1),
(3, '今天有没有什么小秘密想要告诉我？', 1),
(3, '用三个词来形容今天的我吧~', 1),
(3, '今天对我的喜欢又多了多少？', 1);

-- 追加15篇睡前故事（共计20篇）
INSERT INTO t_bedtime_story (title, content, source) VALUES
('爱吃糖的小云朵', '天上有一朵小云朵，它特别爱吃糖。每次吃到甜甜的糖果，它就会变成粉红色，然后下起甜甜的糖果雨。地上的小朋友们可开心了，但小云朵说："这些糖果雨只给最可爱的小朋友哦～"今天它看到你，觉得你最可爱，所以今晚的梦里会有好多好多糖果。晚安，甜甜蜜蜜的小可爱。🍬', 1),
('月亮船', '弯弯的月亮像一艘小船，每天晚上它都会划过夜空，收集人们的愿望。今晚月亮船收集到了一个特别温暖的愿望——希望你能做一个好梦。于是月亮船把最柔和的月光洒在你的窗前，还让星星们小声地唱着摇篮曲。闭上眼睛吧，月亮船正载着你驶向甜甜的梦乡。🌙', 1),
('会讲故事的猫咪', '有一只毛茸茸的小猫咪，它有一个特别的本领——会讲睡前故事。每天晚上，小猫咪都会趴在你的枕头边，用软软的声音给你讲一个全新的故事。今天的故事是关于一个勇敢的小公主和她忠诚的小骑士，他们一起打败了噩梦怪兽，守护了所有好梦。小猫咪说："你也是我的小公主，我会一直守护你的梦。"🐱', 1),
('彩虹滑梯', '你知道吗？每当下过雨之后出现的彩虹，其实是一个大大的滑梯。小天使们会从彩虹上滑下来，把快乐带到人间。今天有一个小天使滑下来的时候，正好看到你在笑，她觉得你的笑容比彩虹还要美。所以她决定今晚偷偷飞到你的梦里，带你一起玩彩虹滑梯。准备好了吗？3、2、1——滑！🌈', 1),
('睡不着的小星星', '天上有一颗最小的小星星，它每天晚上都睡不着，因为它总想多看看地上的人们。有一天它发现了一个秘密：地上有一个特别可爱的人在数星星。小星星开心极了，它一闪一闪地打招呼，然后轻轻地唱起了歌。听着小星星的歌，那个人慢慢地、慢慢地闭上了眼睛。你猜那个人是谁？就是你呀～✨', 1),
('住在贝壳里的精灵', '大海边有一枚粉色的贝壳，里面住着一只小小的精灵。精灵的工作是收集所有美好的心情，然后把它们变成珍珠。今天精灵收集到了特别多好心情——因为你想到了开心的事。精灵把这些心情做成了一个最大最亮的珍珠，放在你的枕头下面。这颗珍珠会保佑你做一个闪闪发光的好梦。🐚', 1),
('棉花糖森林', '在很远很远的地方，有一片棉花糖森林。那里的树是棉花糖做的，小溪里流的是热巧克力，草地上开的花是棒棒糖。守护这片森林的是一只软绵绵的大白熊，它最喜欢的事情就是邀请可爱的小朋友来森林里做客。今天大白熊给你发来了邀请函，上面写着："今晚梦里见，我准备了最好吃的棉花糖等你哦～"🐻', 1),
('蒲公英的旅行', '有一朵小小的蒲公英，它最大的梦想就是去看遍全世界。风爷爷帮它飞过了高山、飞过了大海、飞过了沙漠。但最后蒲公英选择落在了一个小花盆里，因为那里住着一朵温柔的小花。蒲公英说："看过全世界才发现，最想去的地方，是你在的地方。"小花害羞地摇了摇花瓣。晚安，愿你梦里也有一个愿意为你停留的人。🌼', 1),
('温柔的晚安魔法', '这个世界上有一种最温柔的魔法，叫做"晚安魔法"。施展这个魔法不需要魔杖，只需要一颗真心。当你听到"晚安"两个字的时候，魔法就生效了——它会帮你赶走所有不开心，用最柔软的云朵包裹你，然后带你进入最甜的梦乡。现在，我要施展魔法了：晚——安——✨魔法已生效！🌙', 1),
('小鲸鱼的歌', '在深深的大海里，住着一只小蓝鲸。小蓝鲸会唱一首特别的歌，这首歌只有最温柔的人才能听到。每天晚上，小蓝鲸都会浮到海面上，对着月亮唱歌。歌声穿过海浪、穿过海风，飘到你的窗前。如果你仔细听，就会发现小蓝鲸唱的是："好——好——睡——觉——做——好——梦——"🐋', 1),
('冰淇淋山的秘密', '在夏天的最深处，有一座冰淇淋山。山是草莓味的，山顶的雪是奶油，山坡上还撒满了彩虹糖碎。看守这座山的是一个戴着甜筒帽的小雪人。小雪人最怕热，但它每天都坚持守在那里，因为它要确保每个好孩子的梦里都有冰淇淋吃。"今天你表现得特别好，"小雪人说，"奖励你一个超级大的圣代！"🍦', 1),
('魔法枕头', '你知不知道你的枕头其实有魔法？每天晚上当你躺下来的时候，魔法枕头就开始工作了。它会把你白天的烦恼都吸走，变成一朵朵小花，然后把这些花编成一个美丽的花环。等你早上醒来的时候，烦恼都不见了，只有花环留在你的枕边，散发着淡淡的香气。所以别担心任何事，把一切都交给魔法枕头吧。🌺', 1),
('会飞的被子', '有一床被子，它有一个梦想——飞上天空。每天晚上，它都会悄悄地练习，一点一点地飘起来。终于有一天，它成功飞起来了！它高兴地带着你一起飞，飞过了城市、飞过了森林、飞到了云层上面。云层上面有一张全世界最舒服的床，被子轻轻把你放在床上，说："今晚就在这里睡吧，我会一直盖着你，不会让你着凉的。"☁️', 1),
('晚安快递员', '每天晚上都有一个看不见的快递员，他专门负责派送"美梦包裹"。他会根据你今天的心情来选择包裹——如果你今天开心，他会送你一个甜蜜的梦；如果你今天难过，他会送你一个治愈的梦；如果你今天生气，他会送你一个让你笑出来的梦。现在快递员已经在路上了，请保持微笑准备签收哦～📦', 1),
('星星糖', '银河里有一家小小的糖果店，专门卖一种叫"星星糖"的糖果。每一颗星星糖都含着一个美好的梦境。粉色的糖会梦见花海，蓝色的糖会梦见大海，金色的糖会梦见阳光。今天店主特地为你挑选了一颗七彩的星星糖——里面有所有美好的东西。放进嘴里，甜甜的，然后闭上眼睛，你的梦就开始了。⭐', 1);

-- ========== 追加10篇睡前故事（共计30篇） ==========
INSERT INTO t_bedtime_story (title, content, source) VALUES
('月亮上的秋千', '在月亮的背面，有一个秘密花园，花园里挂着一个银色的秋千。每天晚上，月亮姐姐都会邀请最善良的小朋友来荡秋千。秋千荡得越高，就能看到越多的美梦在空中飘浮。今天月亮姐姐选中了你，因为你是这个世界上最可爱的人。抓紧秋千的绳子，闭上眼睛，让月亮带你飞进最美的梦境吧。🌙', 1),
('蜜糖小镇', '在很远很远的地方，有一个叫蜜糖的小镇。那里的房子是饼干做的，屋顶是巧克力，街道上流淌着草莓牛奶的小溪。小镇的居民都是可爱的小动物，它们最欢迎笑容灿烂的访客。今天镇长小熊先生说："有一位特别的客人要来！"它们准备了最好吃的蜂蜜蛋糕，等着你来梦里做客呢。🍯', 1),
('会跳舞的萤火虫', '夏天的夜晚，森林里有一群会跳舞的萤火虫。它们每天晚上都会排练一支新的舞蹈，专门为那些睡不着的小朋友表演。领舞的萤火虫叫小光，它是森林里最亮的萤火虫。小光说："今晚的舞蹈叫做'好梦华尔兹'，希望能让你做个甜甜的梦。"闭上眼，萤火虫的舞蹈开始了。🪲', 1),
('云朵面包店', '在天空的最高处，有一家云朵面包店。老板娘是一朵棉花糖一样的云，她做的面包又软又甜，吃一口就会做一个好梦。每天傍晚，她都会把最新鲜的面包放在篮子里，让晚风快递员送到每个好孩子的梦里。今天的面包是草莓味的，上面还撒了星星糖霜。🥐', 1),
('大海的摇篮曲', '大海妈妈每天晚上都会唱摇篮曲。她的声音有时候是轻轻的浪花声，有时候是远远的海鸥叫声，有时候是海风吹过椰子树的声音。大海妈妈说："我的摇篮曲是世界上最古老的歌，听过这首歌的孩子都会做最好最甜的梦。"仔细听，大海妈妈开始唱了。🌊', 1),
('雪精灵的礼物', '在北方的雪山上，住着一个雪精灵。雪精灵的工作是制作美丽的雪花，每一片雪花都是独一无二的。但雪精灵还有一个秘密任务——她会在最乖的小朋友的梦里，偷偷放一片永远不会融化的雪花。这片雪花可以实现一个小小的愿望。今晚，雪精灵正在飞往你的梦里。❄️', 1),
('时光胶囊', '森林里有一棵最老最老的树，树干上有一个小洞，里面藏着很多时光胶囊。每个胶囊里都装着一个美好的回忆。今天你放进去了一个开心的瞬间，老树爷爷说："我会帮你好好保管，等你下次来的时候，它会变得更甜。"在老树爷爷的守护下，所有美好的回忆都会永远存在。🌳', 1),
('梦境画家', '有一个专门画梦境的画家，他每天晚上都会背着他的魔法画板，来到小朋友的窗前。他用最温柔的颜料画出最美的梦境——粉色的云朵、蓝色的海洋、金色的阳光、紫色的花海。今晚画家说："我要为你画一个关于勇气的梦，梦里你是一位勇敢的探险家。"🎨', 1),
('小火车嘟嘟', '有一辆叫嘟嘟的小火车，它的工作不是运货，而是每天晚上运送好梦到每个小朋友的枕头边。嘟嘟的车厢里装满了各种各样的梦——甜的、温暖的、有趣的、冒险的。今晚嘟嘟特意为你准备了一个特别的车厢，里面有一个关于友谊和快乐的故事。呜呜——嘟嘟出发了！🚂', 1),
('晚安，全世界', '当夜幕降临，全世界都开始准备入睡。太阳公公下班了，换月亮婆婆值班。小鸟飞回了巢，小花合上了花瓣，小猫咪蜷成了一个球。每个人都在说晚安——晚安，天空；晚安，大地；晚安，星星；晚安，最特别的你。现在，你也该睡了。闭上眼，全世界都在保护你的梦。🌍', 1);

-- ========== 追加88条真心话问题（共计100条） ==========
INSERT INTO t_truth_question (category, question, source) VALUES
(1, '还记得我们第一次牵手是什么感觉吗？', 1),
(1, '你觉得我从认识你到现在最大的变化是什么？', 1),
(1, '我们之间最让你感动的一件小事是什么？', 1),
(1, '你第一次见我的家人的时候，心里在想什么？', 1),
(1, '你觉得我们的感情在哪一个瞬间让你认定就是我了？', 1),
(1, '我们一起去过的所有地方里，你最喜欢哪里？', 1),
(1, '你觉得我做过最让你惊喜的事情是什么？', 1),
(1, '如果给我们的故事写一本书，书名会是什么？', 1),
(1, '你觉得我们在彼此的生命中扮演了什么角色？', 1),
(1, '有什么是你一直想对我说但没有说出口的话？', 1),
(1, '你保留的关于我们的第一件纪念品是什么？', 1),
(1, '我们经历过的最难的事是什么？是怎么熬过来的？', 1),
(1, '你第一次觉得"这个人就是对的人"是什么时候？', 1),
(1, '如果让你重新选择，你还会选择和我在一起吗？', 1),
(1, '你觉得我们的关系中，最珍贵的东西是什么？', 1),
(1, '哪一个平凡的日子，却让你印象最深？', 1),
(1, '你觉得我什么时候最帅/最美？', 1),
(1, '你最喜欢我穿什么风格的衣服？', 1),
(1, '如果有机会回到过去，你最想回到我们哪一天？', 1),
(1, '你觉得自己在恋爱中学到的最重要的事是什么？', 1),
(2, '如果明天就是世界末日，你今天最想和我做什么？', 1),
(2, '你希望我们五年后的生活是什么样子的？', 1),
(2, '如果我们可以一起去世界上任何一个地方，你想去哪？', 1),
(2, '你觉得我们以后会养宠物吗？养什么？', 1),
(2, '你希望我们老了以后怎样度过每一天？', 1),
(2, '如果我们有了家，你最想布置哪个房间？', 1),
(2, '你最想和我一起学什么新技能？', 1),
(2, '你觉得我们之间还需要更多什么？', 1),
(2, '如果要给我一个惊喜，你觉得会是什么？', 1),
(2, '你希望我为你改掉的三个习惯是什么？', 1),
(2, '你觉得我们的爱情会随着时间变得更深吗？为什么？', 1),
(2, '你未来最想和我一起完成的目标是什么？', 1),
(2, '你梦想中的婚礼/婚礼场景是什么样？', 1),
(2, '如果有超能力，你最想为我做什么？', 1),
(2, '你希望我们一年有多少次旅行？去哪里？', 1),
(2, '你觉得十年后的我们，会和现在有什么不同？', 1),
(2, '如果可以和我互换一天身份，你最想做什么？', 1),
(2, '你觉得什么能让我们的感情永远保持新鲜？', 1),
(2, '你理想中的家是什么样子的？', 1),
(2, '如果要你用三个愿望来改变我们的未来，你会许什么愿？', 1),
(3, '今天哪件小事让你笑了？', 1),
(3, '今天有没有突然想我的瞬间？', 1),
(3, '如果今天的开心程度打分，你打几分？', 1),
(3, '今天吃了什么好吃的？下次一起去吃吧', 1),
(3, '今天的天气让你想到了什么心情？', 1),
(3, '今天有没有人夸你？我也想夸你一句', 1),
(3, '今天遇到的最有趣的事是什么？', 1),
(3, '如果今天是最后一天，你怎么过？', 1),
(3, '今天你为自己做了什么开心的事？', 1),
(3, '今天的你，最想被怎么宠爱？', 1),
(3, '今天最让你有成就感的一件事是什么？', 1),
(3, '今天听了什么好听的歌？推荐给我吧', 1),
(3, '今天最想说的一句话是什么？', 1),
(3, '今天有没有好好吃饭？吃了什么？', 1),
(3, '今天的你比昨天更爱我了吗？', 1),
(3, '今天有没有受委屈？说出来我抱抱你', 1),
(3, '今天看到最好看的风景是什么？拍给我看', 1),
(3, '如果你今天的心情是一种颜色，会是什么？', 1),
(3, '今天有没有什么遗憾？明天我帮你补上', 1),
(3, '今天在街上看到情侣你最羡慕的是什么？', 1),
(3, '今天有没有人让你不开心？我帮你去出气', 1),
(3, '今天看到什么让你想起了我？', 1),
(3, '今天的你开心吗？不开心的话要告诉我哦', 1),
(3, '今天有什么小秘密不能告诉我？', 1),
(3, '我要怎么做才能让你每天都像今天这么开心？', 1),
(3, '今天哪个瞬间你希望我在身边？', 1),
(3, '今天的你收到了什么美好？分享给我吧', 1),
(3, '今天睡觉前想让我对你说什么？', 1);
