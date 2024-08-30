package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;


public class JSONObjectjWsReply extends JSONObject {
    JSONObjectjWsReply(String event, JSONObject data){
        super.put("event", event);
        super.put("data", event);
    }
}
