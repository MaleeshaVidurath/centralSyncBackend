package CentralSync.demo.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ActiveUserStore {

    private final Set<Long> activeUsers;
    private final Map<String, Long> sessionUserMap;

    public ActiveUserStore() {
        this.activeUsers = new HashSet<>();
        this.sessionUserMap = new HashMap<>();
    }

    public synchronized void addUser(Long userId) {
        activeUsers.add(userId);
    }

    public synchronized void removeUser(Long userId) {
        activeUsers.remove(userId);
    }

    public synchronized Set<Long> getActiveUsers() {
        return new HashSet<>(activeUsers);
    }

    public synchronized boolean isUserActive(Long userId) {
        return activeUsers.contains(userId);
    }

    public synchronized Long getUserIdForSession(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    public synchronized void setUserIdForSession(String sessionId, Long userId) {
        sessionUserMap.put(sessionId, userId);
    }

    public synchronized void removeSession(String sessionId) {
        sessionUserMap.remove(sessionId);
    }
}
