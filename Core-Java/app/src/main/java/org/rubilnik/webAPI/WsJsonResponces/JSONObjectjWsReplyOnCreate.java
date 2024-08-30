package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectjWsReplyOnCreate extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnCreate(boolean isOk){
        super("create", new JSONObject().put("status", isOk? "ok" : "fail"));
    }
}

