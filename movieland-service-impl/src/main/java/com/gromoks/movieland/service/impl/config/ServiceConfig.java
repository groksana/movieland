package com.gromoks.movieland.service.impl.config;


import com.gromoks.movieland.dao.config.JdbcConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(basePackages = {"com.gromoks.movieland.service.impl"})
@PropertySource("classpath:web.properties")
public class ServiceConfig {

}
