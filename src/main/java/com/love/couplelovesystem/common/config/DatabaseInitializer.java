package com.love.couplelovesystem.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据库表初始化 — 应用启动时自动建表 + 插入默认数据
 * <p>
 * schema.sql 使用 CREATE TABLE IF NOT EXISTS + INSERT IGNORE，幂等安全，可重复执行。
 * ORDER 设为最高优先级，确保在其他 @EventListener 之前执行。
 */
@Component
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        try {
            log.info("========================================");
            log.info("开始执行数据库初始化 (schema.sql) ...");
            log.info("========================================");

            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.setContinueOnError(false);
            populator.setSeparator(";");
            populator.populate(dataSource.getConnection());

            log.info("========================================");
            log.info("数据库初始化完成 ✓");
            log.info("========================================");
        } catch (Exception e) {
            log.error("========================================");
            log.error("数据库初始化失败！", e);
            log.error("========================================");
            throw new RuntimeException("数据库初始化失败", e);
        }
    }
}
