package com.slyim.websocket;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.slyim.pojo.AddressMongo;
import com.slyim.pojo.ChatMongo;
import com.slyim.service.impl.AddressServiceMongo;

public class MyWebSocketHander implements WebSocketHandler {
	private Gson gson=new Gson();
	private static final ArrayList<WebSocketSession> users=new ArrayList<WebSocketSession>();
	@Autowired
	private AddressServiceMongo addressServiceMongo;
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		users.remove(session);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		users.add(session);
		//
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		MyWebSocketMsg sockMsg=gson.fromJson((String)message.getPayload(), MyWebSocketMsg.class);
		if (sockMsg.getSockType()==null || sockMsg.getSockType().equals("")) {
			return;
		}
		if (sockMsg.getSockType().equals("AddFriend")) {
			AddressMongo addressMongo=gson.fromJson(sockMsg.getJsonData(),AddressMongo.class);
			AddressMongo toAddressMongo=addressServiceMongo.findOneByMyIdAndFriendId(addressMongo.getFriendId(), addressMongo.getMyId());
			sockMsg.setJsonData(gson.toJson(toAddressMongo));
			TextMessage returnMessage = new TextMessage(gson.toJson(sockMsg));
			sendMessageToUser(toAddressMongo.getMyId(),returnMessage);
		} else if (sockMsg.getSockType().equals("SendMsg")) {
			ChatMongo chatMongo=gson.fromJson(sockMsg.getJsonData(), ChatMongo.class); 
			ChatMongo toChatMongo=new ChatMongo();
			String myId=(String)session.getAttributes().get("id");
			toChatMongo.setType(chatMongo.getType());
			toChatMongo.setContent(chatMongo.getContent());
			toChatMongo.setMyId(chatMongo.getFriendId());
			toChatMongo.setFriendId(myId);
			toChatMongo.setSendTime(System.currentTimeMillis()/1000);
			sockMsg.setJsonData(gson.toJson(toChatMongo));
			TextMessage returnMessage = new TextMessage(gson.toJson(sockMsg));
			sendMessageToUser(toChatMongo.getMyId(),returnMessage);
		} else {
			return;
		}
		
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable arg1) throws Exception {
		if(session.isOpen()){
            session.close();
        }
        users.remove(session);
		
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String id, TextMessage message) {
        for (WebSocketSession user : users) {
            if (user.getAttributes().get("id").equals(id)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
	
}
