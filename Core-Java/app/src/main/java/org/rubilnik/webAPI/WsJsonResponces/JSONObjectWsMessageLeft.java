package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectWsMessageLeft extends JSONObjectjWsReply {
    public JSONObjectWsMessageLeft(String id, String name){
        super("left", new JSONObject().put("id", id).put("name", name));
    }
}
