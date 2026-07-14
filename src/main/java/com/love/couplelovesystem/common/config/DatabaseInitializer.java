package com.love.couplelovesystem.common.config;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 数据库表初始化 — 在所有业务 Bean 之前执行
 */
@Component
public class DatabaseInitializer {

    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.setContinueOnError(true);
            populator.setSeparator(";");
            populator.populate(conn);
        } catch (Exception e) {
            throw new RuntimeException("数据库初始化失败", e);
        }
    }
}