package com.motadata.itsm.service;

import com.motadata.itsm.entity.User;

public interface UserService {

    User getUser(Long id);

    User saveUser(User user);

    String deleteUser(Long id);

    User updateUser(User user, Long id);

}
