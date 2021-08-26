package io.javabrains.inbox.controllers;

import java.util.Date;
import java.util.List;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.emailslist.EmailsList;
import io.javabrains.inbox.emailslist.EmailsListRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.InitFolders;

@Controller
public class EmailPageController {

    @Autowired
	private EmailRepository emailRepository;

    @GetMapping(value = "/email/{id}")
    public String getHomePage(@RequestParam(required = false) String folder, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null && principal.getAttribute("login") != null) {
           
        }
        return "index";
        
    }
    
    
}
