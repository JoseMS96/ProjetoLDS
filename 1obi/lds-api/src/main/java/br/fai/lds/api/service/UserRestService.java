package br.fai.lds.api.service;

import br.fai.lds.model.entities.UserModel;

public interface UserRestService<T> extends BaseRestService<T> {

    UserModel validateLogin(String encodedData);

}
