package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectjWsReplyOnChoice extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnChoice(int questionId, int chocieId){
        super("next", new JSONObject().put("questionId", questionId).put("choiceId", chocieId));
    }
}

