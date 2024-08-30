package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectjWsReplyOnJoin extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnJoin(String id, String name){
        super("join", new JSONObject().put("id", id).put("name", name));
    }
}
