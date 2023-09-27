package et.com.movieReview.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties
public class DataSource {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties masterDataSourceProperties() {
        log.info("creating data source 1");
        return new DataSourceProperties();
    }

    @Bean(name = "masterDataSource")
    @LiquibaseDataSource
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public javax.sql.DataSource masterDataSource() {
        HikariDataSource dataSource = masterDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        dataSource.setPoolName("masterDataSource");

        return dataSource;
    }

}
