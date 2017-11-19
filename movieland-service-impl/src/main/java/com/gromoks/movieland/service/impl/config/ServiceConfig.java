package com.gromoks.movieland.service.impl.config;


import com.gromoks.movieland.dao.config.JdbcConfig;
import org.springframework.context.annotation.*;

@Configuration
@Import(JdbcConfig.class)
@ComponentScan(basePackages = {"com.gromoks.movieland.service.impl"})
@PropertySource("classpath:web.properties")
public class ServiceConfig {

}
