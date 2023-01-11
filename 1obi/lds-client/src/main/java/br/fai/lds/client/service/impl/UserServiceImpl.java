package br.fai.lds.client.service.impl;

import br.fai.lds.client.service.RestService;
import br.fai.lds.client.service.UserService;
import br.fai.lds.model.entities.UserModel;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService<UserModel> {

    public UserServiceImpl(HttpSession httpSession, RestService<UserModel> restService, RestTemplate restTemplate) {
        this.httpSession = httpSession;
        this.restService = restService;
        this.restTemplate = restTemplate;
    }

    private HttpSession httpSession;

    //    foi removido o autowired para que a injecao
//    seja feita atraves do construtor
//    @Autowired
    private RestService<UserModel> restService;

    private RestTemplate restTemplate;


    private static final String BASE_ENDPOINT = "http://localhost:8081/api/";

    private String buildEndpoint(String resource) {
        return BASE_ENDPOINT + resource;
    }

    @Override
    public int create(UserModel entity) {

        return restService.post("user/create", entity);


    }

    @Override
    public List<UserModel> find() {
        HttpHeaders requestHeaders = restService.getRequestHeaders(httpSession);

        List<UserModel> userModels = restService.get("user/find", requestHeaders);

        //para testar metodos privados: PowerMock - Reflections

//        for (UserModel user: userModels){
//            user.setUsername("gambiarra-" + user.getUsername());
//        }
//
//        userModels.add(new UserModel());
        return userModels;

//        return restService.get("user/find", requestHeaders);
    }


    @Override
    public UserModel findById(int id) {

        if (id < 0) {
            return null;
        }

        HttpHeaders requestHeaders = restService.getRequestHeaders(httpSession);

        return restService.getById("user/find/" + id, UserModel.class, requestHeaders);
    }

    @Override
    public boolean update(int id, UserModel entity) {
        HttpHeaders requestHeaders = restService.getRequestHeaders(httpSession);

        if (id < 0) {
            return false;
        }

        if (entity == null) {
            return false;
        }

        if (id != entity.getId()) {
            return false;
        }

        return restService.put("user/update/" + id, entity, requestHeaders);
    }

    @Override
    public boolean deleteById(int id) {
        HttpHeaders requestHeaders = restService.getRequestHeaders(httpSession);

        return restService.deleteById("user/delete/" + id, requestHeaders);
    }

    @Override
    public UserModel validateUsernameAndPassword(String username, String password) throws NullPointerException {

        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        HttpHeaders httpHeaders = restService.getAuthenticationHeaders(username, password);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String resource = "account/login";

        ResponseEntity<UserModel> responseEntity = restTemplate.exchange(buildEndpoint(resource),
                HttpMethod.POST, httpEntity, UserModel.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }

        UserModel user = responseEntity.getBody();

        return user;


    }
}
