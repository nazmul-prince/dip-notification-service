package com.dip.unifiedviewer.storage.redis.service;

import com.dip.unifiedviewer.domain.services.DataUpdater;
import com.dip.unifiedviewer.domain.services.RequestAcceptionTemplate;
import com.dip.unifiedviewer.util.StringUtil;

import static com.dip.unifiedviewer.constansts.JsonConstants.JSON_KEY_PUBLISHED_CHANNEL;
import static com.dip.unifiedviewer.constansts.JsonConstants.JSON_KEY_RESPONSE_TYPE;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {
	private final Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

//	private final DataUpdater dataUpdater;
	private final RequestAcceptionTemplate requestAcceptionTemplate;

	public RedisMessageSubscriber(@Lazy RequestAcceptionTemplate requestAcceptionTemplate) {
		this.requestAcceptionTemplate = requestAcceptionTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
//    String channel = new String(bytes);
		String channel = String.valueOf(bytes);
		try {

			String strMessage = message.toString();
			strMessage = StringUtil.getParsedString(strMessage);
			JSONObject jsonResponse = new JSONObject(strMessage.substring(1, strMessage.length() - 1));
			logger.info("Message received from channel: " + channel + ", type: " + jsonResponse.get("type")
					+ " in redis on message listener.");

			// This requestInfoKey is the same as the channel.
			String requestInfoKey = jsonResponse.getString(JSON_KEY_PUBLISHED_CHANNEL);
			String responseDataType = jsonResponse.getString(JSON_KEY_RESPONSE_TYPE);
			
			requestAcceptionTemplate.initiateUnifiedViewAcceptionNotificationRequest(jsonResponse);
		} catch (Exception e) {
			logger.error("Error while get the message from listener for channel: " + channel, e);
		}
	}
}
