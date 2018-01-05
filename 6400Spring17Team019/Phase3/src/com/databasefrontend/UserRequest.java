package com.databasefrontend;

/**
 * Created by Goutham on 3/19/2017.
 */
public class UserRequest {
    public int requestid;
    public int itemid;
    public int destsiteid;
    public enum request_state_e {pending, closed};
    request_state_e reqstate;
    public int quantity;
    public String username;

    public UserRequest(int itemid, int  destsiteid, request_state_e reqstate, int quantity, String username) {
        this.destsiteid = destsiteid;
        this.itemid = itemid;
        this.reqstate = reqstate;
        this.quantity = quantity;
        this.username = username;
    }

    public UserRequest(String requestid, String itemid, String  destsiteid,
                       String reqstate, String quantity, String username) {

        this.requestid = Integer.parseInt(requestid);
        this.destsiteid = Integer.parseInt(destsiteid);
        this.itemid = Integer.parseInt(itemid);
        this.reqstate = request_state_e.valueOf(reqstate);
        this.quantity = Integer.parseInt(quantity);
        this.username = username;
    }



}
