//package com.dip.unifiedviewer.domain.services.impls;
//
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_DAILY;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_END;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_OFFPEAK;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_PEAK;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_QUOTA;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_REMAINING;
//import static com.dip.unifiedviewer.constansts.KeycloakConstants.ATTRIBUTE_KEY_SUBSTRING_START;
//
//import java.security.PublicKey;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.dip.unifiedviewer.domain.factory.MasterTreeFactory;
//import com.dip.unifiedviewer.domain.model.TreeNode;
//import com.dip.unifiedviewer.domain.model.requests.BaseRequestBodyModel;
//import com.dip.unifiedviewer.domain.persistent.services.AuthenticationPort;
//import com.dip.unifiedviewer.domain.services.AuthenticationService;
//import com.dip.unifiedviewer.exceptions.RequestForbiddenException;
//import com.dip.unifiedviewer.exceptions.RequestUnauthorizedException;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//@Service
//@Slf4j
//public class AuthenticationServiceImpl implements AuthenticationService {
//
//  private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
//
//  private final MasterTreeFactory masterTreeFactory;
//  private final AuthenticationPort authenticationPort;
//  private final PublicKey publicKey;
//
//  public AuthenticationServiceImpl(
//          MasterTreeFactory masterTreeFactory,
//          AuthenticationPort authenticationPort, PublicKey publicKey) {
//    this.masterTreeFactory = masterTreeFactory;
//    this.authenticationPort = authenticationPort;
//    this.publicKey = publicKey;
//  }
//
//  @Override
//  public String authenticateUser(BaseRequestBodyModel requestBodyModel) {
//    String accessToken = authenticationPort.getUserAccessToken(requestBodyModel.getUsername(), requestBodyModel.getPassword());
//    if (accessToken == null) {
//      throw new RequestUnauthorizedException("Invalid credentials");
//    }
//    Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody();
//    String userId = claims.getSubject();
//    List<String> roles = Optional.of(claims.get("roles", List.class))
//            .orElseThrow(() -> new RequestForbiddenException("No Roles added for user"));
//
//    String agency = String.valueOf(Optional.of(claims.get("groups", List.class))
//            .orElseThrow(() -> new RequestForbiddenException("No Groups added for user"))
//            .get(0));
//    requestBodyModel.setUserId(userId);
//    requestBodyModel.setAgencyId(agency);
//    String rule = requestBodyModel.getRule().getValue();
//    for (String requestedFor : requestBodyModel.getRequestedFor()) {
//      String roleName = "CAN_REQUEST_" + requestedFor + "_WITH_" + rule;
//      if (!roles.contains(roleName)) {
//        throw new RequestForbiddenException("Permission denied for " + requestedFor);
//      }
//    }
//    return accessToken;
//  }
//
//  @Override
//  public void adjustParameters(BaseRequestBodyModel requestBodyModel) {
//    String adminAccessToken = authenticationPort.getAdminAccessToken();
//    if (adminAccessToken == null) {
//      logger.error("Could not retrieve admin access token");
//    }
//    logger.info("retrieved admin access token");
//    Map<String, String> attributeMap =
//        authenticationPort.getAttributeMap(requestBodyModel.getUserId(), adminAccessToken);
//    logger.info("retrieved attribute map with size: " + attributeMap.size());
//    adjustNodeAndAttribute(
//        requestBodyModel,
//        masterTreeFactory.getTreeByType(requestBodyModel.getRule()),
//        attributeMap);
//    logger.info("Adjusted nodes and attributes");
//    authenticationPort.updateAttributes(adminAccessToken, requestBodyModel.getUserId(), attributeMap);
//    logger.info("Adjusted parameters for request id: " + requestBodyModel.getRequestId());
//  }
//
//  private void adjustNodeAndAttribute(
//      BaseRequestBodyModel requestBodyModel, TreeNode node, Map<String, String> attributeMap) {
//    Set<String> requestedFor = requestBodyModel.getRequestedFor();
//    if (isSubTreeUsed(node, requestedFor)) {
//      if (isLimitExceededForNode(node, attributeMap)) {
//        removeSubTree(node, requestedFor);
//        requestBodyModel.getLimitExceededList().add(node.getType().getValue());
//      } else {
//        adjustAttributes(node, attributeMap);
//        for (TreeNode child : node.getChildren()) {
//          adjustNodeAndAttribute(requestBodyModel, child, attributeMap);
//        }
//      }
//    }
//  }
//
//  private void adjustAttributes(TreeNode node, Map<String, String> attributeMap) {
//    String type = node.getType().getValue();
//    if (attributeMap.containsKey(
//        getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_DAILY, ATTRIBUTE_KEY_SUBSTRING_QUOTA))) {
//      int remaining =
//          Integer.parseInt(
//              attributeMap.getOrDefault(
//                  getKeyName(
//                      type, ATTRIBUTE_KEY_SUBSTRING_DAILY, ATTRIBUTE_KEY_SUBSTRING_REMAINING),
//                  "-1"));
//      if (remaining != -1) {
//        remaining = remaining - node.getExpectedCount();
//      }
//      attributeMap.put(
//          getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_DAILY, ATTRIBUTE_KEY_SUBSTRING_REMAINING),
//          String.valueOf(remaining));
//    }
//
//    if (attributeMap.containsKey(
//        getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_QUOTA))) {
//      String peakStartStr =
//          attributeMap.get(
//              getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_START));
//      String peakEndStr =
//          attributeMap.get(
//              getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_END));
//      LocalTime peakStart = LocalTime.parse(peakStartStr);
//      LocalTime peakEnd = LocalTime.parse(peakEndStr);
//      LocalTime now = LocalTime.now();
//      String time = ATTRIBUTE_KEY_SUBSTRING_OFFPEAK;
//      if (now.isAfter(peakStart) && now.isBefore(peakEnd)) {
//        time = ATTRIBUTE_KEY_SUBSTRING_PEAK;
//      }
//      String key = getKeyName(type, time, ATTRIBUTE_KEY_SUBSTRING_REMAINING);
//      int remaining = Integer.parseInt(attributeMap.getOrDefault(key, "-1"));
//      if (remaining != -1) {
//        remaining = remaining - node.getExpectedCount();
//      }
//      attributeMap.put(key, String.valueOf(remaining));
//    }
//  }
//
//  public void removeSubTree(TreeNode node, Set<String> list) {
//    list.remove(node.getType().getValue());
//    for (TreeNode child : node.getChildren()) {
//      removeSubTree(child, list);
//    }
//  }
//
//  private boolean isSubTreeUsed(TreeNode node, Set<String> requestedFor) {
//    if (requestedFor.contains(node.getType().getValue())) return true;
//    for (TreeNode child : node.getChildren()) {
//      if (isSubTreeUsed(child, requestedFor)) return true;
//    }
//    return false;
//  }
//
//  private boolean isLimitExceededForNode(TreeNode node, Map<String, String> attributeMap) {
//    String type = node.getType().getValue();
//    if (attributeMap.containsKey(
//        getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_DAILY, ATTRIBUTE_KEY_SUBSTRING_QUOTA))) {
//      int remaining =
//          Integer.parseInt(
//              attributeMap.getOrDefault(
//                  getKeyName(
//                      type, ATTRIBUTE_KEY_SUBSTRING_DAILY, ATTRIBUTE_KEY_SUBSTRING_REMAINING),
//                  "-1"));
//      if (remaining != -1 && remaining < node.getExpectedCount()) return true;
//    }
//
//    if (attributeMap.containsKey(
//        getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_QUOTA))) {
//      LocalTime now = LocalTime.now();
//      String peakStartStr =
//          attributeMap.get(
//              getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_START));
//      String peakEndStr =
//          attributeMap.get(
//              getKeyName(type, ATTRIBUTE_KEY_SUBSTRING_PEAK, ATTRIBUTE_KEY_SUBSTRING_END));
//      LocalTime peakStart = LocalTime.parse(peakStartStr);
//      LocalTime peakEnd = LocalTime.parse(peakEndStr);
//      String time = ATTRIBUTE_KEY_SUBSTRING_OFFPEAK;
//      if (now.isAfter(peakStart) && now.isBefore(peakEnd)) {
//        time = ATTRIBUTE_KEY_SUBSTRING_PEAK;
//      }
//      int remaining =
//          Integer.parseInt(
//              attributeMap.getOrDefault(
//                  getKeyName(type, time, ATTRIBUTE_KEY_SUBSTRING_REMAINING), "-1"));
//      if (remaining != -1 && remaining < node.getExpectedCount()) return true;
//    }
//
//    return false;
//  }
//
//  private String getKeyName(String type, String time, String attributeType) {
//    return type + "_REQUEST_" + time + "_" + attributeType;
//  }
//}
