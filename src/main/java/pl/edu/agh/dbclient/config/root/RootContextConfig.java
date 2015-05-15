package pl.edu.agh.dbclient.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author mnowak
 */
@Configuration
@ComponentScan({"pl.edu.agh.dbclient.components"})
public class RootContextConfig {
}
