package br.fai.lds.api.service;

import br.fai.lds.model.entities.UserModel;

public interface JwtService {

    String getJwtToken(UserModel user);

}
