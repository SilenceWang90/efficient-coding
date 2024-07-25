package com.wp.mongo;

import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangpeng
 * @description MongoConfig
 * @date 2024/7/25 09:27
 **/
@Configuration
public class MongoConfig {
    @Bean
    public GridFSBucket gridFSBucket() {
        return GridFSBuckets.create(MongoClients.create("mongodb://localhost:27017/mydatabase").getDatabase("mydatabase"));
    }
}
