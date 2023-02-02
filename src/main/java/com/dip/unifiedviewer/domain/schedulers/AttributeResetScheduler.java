package com.dip.unifiedviewer.domain.schedulers;

import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_QUOTA;
import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_REMAINING;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dip.unifiedviewer.domain.persistent.services.AuthenticationPort;

@EnableScheduling
@Service
public class AttributeResetScheduler {

    private final static Logger logger = LoggerFactory.getLogger(AttributeResetScheduler.class);
    private final AuthenticationPort authenticationPort;

    public AttributeResetScheduler(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }


    @Scheduled(cron = "${dip.publicapi.scheduler.cron}")
    public void resetAttributes() {
        logger.info("Executing scheduler for resetting attributes ");
        String adminAccessToken = authenticationPort.getAdminAccessToken();
        Map<String, Map<String, String>> allUserAttributes = authenticationPort.getAllUserAttributes(adminAccessToken);
        for (String userId: allUserAttributes.keySet()) {
            Map<String, String> userAttributes = allUserAttributes.get(userId);
            for (String attributeKey: userAttributes.keySet()) {
                if (attributeKey.endsWith(ATTRIBUTE_KEY_SUBSTRING_QUOTA)) {
                    String value = userAttributes.get(attributeKey);
                    String remainingKey = attributeKey.replace(ATTRIBUTE_KEY_SUBSTRING_QUOTA, ATTRIBUTE_KEY_SUBSTRING_REMAINING);
                    userAttributes.put(remainingKey, value);
                }
            }
            authenticationPort.updateAttributes(adminAccessToken, userId, userAttributes);
        }
    }
}
