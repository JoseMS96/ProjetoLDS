package br.fai.lds.api.service.impl;

import br.fai.lds.api.enums.Credentials;
import br.fai.lds.api.service.JwtService;
import br.fai.lds.api.service.UserRestService;
import br.fai.lds.db.dao.UserDao;
import br.fai.lds.model.entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRestServiceImpl implements UserRestService<UserModel> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Override
    public List<UserModel> find() {
        return userDao.find();
    }

    @Override
    public UserModel findById(int id) {

        if (id < 0) return null;

        return (UserModel) userDao.findById(id);
    }

    @Override
    public int create(UserModel entity) {

        return userDao.create(entity);
    }

    @Override
    public boolean update(final int id, UserModel entity) {

        UserModel user = (UserModel) userDao.findById(id);
        if (user == null) return false;

        user.setEmail(entity.getEmail());
        user.setFullName(entity.getFullName());

        return userDao.update(user);
    }

    @Override
    public boolean deleteById(int id) {

        return userDao.deleteById(id);
    }

    @Override
    public UserModel validateLogin(String encodedData) {

        if (encodedData.isEmpty()) {
            return null;
        }

        Map<Credentials, String> credentialsMap = decodeAndGetUsernameAndPassword(encodedData);

        if (credentialsMap == null
                || credentialsMap.size() != 2) {
            return null;
        }

        String username = credentialsMap.get(Credentials.USERNAME);
        String password = credentialsMap.get(Credentials.PASSWORD);

        if (username.isEmpty()
                || password.isEmpty()) {
            return null;
        }

        if (username.length() < 4
                || password.length() < 3) {
            return null;
        }

        UserModel user = userDao.validateUsernameAndPassword(username, password);

        String token = jwtService.getJwtToken(user);
        user.setToken(token);
        user.setPassword(null);

        return user;
    }

    private Map<Credentials, String> decodeAndGetUsernameAndPassword(String encodedData) {

        // Basic GAKDKJA78y487324jksd
        String[] splitData = encodedData.split("Basic ");

        // 0 - Basic
        // 1 - GAKDKJA78y487324jksd
        if (splitData.length != 2) {
            return null;
        }

        String base64Data = splitData[1];
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

        // Username=nomeDoCara;Password=senhaDoCara
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        String[] firstPart = decodedString.split("Username=");

        // 0 - Username=
        // 1 - nomeDoCara;Password=senhaDoCara
        if (firstPart.length != 2) {
            return null;
        }

        String[] credentials = firstPart[1].split(";Password=");

        // 0 - nomeDoCara
        // 1 - senhaDoCara
        if (credentials.length != 2) {
            return null;
        }

        Map<Credentials, String> credentialsMap = new HashMap<>();
        credentialsMap.put(Credentials.USERNAME, credentials[0]);
        credentialsMap.put(Credentials.PASSWORD, credentials[1]);

        return credentialsMap;
    }
}
