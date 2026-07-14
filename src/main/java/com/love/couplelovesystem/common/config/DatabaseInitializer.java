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
 * schema.sql 使用 CREATE TABLE IF NOT EXISTS + INSERT IGNORE，幂等安全。
 * TiDB Cloud 免费版用户可能无 DDL 权限，此时需手动通过控制台建表。
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
            log.info("数据库: {}, 开始检查表结构...", catalog);

            // 先检查表是否已存在
            if (tableExists(conn, catalog, "t_system_config")) {
                log.info("表已存在，跳过初始化");
                return;
            }

            log.info("表不存在，执行 schema.sql ...");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.setContinueOnError(true);  // TiDB 免费版可能无 DDL 权限
            populator.setSeparator(";");
            populator.populate(conn);

            // 再次检查是否成功
            if (tableExists(conn, catalog, "t_system_config")) {
                log.info("数据库初始化完成");
            } else {
                log.error("========================================");
                log.error("自动建表失败！TiDB Cloud 免费版用户无 CREATE 权限。");
                log.error("请通过 TiDB Cloud 控制台 → SQL Editor 手动执行 schema.sql");
                log.error("文件位置: src/main/resources/schema.sql");
                log.error("========================================");
                // 不抛异常，让应用继续启动（手动建表后可正常运行）
            }
        } catch (Exception e) {
            log.error("数据库初始化异常: {}", e.getMessage());
            // 不抛异常 - 允许应用在手动建表后正常运行
        }
    }

    private boolean tableExists(Connection conn, String catalog, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            String sql = String.format(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'",
                catalog, tableName);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (Exception e) {
            log.debug("检查表 {} 是否存在时出错: {}", tableName, e.getMessage());
        }
        return false;
    }
}
