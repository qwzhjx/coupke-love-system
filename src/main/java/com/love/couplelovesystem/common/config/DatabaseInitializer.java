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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库表初始化 — 仅在新数据库时执行
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
            // 检查是否已经初始化过（t_system_config 表存在且有数据说明已初始化）
            boolean needInit = false;
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'T_SYSTEM_CONFIG'");
                if (rs.next() && rs.getInt(1) == 0) {
                    needInit = true;
                }
            } catch (Exception e) {
                // 表不存在时会进这里（H2 中查询 INFORMATION_SCHEMA 不会抛异常，但以防万一）
                needInit = true;
            }
            // 表存在但为空（首次启动时 schema.sql 未通过 Spring 执行）
            if (!needInit) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM T_SYSTEM_CONFIG");
                    if (rs.next() && rs.getInt(1) == 0) {
                        needInit = true;
                    }
                } catch (Exception e) {
                    needInit = true;
                }
            }

            if (needInit) {
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("schema.sql"));
                populator.setContinueOnError(true);
                populator.setSeparator(";");
                populator.populate(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库初始化失败", e);
        }
    }
}