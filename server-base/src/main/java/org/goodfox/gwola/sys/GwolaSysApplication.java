package org.goodfox.gwola.sys;

import org.goodfox.gwola.util.persistence.BaseRepositoryImpl;
import org.goodfox.gwola.util.utils.DataSourceUtils;
import org.goodfox.gwola.util.utils.SpringContextHolder;
import org.goodfox.gwola.util.utils.SwaggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.goodfox.gwola", repositoryBaseClass = BaseRepositoryImpl.class)
@EntityScan("org.goodfox.gwola")
@EnableSwagger2
@SpringBootApplication
public class GwolaSysApplication {

    private static final Logger logger = LoggerFactory.getLogger(GwolaSysApplication.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GwolaSysApplication.class, args);
        SpringContextHolder.setApplicationContext(applicationContext);
        logger.info("Registry ApplicationContext");
    }

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        return DataSourceUtils.createDruidDataSource(environment);
    }

    @Bean
    public Docket docket() {
        return SwaggerUtils.initDocket();
    }

}
