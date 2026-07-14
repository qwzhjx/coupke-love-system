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
INSERT IGNORE INTO t_system_config (config_key, config_value) VALUES
('access_password', '5201314'),
('page_slogan', '🎀 Hello Kitty · 专属宠爱只属于你 🎀'),
('boy_nickname', '宝宝'),
('girl_nickname', '女朋友'),
('love_start_date', '2024-01-01'),
('theme_color', '#F8A5C2');

-- 系统内置睡前故事（5篇）
INSERT IGNORE INTO t_bedtime_story (title, content, source) VALUES
('小兔子的睡前故事', '从前，在一片美丽的大森林里，住着一只可爱的小兔子。每天晚上，小兔子都会望着天空中的星星，想着远方的那只小兔子。她们虽然相隔很远，但每晚都会在同一时间看着同一片星空，心里暖暖的。小兔子知道，不管距离多远，真心相爱的两颗心永远都在一起。晚安，我最爱的小兔子。🌟', 1),
('最温柔的月光', '你知道吗？月亮之所以那么温柔，是因为它承载了太多人的思念。今晚的月光特别美，我想把最美的月光都收集起来，送给你。当你闭上眼睛的时候，月光会轻轻落在你的脸上，就像我的吻一样温柔。睡吧，我的宝贝，明天醒来又是一个有我的好天气。💤', 1),
('星星与大海的约定', '在遥远的海边，有一颗最亮的星星和一片最蓝的大海。星星每天晚上都会把光芒洒在海面上，大海则用温柔的波浪回应它。它们约定好，无论多少个夜晚过去，都要这样彼此陪伴。就像我们一样，无论经历什么，都会一直一直在一起。晚安，我的星星。✨', 1),
('永远在一起的小熊', '有一只小熊，它有一个最心爱的蜂蜜罐子。但它最喜欢的事情不是吃蜂蜜，而是每天抱着罐子睡觉。因为罐子里装满了它对另一只小熊的想念。每一滴蜂蜜都是一句"我爱你"，甜到心里去。所以每晚入睡前，小熊都会说："明天也要比今天更爱你一点哦。"🐻', 1),
('花与蝴蝶的梦', '花园里有一朵最美的花，每天都会有一只蝴蝶飞来陪它。蝴蝶告诉花说："世界很大，但我只想停在你的花瓣上。"花笑了，花瓣轻轻摇动。它们一起做了一个很甜很甜的梦，梦里有花、有蝴蝶、还有永远不会结束的春天。晚安，愿你梦里有我。🦋', 1);

-- 系统内置真心话问题
INSERT IGNORE INTO t_truth_question (category, question, source) VALUES
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

