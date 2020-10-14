package com.huzh.springbootmybatisdynamicdatasource2.common.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author huzh
 * @date 2020/10/13 18:05
 */
@Configuration
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class})
public class DataSourceConfiguration {

    /**
     * 分表数据源名称
     */
    private static final String SHARDING_DATASOURCE_NAME = "sharding";

    @Resource
    private DynamicDataSourceProperties properties;


//    /**
//     * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
//     * 1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
//     * 2. 主从数据源: masterSlaveDataSource;
//     * 3. 脱敏数据源：encryptDataSource;
//     * 4. 影子数据源：shadowDataSource
//     */
//    @Lazy
//    @Resource(name = "shardingDataSource")
//    private DataSource shardingDataSource;


    /**
     * 未使用分片, 脱敏的名称(默认): shardingDataSource
     * shardingjdbc使用了主从: masterSlaveDataSource
     */
    @Lazy
    @Resource(name = "shardingDataSource")
    private DataSource shardingDataSource;

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
                dataSourceMap.put(SHARDING_DATASOURCE_NAME, shardingDataSource);
                //打开下面的代码可以把 shardingjdbc 管理的数据源也交给动态数据源管理 (根据自己需要选择开启)
                //dataSourceMap.putAll(((MasterSlaveDataSource) masterSlaveDataSource).getDataSourceMap());
                return dataSourceMap;
            }
        };
    }

    /**
     * 将动态数据源设置为首选的
     * 当spring存在多个数据源时, 自动注入的是首选的对象
     * 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
     */
    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }
}