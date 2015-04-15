package pl.edu.agh.dbclient;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author mnowak
 */
@Configuration
@ComponentScan("pl.edu.agh.dbclient")
@EnableWebMvc
public class WebConfig {
}
