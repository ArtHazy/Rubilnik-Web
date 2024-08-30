package org.rubilnik.webAPI.WsJsonResponces;

import org.json.JSONObject;

public class JSONObjectjWsReplyOnReveal extends JSONObjectjWsReply {
    JSONObjectjWsReplyOnReveal(int correctChoiceInd){
        super("next", new JSONObject().put("correctChoiceInd", correctChoiceInd));
    }
}
