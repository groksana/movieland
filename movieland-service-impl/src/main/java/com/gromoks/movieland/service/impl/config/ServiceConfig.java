package com.gromoks.movieland.service.impl.config;


import com.gromoks.movieland.dao.config.JdbcConfig;
import org.springframework.context.annotation.*;

@Configuration
@Import({JdbcConfig.class, MailConfig.class})
@ComponentScan(basePackages = {"com.gromoks.movieland.service.impl"})
@PropertySource("classpath:service.properties")
public class ServiceConfig {

}
