package com.love.couplelovesystem.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据库初始化（H2兼容）
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    @Value("${spring.sql.init.mode:never}")
    private String initMode;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        if ("always".equals(initMode)) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.setContinueOnError(true);
            populator.setSeparator(";");
            populator.execute(dataSource);
        }
    }
}