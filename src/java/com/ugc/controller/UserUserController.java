package com.ugc.controller;

import com.ugc.domain.UserDTO;
import com.ugc.domain.UserUser;
import com.ugc.facade.ExportImportFacade;
import com.ugc.facade.StreamRequestsFacade;
import com.ugc.facade.UserUserDomainFacade;
import com.ugc.view.ViewConstants;
import com.ugc.view.ViewDTO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@Controller
public class UserUserController
{

    private UserUserDomainFacade userUserDomainFacade;
    private StreamRequestsFacade streamRequestsFacade;
    private ExportImportFacade exportImportFacade;


    @Autowired
    public UserUserController(UserUserDomainFacade userDomainFacade, ExportImportFacade exportImportFacade,
                              StreamRequestsFacade streamRequestsFacade) {
        this.userUserDomainFacade = userDomainFacade;
        this.streamRequestsFacade = streamRequestsFacade;
        this.exportImportFacade = exportImportFacade;
    }


    @RequestMapping(value = "/useruser/create", method = RequestMethod.POST)
    public String createUser(
            @RequestParam("userName")String userName, @RequestParam("systemUserName")String systemUserName,
            @RequestParam("password")String password, @RequestParam("json")String json,
            ModelMap map) {

        streamRequestsFacade.countRequest(systemUserName);
        String saveKey = systemUserName + userName;
        if (userUserDomainFacade.isUserExists(saveKey)) {
            ViewDTO view = new ViewDTO();
            view.setTransferObject("this user name is existing in your system");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }
        UserUser user = userUserDomainFacade.createUser(saveKey, json, password);
        ViewDTO viewDTO = new ViewDTO();
        viewDTO.setTransferObject(user);
        map.addAttribute(viewDTO);
        return ViewConstants.CREATE_USER_USER;
    }

    @RequestMapping(value = "/useruser/allusers/(*:clientKey)", method = RequestMethod.GET)
    public String getAllUsers(@RequestParam("clientKey")String clientKey, ModelMap map){

        streamRequestsFacade.countRequest(clientKey);
        List<UserDTO> users = userUserDomainFacade.getAllUsers(clientKey);
        ViewDTO viewDTO = new ViewDTO();
        viewDTO.setCollections(users);
        map.addAttribute(viewDTO);
        return ViewConstants.USER_LIST_VIEW;
        
    }

    @RequestMapping(value = "/user/export/(*:clientKey)/(*:userName)", method = RequestMethod.GET)
    public void export(@RequestParam("userName")String userName, @RequestParam("clientKey")String clientKey, HttpServletResponse res){

        try {

             File file =  exportImportFacade.export(clientKey, userName, SystemConstants.USER_USER_BUKET);
             InputStream in = new FileInputStream(file);
             if (in != null) {
                res.setContentType("application/octet-stream");
                String name = clientKey + ".zip";
                res.setHeader("Content-Disposition", "attachment;filename=" + name);
                OutputStream output = res.getOutputStream();
                IOUtils.copy(in, output);
                output.close();
                in.close();
                file.delete();
            }

        } catch (IOException e) {

        }

    }

    @RequestMapping(value = "/useruser/userExists", method = RequestMethod.POST)
    public synchronized String userExists(@RequestParam("userName")String userName, @RequestParam("systemUserName")String systemUserName, ModelMap map) {

        streamRequestsFacade.countRequest(systemUserName);
        String finalKey = systemUserName + userName;
        UserUser user = userUserDomainFacade.findByUserName(finalKey);
        String message = "OK";
        if (user == null) {
            message = "NO";
        }
        ViewDTO view = new ViewDTO();
        view.setTransferObject(message);
        map.addAttribute(view);
        return ViewConstants.DEFAULT;

    }

    @RequestMapping(value = "/useruser/login", method = RequestMethod.POST)
    public synchronized String loginUser(@RequestParam("userName")String userName, @RequestParam("systemUserName")String systemUserName,
                            @RequestParam("password")String password, ModelMap map) {

        streamRequestsFacade.countRequest(systemUserName);
        String finalKey = systemUserName + userName;
        UserUser user = userUserDomainFacade.findByUserName(finalKey);
        if (user == null){
            ViewDTO view = new ViewDTO();
            view.setTransferObject("user does not exist");
            map.addAttribute(view);
            return ViewConstants.DEFAULT;
        }else{
            if (!password.equals(user.getPassword())){
                ViewDTO view = new ViewDTO();
                view.setTransferObject("password does not match the username");
                map.addAttribute(view);
                return ViewConstants.DEFAULT;
            }
            userUserDomainFacade.updateUserLoginTime(user, password, finalKey);
            ViewDTO viewDTO = new ViewDTO();
            viewDTO.setTransferObject(user);
            map.addAttribute(viewDTO);
            return ViewConstants.USER_LOGIN_VIEW;
        }

    }

    @RequestMapping(value = "/useruser/update/(*:clientKey)", method = RequestMethod.POST)
    public String updateUser(
            @RequestParam("userName")String userName, @RequestParam("clientKey")String systemUserName,
            @RequestParam("json")String json,
            ModelMap map) {

        streamRequestsFacade.countRequest(systemUserName);
        String key = systemUserName + userName;
        userUserDomainFacade.updateUser(key, json);
        ViewDTO view = new ViewDTO();
        view.setTransferObject("OK");
        map.addAttribute(view);
        return ViewConstants.DEFAULT;

    }

}
