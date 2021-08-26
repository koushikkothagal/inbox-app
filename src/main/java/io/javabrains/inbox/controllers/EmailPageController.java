package io.javabrains.inbox.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.InitFolders;

@Controller
public class EmailPageController {

    @Autowired 
    private FolderRepository folderRepository;
    
    @Autowired
	private EmailRepository emailRepository;

    @GetMapping(value = "/email/{id}")
    public String getEmailPage(@PathVariable String id, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null && principal.getAttribute("login") != null) {
            String loginId = principal.getAttribute("login");
            List<Folder> folders = folderRepository.findAllById(loginId);
            List<Folder> initFolders = InitFolders.init(loginId);
                // initFolders.stream().forEach(folderRepository::save);
            model.addAttribute("defaultFolders", initFolders);
            if (folders.size() > 0) {
                model.addAttribute("userFolders", folders);
            }
            try {
                UUID uuid = UUID.fromString(id);
                Optional<Email> email = emailRepository.findById(uuid);
                if (email.isPresent()) {
                    model.addAttribute("email", email.get());
                    return "email-page";
                }
            } catch (IllegalArgumentException e) {
                return "inbox-page";
            }
            
        }
        return "index";
        
    }
    
    
}
