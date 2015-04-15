package pl.edu.agh.dbclient.components;

import org.springframework.stereotype.Component;

/**
 * @author mnowak
 */
@Component
public class CreateServiceImpl implements CreateService {

    @Override
    public int someMethod() {
        return 42;
    }
}
