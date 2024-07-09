package CentralSync.demo.util;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

@Autowired
    private ActiveUserStore activeUserStore;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Long userId = null;
        synchronized (activeUserStore) {
            Set<Long> activeUsers = activeUserStore.getActiveUsers();
            if (!activeUsers.isEmpty()) {
                userId = activeUsers.iterator().next();  // Get one active user ID
                // Optionally: Mark the user as "in use" if needed
            } else {
                LOG.warn("No active user IDs available.");
            }
        }

        if (userId != null) {
            LOG.info("User with ID '{}' opened the page", userId);
            return new UserPrincipal(userId.toString());
        } else {
            // Fallback to a random ID if no active user IDs are available
            final String randomId = UUID.randomUUID().toString();
            LOG.info("Fallback: User with ID '{}' opened the page", randomId);
            return new UserPrincipal(randomId);
        }
    }
}