-- 更多睡前故事（可爱诙谐风）
INSERT IGNORE INTO t_bedtime_story (title, content, source) VALUES
('月亮上的奶茶店', '月亮背面有一家只开在深夜的奶茶店，老板是一只胖胖的月兔。它的招牌是"安眠波波茶"，喝一口眼皮就开始打架。月兔说："这杯免费送给你，因为你今天也很努力啦~"喝完这杯魔法奶茶，你倒头就睡，梦里还有珍珠在蹦迪。晚安，吸溜~🧋', 1),
('恐龙宝宝的烦恼', '有一只小恐龙，它的脖子太长了，每天睡觉都找不到合适的枕头。它试过石头、木头、云朵，全都不行。最后它想到一个好办法——把脖子盘成一个圈，自己枕着自己睡！第二天脖子打结了，被朋友们笑了三天。但它说："值了，那是我睡得最香的一晚！"🦕', 1),
('会下糖果雨的云', '天空有一朵特别的云，别家下雨它下糖果。小朋友们天天在它下面等。有一次它感冒了，打了个喷嚏，下了一地棉花糖。小动物们乐疯了，在棉花糖里打滚。云朵说："别吃太多哦，不然睡不着觉~"于是它降下温牛奶味的雨，大家喝完都乖乖睡了。☁️🍬', 1),
('失眠的小绵羊', '有一只小绵羊，它数羊数到第250只的时候发现——自己就是羊！它懵了："那我算了吗？不算？要重来？"于是它从1开始重新数，数到第1000只的时候天亮了。第二天晚上它学聪明了："今天数奶牛！"结果草原上的奶牛全失眠了。后来大家开了一个会，决定——谁都不许数了，一起睡！🐑', 1),
('爱吃晚安的小精灵', '有一个小精灵，它别的什么都不吃，专吃"晚安"两个字。每天晚上它在城市上空飞来飞去，收集大家说的晚安。有的晚安甜甜的（加了emoji🥰），有的晚安淡淡的（加班到没力气😮‍💨），还有的晚安辣辣的（刚吵完架😤）。小精灵把收集到的晚安熬成一锅汤，洒在月亮上面。于是月亮就变得好亮好温柔，照得所有人都能在梦里和好。晚安，今天你的晚安是什么味道的？🧚', 1),
('被窝是一种结界', '科学研究表明（科学家是一只肥猫），被窝是一种远古结界。只要你钻进被窝，手机放旁边，结界就自动激活。外面的世界——作业、工作、早起闹钟——全都进不来！但结界有一个致命 bug：如果你在被窝里玩手机超过 30 分钟，结界会反向作用，让你第二天顶着两个黑眼圈出门。所以今天的正确用法是：钻进被窝，闭上眼睛，让结界带你进入梦乡。🛌✨', 1),
('企鹅的睡眠课', '南极洲有一只企鹅教授，专门教小企鹅如何快速入睡。它总结了三个步骤：第一步，抖抖翅膀（放松身体）；第二步，蹭蹭旁边的企鹅（增进感情）；第三步，把脑袋埋在翅膀下面（创造黑暗环境）。学生们学了之后效果很好，只有一只小企鹅投诉："老师，我蹭错企鹅了，被扇了一巴掌，更清醒了。"教授说："那你就换个方向蹭嘛！"🐧', 1),
('一颗不想被吃掉的安眠药', '有一粒白色的小药丸，它刚出厂的时候以为自己要去治头痛。结果发现自己被归类为"安眠药"，要去哄人睡觉。它很不高兴："我又不是睡前故事！我要去拯救偏头痛！"于是它从药瓶里偷偷滚出来，滚进了厨房，被误认为是一粒米，下到了锅里。最后它变成了一粒饭，被端上餐桌——然后它发现，吃饱饭的人更容易睡着。于是它悟了："原来哄睡是用碳水！"🍚💊', 1),
('月亮说它想翘班', '有一天月亮对太阳说："哥们，我今天不想上班了，你能帮我代个班吗？"太阳说："行啊！"然后那天晚上变成了白天。小动物们疯了：猫头鹰找不到方向撞了树，萤火虫觉得自己的光不亮了很自卑，蝙蝠忘了自己是倒着睡的摔了一地。月亮在远处看着笑出了声，然后赶紧跑回来："对不起对不起，我还是上班吧，这世界没我真不行。"大家终于能睡了。🌙😅', 1),
('吃梦的妖怪', '有一只小妖怪，它的工作是吃掉噩梦。每天晚上它蹲在你的枕头旁边，看到噩梦要来就一口吞掉。它最喜欢的噩梦是"被大怪兽追"口味，嘎嘣脆；最讨厌"考试没带笔"口味，嚼起来像泡沫塑料。有一次它吃了一个"迟到了""牙掉了""电梯坠落"三重口味拼盘，消化不良放了一个嗝——那个嗝飘到你梦里，变成了一串彩虹。你梦到自己在彩虹上滑滑梯，笑醒了。小妖怪说："不客气！"👾🌈', 1),
('枕头悄悄话', '你知道吗？枕头每天晚上都在听你说梦话，然后悄悄告诉被子。被子再告诉床垫，床垫告诉地板，地板告诉整栋楼。所以你昨晚说"我要吃小龙虾"的梦话，今早整栋楼都知道了。二楼的老王甚至给你门口放了一张小龙虾优惠券。今晚请对你的枕头好一点，说不定它会偷偷帮你实现一个梦里的愿望。🛏️🤫', 1),
('披星戴月外卖员', '天庭有一家外卖店，专门送"好梦外卖"。骑手是一只穿着黄色背心的小麒麟。它每天晚上的订单包括：一份"甜蜜恋爱梦"（加辣椒）、两杯"发财梦"（去冰）、一个"裸考满分梦"（备注：please please please🙏）。小麒麟骑着闪电，挨家挨户送。送到你家的时候，它发现你的窗户关着，于是把梦从门缝里塞进来了。所以你的梦有一点点的云朵味和星光味。祝用餐愉快。🦄🌟', 1),
('小熊的失眠电台', '森林里有一头小熊，开了一个深夜电台，专门给失眠的小动物放故事。今晚的节目是"当你有三个紧急任务但一个都不想干的时候该怎么办"。热线打爆了，全是小动物们的哭诉。小熊淡定地说："我的建议是——关掉手机，对自己说'没关系我明天也能做'，然后闭上眼睛。"信号突然断了。后来大家才知道，小熊自己先睡着了。🎙️🐻', 1),
('一片叶子的前半生', '有一片叶子，春天的时候是嫩绿的，夏天变成深绿色。秋天来了，它变成金黄色，从树上飘下来。它飘啊飘，飘过了小溪，飘过了灌木丛，最后精准地落在一个人的头发上。那个人浑然不知，在楼下走了三圈然后回到家里。他的女朋友看到哈哈大笑："你头上有一片叶子！"叶子想：我这一生的终点，居然是贡献了一个笑话。值得。🍂😆', 1),
('会说话的被套', '有一床被套，它默默无闻地套着被子很多年。直到有一天它开口说话："我最讨厌别人在我里面放屁。"被子很委屈："又不是我自己要放的！"枕头说："别吵了，你们都是二手的。"床单冷笑："起码我还被太阳晒过，你们呢？"床板吼了一声："都给我闭嘴睡觉！"于是卧室安静了。你看，连家具都懂得及时止损的道理。🛏️', 1),
('不熬夜仙女协会', '天界有一个"不熬夜仙女协会"，会员们每天晚上11点准时睡觉。只有一个小仙女，每天晚上刷天庭短视频刷到凌晨三点——看别的仙女施魔法变装、看角木蛟和奎木狼的CP剪辑、看太上老君的炼丹教程。第二天化了一脸仙粉才遮住黑眼圈。协会会长很生气，罚她去给月亮当三天助手。月亮每天10点就下班，小仙女被迫早睡，三天后皮肤好到发光。从此她成了协会的荣誉会长。🧚‍♀️✨', 1),
('一根面条的孤独', '有一根意大利面，它从锅里逃出来，因为它不想被吃掉。它一个人躲在碗底，看着其他面条被叉子卷走。最后锅里的面条都走光了，只剩它一根。"好无聊啊，"它说，"被吃掉也没那么可怕。"这时候厨师发现了它，把它捞起来，和番茄酱、肉丸一起装进便当盒。它在新盒子里交到了朋友——一颗话很多的豌豆。豌豆说："你看起来很好吃耶！"面条笑了："谢谢，你也是。"🍝', 1),
('小闹钟罢工记', '有一个小闹钟，每天早上7点准时响。但它最近不想上班了，因为主人天天按它然后继续睡。"我都叫了，你不起，这不是浪费我吗？"于是它决定罢工，第二天没响。主人睡到自然醒，迟到了，被扣了工资。主人愤怒地瞪着闹钟，闹钟无辜地指着手表："你看，时间在你心里，不需要我提醒。"主人被哲学到了，决定今晚早点睡。闹钟心想：嘿，这招有用！⏰', 1),
('困了星球', '在宇宙深处有一个"困了星球"，上面所有生物都在打哈欠。星球的大气层是咖啡因做的——但大家都免疫了。国王是一只永远睡不够的考拉，王后是一只每天要睡18小时的猫。他们的国歌是哈欠合唱，国旗是一个枕头图案。有一天地球人发射了一枚火箭到这颗星球，宇航员登陆后说的第一句话是："我……也觉得……好困……"然后就倒下了。最后被外星人用被子裹好送回地球，还附了一张纸条："下次带床垫来。"🦥', 1),
('一颗糖果的使命', '货架上有一颗草莓牛奶糖，它坚信自己被人类买走是为了"治愈不开心"。但其实买它的人只是凑单满减。不过没关系！当那个女孩打开糖纸，把糖果放进嘴里的时候，她确实笑了一下。糖果很满足："你看吧，我就说我是来治愈她的！"收银小票在垃圾桶里默默吐槽："你只是凑单的。"糖纸飞走了，带着草莓的余香，去追彩虹了。🍓', 1),
('会飞的床', '有一张床，它有一个秘密——它会在主人睡着以后悄悄飞起来。每天晚上它载着主人飞过城市、飞过大海、飞过雪山。有一次飞得太远，差点被卫星拍到。还有一次撞到了圣诞老人的雪橇，被罚款了两颗驯鹿粮。但主人从来不知道这件事，因为床总是在天亮之前飞回来。主人只奇怪一件事：为什么每天早上头发里都有企鹅毛？🛫🛏️', 1),
('被窝里的星空', '有一个小朋友（其实就是你），每天晚上都要看一会儿星星才能睡着。可是今天是阴天，乌云把星星全遮住了。小朋友不高兴。被窝感觉到了，于是悄悄把被子掀开一条缝——里面居然有一整片星空！原来被窝是一个传送门，直通银河！小朋友钻进被窝，在星星之间游泳，和流星赛跑，还揪了一片星云当被子。第二天醒来，手里还攥着一颗小小的星星石头。✨🌟', 1),
('一只鞋的旅行', '有一只左脚的拖鞋，被主人的猫叼走了。鞋很生气："我要陪主人走天涯的！"猫把它叼到了猫窝、厨房、阳台、马桶旁边。鞋经历了一天的冒险，最后被一只蟑螂骑着回了卧室。主人醒来发现鞋在床底下，一脸问号。鞋说："你永远不知道我经历了什么。"另一只右脚的鞋淡定地说："那你还爱不爱主人了？"左脚鞋说："当然爱，明天继续！"🩴🐱', 1),
('月光信使', '月亮派了一束月光来到你的窗前。月光悄悄钻进你的房间，看见你已经睡着了。月光有点遗憾（因为它跑了38万公里来看你），但它没有叫醒你。它把你的呼吸声收集起来，装进一个小瓶子里，带回月亮上去。月亮每天晚上都在听地球上大家呼吸的声音——你的呼吸声，是月亮最喜欢的那一首。所以每天晚上都有一束月光专门为你而来。你发现了吗？💫', 1),
('瞌睡虫快递', '天庭近来开通了一项新业务：瞌睡虫快递。瞌睡虫是一种比芝麻还小的虫子，专门往人眼皮上爬。一只瞌睡虫趴在你右眼皮上，你打了个哈欠。第二只趴在你左眼皮上，你又打了个哈欠。第三只还没爬到，你的眼睛就闭上了。于是第三只虫失业了。它很不开心，爬到你的嘴角，让你的嘴角上扬——于是你睡着了还在微笑。晚安，做个好梦。🐛😊', 2),
('洗衣机里的袜子哲学', '有一只袜子，它每天都被塞进洗衣机里转啊转。它很郁闷："我为啥要被转来转去？"另一只袜子说："这叫锻炼，转多了就不晕了。"第三只袜子（已经失踪了很久）的声音飘过来："至少你们还在洗衣机里，我被猫叼去藏了三个月今天才见天日！"洗衣机停了下来，三只袜子被晾在阳台上，晒着月亮，聊了一整晚。它们一致认为：生活就是旋转、晕眩、然后被晾干。晚安，不要想太多~🧦', 2),
('会打呼噜的云朵', '天上有一朵特别会打呼噜的云。别的云下雨的时候，它下呼噜声。地上的人听到"呼~噜~呼~噜~"，以为是打雷了，但天气预报说是晴天。小动物们无所谓，它们把云朵的呼噜声当白噪音，睡得比平时还香。有一些失眠的动物专门跑到这朵云下面搭窝。云朵表示压力很大："我就打个呼噜，居然成了网红。"有一天它不打呼噜了，大家反而睡不着了，纷纷投诉。云朵只好加班打呼噜。⛅💤', 2),
('便当盒里的悄悄话', '你明天的午饭在冰箱里正在开一个秘密会议。米饭说："我希望明天被吃掉的时候，她能配着汤汁一起。"鸡腿说："我希望她吃我的时候不要看手机，专心地吃。"小番茄说："我希望她吃了我以后心情变好，下午继续加油。"只有芥末说："我不想被吃掉！我太辣了！"所有食物沉默了两秒，然后米饭说："你是用来调味的，不是用来吃的。"芥末：？？？晚安，明天好好吃饭哦~🍱', 2),
('一颗想变成薄荷糖的冰块', '冰箱里有一块冰块，它不想做冰块了，想做薄荷糖。"冰块太冷了，没人喜欢，"它说，"薄荷糖清清凉凉还提神。"旁边的可乐罐说："你已经是清爽的代名词了好吧。"雪糕说："没你我都得化。"冰块想了想，好像也有道理。但它还是朝薄荷糖的方向努力——它自己拿了一根牙签戳了个洞，放了一片薄荷叶进去。虽然变成了"薄荷味冰块"，但它很开心。做自己，但加点料也不错~🧊', 2),
('话痨小台灯', '书桌上有一盏小台灯，它最大的问题就是话太多。每天晚上你一坐下，它就开始："你作业写了吗？""别看手机了，书还没翻呢。""已经十点了！""你那个PPT做到第几页了？"你烦得不行，把开关一拧。世界安静了。但过一会儿你觉得太安静了，又打开。小台灯刚要说话，你说："别说话，就亮着。"小台灯于是一句话不说，只温柔地亮着光。它第一次知道，陪伴不需要出声。💡', 2),
('被蚊子咬了的烦恼', '一只蚊子在你耳边嗡嗡嗡。你翻身，它飞走。你翻身回去，它又嗡嗡嗡。你没睡好。蚊子也没睡好——因为你的花露水味道太冲。蚊子回家跟老婆说："今天那个人的血是花露水味的，我没下得去口。"蚊子老婆说："我就说那个人不好对付，你偏不信。"蚊子叹气："明天换一家。"所以——你涂了花露水，蚊子不咬你了，你睡得香了。花露水，永远的神。🦟😴', 2),
('自动关灯魔咒', '传说在很久很久以前（其实就是上个月），宇宙颁布了一条魔咒：每天晚上闭上眼睛三分钟，房间的灯就会自动关掉。你信吗？不信。你试了。没关。你骂骂咧咧地自己起来关灯。然后你发现——刚才那三分钟，你已经快要睡着了。宇宙说："你看吧，魔咒其实是让你快速入睡，省得你起来关灯又不困了。"你沉默，然后觉得宇宙说得有道理。晚安，这次真的关灯吧。🔮', 2),
('一只不喝咖啡的小白熊', '北极有一只小白熊，它不喝咖啡，只喝热牛奶。别的熊说它不够酷："成年熊都喝咖啡！"小白熊不以为然。有一天，咖啡供应商出了问题，所有熊都失眠了——戒断反应。只有小白熊每天准点睡觉，皮肤好好，毛发光亮。失眠熊们纷纷来请教秘诀。小白熊说："热牛奶，永远的神。"现在北极流行起了睡前一杯奶，咖啡店改成了奶茶店。白熊成了商会会长。☕🥛', 2),
('十万个为什么里的晚安', '一个小朋友（据说就是你）每天睡前要问十万个为什么才肯睡。今天的问题包括："为什么天是蓝的？为什么月亮不会掉下来？为什么影子会跟着我？为什么被窝越睡越暖？"当你问到第99999个的时候，回答你的人已经睡着了——那没关系。因为第100000个问题是："明天还会爱我吗？"答案是肯定的。永远肯定。明天见也是。💙', 2);

-- 默契测试题（默认10题）
INSERT IGNORE INTO t_chemistry_question (question, opts, answer) VALUES
('宝宝最喜欢的颜色？', '["粉色","蓝色","黑色","白色"]', 0),
('宝宝最爱的食物？', '["火锅","烧烤","甜点","家常菜"]', 2),
('宝宝最想去旅行的地方？', '["海边","山里","城市","古镇"]', 0),
('宝宝生气时会做什么？', '["不说话","狂吐槽","吃东西","睡觉"]', 0),
('宝宝最喜欢的季节？', '["春天","夏天","秋天","冬天"]', 0),
('宝宝约会最喜欢的活动？', '["看电影","逛街","吃饭","宅在家"]', 2),
('宝宝最看重的品质？', '["诚实","幽默","上进","温柔"]', 0),
('宝宝的星座？', '["白羊座","金牛座","双子座","其他"]', 3),
('宝宝最喜欢的饮品？', '["奶茶","咖啡","果汁","白水"]', 0),
('宝宝睡觉前最常做什么？', '["刷手机","看书","聊天","秒睡"]', 3);
