package com.ugc.controller;

import com.ugc.domain.ObjectListDTO;
import com.ugc.domain.SystemUser;
import com.ugc.domain.UserDTO;
import com.ugc.domain.Statistics;
import com.ugc.facade.StorageDomainFacade;
import com.ugc.facade.SystemUserDomainFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import com.ugc.utils.ConversionUtils;
import com.ugc.helper.FileCreationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@Controller
public class SystemUserController
{

    private SystemUserDomainFacade systemUserDomainFacade;
    private StorageDomainFacade storageDomainFacade;
    private StreamRequestsFacade streamRequestsFacade;
    private FileCreationHelper fch;
    private static Log log = LogFactory.getLog(SystemUserController.class);

    @Autowired
    public SystemUserController(SystemUserDomainFacade systemUserDomainFacade,
                                StorageDomainFacade storageDomainFacade, StreamRequestsFacade streamRequestsFacade, FileCreationHelper fch){
         this.systemUserDomainFacade = systemUserDomainFacade;
         this.storageDomainFacade = storageDomainFacade;
         this.streamRequestsFacade = streamRequestsFacade;
         this.fch = fch;
    }

   @RequestMapping(value = "/systemuser/reset/session/(*:appId)", method = RequestMethod.POST)
   public String resetSession(HttpServletRequest request, @RequestParam("appId")String id, ModelMap map){
       request.getSession(true).setAttribute("appId", id);
       Statistics statistics = systemUserDomainFacade.getStatistics(id);
       request.getSession().setAttribute("sDate", "From " + statistics.getCurrentDate() + " ---- current");
       request.getSession().setAttribute("totalRequests", statistics.getTotalRequests());
       request.getSession().setAttribute("totalPush", statistics.getTotalPush());
       request.getSession().setAttribute("storage", statistics.getTotalStorage());
       ViewDTO view = new ViewDTO();
       view.setTransferObject("OK");
       map.addAttribute(view);
       if (log.isInfoEnabled()){
           log.info("reset session for switching application");
       }
       return ViewConstants.DEFAULT;
   }

    @RequestMapping(value = "/systemuser/logout/session", method = RequestMethod.POST)
    public String logoutSession(HttpServletRequest request, ModelMap map){
        request.getSession(true).setAttribute("clientKey", null);
        request.getSession(true).setAttribute("secretKey", null);
        request.getSession(true).setAttribute("appId", null);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        return ViewConstants.DEFAULT;
    }

