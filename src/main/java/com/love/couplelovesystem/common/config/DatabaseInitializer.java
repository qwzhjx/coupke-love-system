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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 数据库表初始化 — 应用启动时自动建表 + 插入默认数据
 * <p>
 * 通过 INFORMATION_SCHEMA 检测表是否存在，避免重复初始化。
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
        try (Connection conn = dataSource.getConnection()) {
            String catalog = conn.getCatalog();
            log.info("数据库初始化检查 — 当前数据库: {}", catalog);

            // 检测 t_system_config 表是否存在（限定当前数据库，兼容 TiDB / MySQL）
            boolean needInit = !tableExists(conn, catalog, "t_system_config");

            if (needInit) {
                log.info("表未初始化，开始执行 schema.sql ...");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("schema.sql"));
                populator.setContinueOnError(true);
                populator.setSeparator(";");
                populator.populate(conn);
                log.info("schema.sql 执行完毕");
            } else {
                log.info("数据库表已存在，跳过初始化");
            }
        } catch (Exception e) {
            log.error("数据库初始化失败", e);
            throw new RuntimeException("数据库初始化失败", e);
        }
    }

    private boolean tableExists(Connection conn, String catalog, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            // 限定 TABLE_SCHEMA，避免 TiDB 上跨库误判
            String sql = String.format(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'",
                catalog, tableName);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            log.warn("查询 INFORMATION_SCHEMA 异常: {}", e.getMessage());
        }
        return false;
    }
}
