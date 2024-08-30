package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;
import org.rubilnik.basicLogic.Quiz.Question;

public class JSONObjectjWsReplyOnNext extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnNext(Question question){
        super("next", new JSONObject(question));
    }
}