    @RequestMapping(value = "/systemuser/load/application/(*:clientKey)", method = RequestMethod.GET)
    public String loadApplications(@RequestParam("clientKey")String clientKey, ModelMap map){

        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.APPLICATION_BUKET, "", clientKey);
        ViewDTO view = new ViewDTO();
        view.setCollections(objects);
        view.setTransferObject(clientKey);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("load applications for client key " + clientKey);
        }
        return ViewConstants.OBJECT_LIST_VIEW;

    }


    @RequestMapping(value = "/systemuser/create/application/(*:clientKey)", method = RequestMethod.POST)
    public String createApplication(HttpServletRequest request, @RequestParam("clientKey")String clientKey, ModelMap map){

        streamRequestsFacade.countRequest(clientKey);
        String appId = systemUserDomainFacade.createApplication(clientKey);
        request.getSession(true).setAttribute("appId", appId);
        ViewDTO view = new ViewDTO();
        view.setTransferObject(appId);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("create new application with app id " + appId);
        }
        return ViewConstants.DEFAULT;
        
    }

    @RequestMapping(value = "/systemuser/create", method = RequestMethod.POST)
    public String createSystemUser(@RequestParam("userName")String userName,
            @RequestParam("password")String password,    HttpServletRequest request,
             ModelMap map) {

        if (storageDomainFacade.isKeyExists(userName, SystemConstants.SYSTEM_WEBUSER_BUKET)){
             ViewDTO view = new ViewDTO();
             view.setTransferObject("This username already exists, please use a different username");
             map.addAttribute(view);
             return ViewConstants.DEFAULT;
        }

        SystemUser user = systemUserDomainFacade.createUser(userName, password);
        ViewDTO view = new ViewDTO();
        view.setTransferObject(user);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("create system user with new system user name" + userName);
        }
       request.getSession().setAttribute("sDate", "From " + systemUserDomainFacade.getStatisticsDate() + " ---- current");
       request.getSession().setAttribute("totalRequests", 0);
       request.getSession().setAttribute("totalPush", 0);
       request.getSession().setAttribute("storage", 0);
       return ViewConstants.CREATE_SYSTEM_USER_VIEW;

    }

    @RequestMapping(value = "/systemuser/signin", method = RequestMethod.POST)
    public String authenticateSystemUser(
                @RequestParam("clientKey")String clientKey,
                @RequestParam("secretKey")String secretKey,
                @RequestParam("applicationId")String appId,
                ModelMap map) {

        boolean isOK = systemUserDomainFacade.authenticate(clientKey, secretKey);
        if (!isOK){
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The clientKey and secretKey are not valid pair.");
            map.addAttribute(view);
             if (log.isInfoEnabled()){
                log.info("failed auth system user through mobile client for client key" + clientKey + " reason: the clientKey and secretKey are not valid pair.");
             }
            return ViewConstants.DEFAULT;
        }
        boolean foundApp = false;
        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.APPLICATION_BUKET, "", clientKey);
        for (ObjectListDTO object : objects) {
             String fullKey = object.getId();
             String id = fullKey.replace(clientKey, "");
             if (appId.equals(id))
                 foundApp = true;
        }
        if (!foundApp){
            ViewDTO view = new ViewDTO();
            view.setTransferObject("Invalid Application ID.");
            map.addAttribute(view);
             if (log.isInfoEnabled()){
                log.info("failed auth system user through mobile client for client key" + clientKey + " reason: invalid Application ID.");
             }
            return ViewConstants.DEFAULT;
        }

        SystemUser user = new SystemUser();
        user.setLoginTime(System.currentTimeMillis());
        ViewDTO viewDTO = new ViewDTO();
        viewDTO.setTransferObject(user);
        map.addAttribute(viewDTO);
        if (log.isInfoEnabled()){
           log.info("successully auth system user through mobile client for client key" + clientKey);
        }
        return ViewConstants.SYSTEM_USER_VIEW;

    }

     @RequestMapping(value = "/systemuser/signin/web", method = RequestMethod.POST)
    public String authenticateSystemUserFromWeb(
                HttpServletRequest request,
                @RequestParam("userName")String userName,
                @RequestParam("password")String password,
                ModelMap map) {

       UserDTO user = systemUserDomainFacade.authenticateFromWeb(userName, password);
        if (user == null){
            ViewDTO view = new ViewDTO();
            view.setTransferObject("The userName/password is not valid.");
            map.addAttribute(view);
            if (log.isInfoEnabled()){
                log.info("failed auth system user through web for user" + userName + " reason: the userName/password is not valid..");
            }
            return ViewConstants.DEFAULT;
        }
        List<ObjectListDTO> objects = storageDomainFacade.getObjectList(SystemConstants.APPLICATION_BUKET, "", user.getClientKey());
        ObjectListDTO o1 = objects.get(0);
        user.setAppId(o1.getId());
        ViewDTO view = new ViewDTO();
        view.setTransferObject(user);
        map.addAttribute(view);
        if (log.isInfoEnabled()){
           log.info("successully auth system user through web for user " +  userName);
        }
       String id = user.getAppId().replace(user.getClientKey(), "");

       Statistics statistics = systemUserDomainFacade.getStatistics(id);
       request.getSession().setAttribute("sDate", "From " + statistics.getCurrentDate() + " ---- current");
       request.getSession().setAttribute("totalRequests", statistics.getTotalRequests());
       request.getSession().setAttribute("totalPush", statistics.getTotalPush());
       request.getSession().setAttribute("storage", statistics.getTotalStorage());

       return ViewConstants.SYSTEM_USER_LOGINVIEW;

    }

    @RequestMapping(value = "/systemuser/questions/post", method = RequestMethod.POST)
       public String postComments(
                   HttpServletRequest request,
                   @RequestParam("name")String name,
                   @RequestParam("email")String email,
                   @RequestParam("comments")String comments,
                   HttpServletResponse res,
                   ModelMap map) {

        String ques = buildQues(name, email, comments);
        String key = generateUniqueId();
        storageDomainFacade.saveObject(ques, SystemConstants.QUESTIONS, key);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        String url = fch.getRedirectUrl();
        try {
            res.sendRedirect(url);
        } catch (IOException e) {
            
        }
        return ViewConstants.DEFAULT;


    }

    private String buildQues(String name, String email, String comments){

        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name + "\n");
        sb.append("email: " + email + "\n");
        sb.append("comments: " + comments + "\n");
        return sb.toString();

    }

     private String generateUniqueId() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        return getHashValue(uid);
    }


    private String getHashValue(String uid) {

        try {
            return ConversionUtils.createMD5Hash(uid, true);
        } catch (NoSuchAlgorithmException e) {

        }
        return "";

    }

}
