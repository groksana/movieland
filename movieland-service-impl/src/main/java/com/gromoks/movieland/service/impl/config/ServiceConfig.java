package com.gromoks.movieland.service.impl.config;


import com.gromoks.movieland.dao.config.JdbcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(basePackages = {"com.gromoks.movieland.service.impl"})
public class ServiceConfig {
}
