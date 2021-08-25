package io.javabrains.inbox.folders;

import java.util.Arrays;
import java.util.List;

import com.datastax.oss.driver.api.core.uuid.Uuids;

public class InitFolders {
    public static List<Folder> init(String userId) {
        var defaultFolders = Arrays.asList(
            new Folder(userId, "Inbox", "blue"),
            new Folder(userId, "Sent", "purple"),
            new Folder(userId, "Important", "red"),
            new Folder(userId, "Done", "green")
        );
        // for (int i = 0; i < defaultFolders.size(); i++) {
        //     defaultFolders.get(i).setCreatedTimeUuid(Uuids.timeBased());
        // }
        return defaultFolders;
    }
    
}
