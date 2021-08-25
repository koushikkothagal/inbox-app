package io.javabrains.inbox.inboxpage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.InitFolders;

@Controller
public class InboxPageController {

    @Autowired FolderRepository folderRepository;

    @GetMapping(value = "/")
    public String getHomePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null && principal.getAttribute("login") != null) {
            String loginId = principal.getAttribute("login");
            List<Folder> folders = folderRepository.findAllById(loginId);
            if (folders.size() > 0) {
                model.addAttribute("folders", folders);
            } else {
                List<Folder> initFolders = InitFolders.init(loginId);
                initFolders.stream().forEach(folderRepository::save);
                model.addAttribute("folders", initFolders);
            }

            return "inbox-page";
        }
        return "index";
        
    }
    
    
}
