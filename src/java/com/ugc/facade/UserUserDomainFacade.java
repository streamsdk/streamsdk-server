package com.ugc.facade;

import com.ugc.domain.UserUser;
import com.ugc.domain.UserDTO;

import java.util.List;

public interface UserUserDomainFacade
{

    public UserUser createUser(String userName, String json, String password);

    public UserUser findByUserName(String name);

    public boolean isUserExists(String userName);

    public void updateUser(String key, String json);

    public void updateUserLoginTime(UserUser user, String password, String objectKey);

    public List<UserDTO> getAllUsers(String clientKey);
}
