package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectWsMessageJoined extends JSONObjectjWsReply{
    public JSONObjectWsMessageJoined(String id, String name){
        super("joined", new JSONObject().put("id", id).put("name", name));
    }
}
