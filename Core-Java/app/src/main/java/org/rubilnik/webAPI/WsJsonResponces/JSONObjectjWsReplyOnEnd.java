package org.rubilnik.webAPI.WsJsonResponces;

import java.util.Map.Entry;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rubilnik.basicLogic.users.Player;

public class JSONObjectjWsReplyOnEnd extends JSONObjectjWsReply {
    public JSONObjectjWsReplyOnEnd(List<Entry<Player,Integer>> scores){
        super("end", new JSONObject().put("results", new JSONArray(scores)));
    }
}
