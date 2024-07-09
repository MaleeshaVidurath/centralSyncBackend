package CentralSync.demo.util;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class ActiveUserStore {

    private Set<Long> activeUsers;

    public ActiveUserStore() {
        this.activeUsers = new HashSet<>();
    }

    public synchronized void addUser(Long userId) {
        activeUsers.add(userId);
    }

    public synchronized void removeUser(Long userId) {
        activeUsers.remove(userId);
    }

    public synchronized Set<Long> getActiveUsers() {
        return activeUsers;
    }
    public synchronized boolean isUserActive(Long userId) {
        return activeUsers.contains(userId);
    }

}
