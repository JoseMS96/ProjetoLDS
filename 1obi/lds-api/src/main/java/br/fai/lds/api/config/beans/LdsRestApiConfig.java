package br.fai.lds.api.config.beans;

import br.fai.lds.db.dao.UserDao;
import br.fai.lds.db.dao.impl.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LdsRestApiConfig {

    @Bean
    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

}
