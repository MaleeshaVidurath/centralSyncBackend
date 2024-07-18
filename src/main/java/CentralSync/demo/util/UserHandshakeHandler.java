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
import java.util.UUID;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Autowired
    private ActiveUserStore activeUserStore;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // Extract the user ID from query parameters
        String query = request.getURI().getQuery();
        Long userId = null;

        if (query != null && query.startsWith("userId=")) {
            try {
                userId = Long.valueOf(query.substring(7));
            } catch (NumberFormatException e) {
                LOG.error("Invalid user ID format: '{}'", query, e);
            }
        }

        if (userId != null && activeUserStore.isUserActive(userId)) {
            LOG.info("User with ID '{}' opened the page", userId);
            return new UserPrincipal(userId.toString());
        } else {
            // Fallback to a random ID if user ID is not active or not provided
            final String randomId = UUID.randomUUID().toString();
            LOG.info("Fallback: User with ID '{}' opened the page", randomId);
            return new UserPrincipal(randomId);
        }
    }
}
