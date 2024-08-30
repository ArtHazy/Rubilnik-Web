package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;
import org.rubilnik.basicLogic.Quiz.Question;


public class JSONObjectjWsReplyOnStart extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnStart(Question question){
        super("bark", new JSONObject(question));
    }
}




