package com.manjot.snapnote.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuration class for MongoDB auditing.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // additional configuration if needed
}