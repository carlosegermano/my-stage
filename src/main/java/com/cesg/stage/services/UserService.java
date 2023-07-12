package com.cesg.stage.services;

import com.cesg.stage.model.User;

public interface UserService {

    User saveUser(User user);

    User getLoggedUser();
}
