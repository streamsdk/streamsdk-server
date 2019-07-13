package com.ugc.facade;

import com.ugc.domain.Statistics;
import com.ugc.domain.SystemUser;
import com.ugc.domain.UserDTO;

public interface SystemUserDomainFacade
{

    public SystemUser createUser(String userName, String password);

    public boolean isUserExists(String userName);

    boolean authenticate(String clientKey, String secretKey);

    public String createApplication(String clientKey);

    public UserDTO authenticateFromWeb(String userName, String password);

    Statistics getStatistics(String appId);

    String getStatisticsDate();
}
