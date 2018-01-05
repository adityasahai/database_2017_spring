package com.databasefrontend;


/**
 * Created by adityasahai on 22/03/17.
 */
public enum sqlCall_enum {
    viewInventory(true, new String[] {"select itemid as \"Item ID\", name as \"Name\", category as \"Category\"," +
            " subcategory as \"Sub-Category\", storagetype as \"Storage Type\", numunits as \"Units Available\", " +
            "expiry as \"Expiry Date\" from item where siteid = $$temp_var_replace_xyz$$ and numunits!=0;\n"}, new String[] {}),
    viewBunks(false, new String [] {"select siteid as \"Site ID\", site.shortname as shortname, currstate as \"Current State\", description as " +
            "\"Description\", conditions as \"Conditions\", starttime as \"Start Time\", endtime as \"End Time\", " +
            "malebunks as \"No. of Male Bunks\", femalebunks as \"No. of Female Bunks\", mixedbunks as " +
            "\"No. of Mixed Bunks\", roomsavail as \"No. of Rooms\" from serviceshelter natural join site where currstate='active' " +
            "and (malebunks != 0 or femalebunks != 0 or roomsavail != 0 or mixedbunks != 0);"}, new String[] {}),
    viewMeals(false,
              new String[] {"DROP TABLE IF EXISTS foodItemSubCategory; CREATE TEMP TABLE " +
                      "foodItemSubCategory as SELECT 'nuts/grains/beans' as subcategory " +
                      "UNION SELECT 'vegetables' UNION SELECT 'meat/seafood' UNION SELECT 'dairy/eggs';",
                      "DROP TABLE IF EXISTS temp1; CREATE TEMP TABLE temp1 AS select d.subcategory,sum(numunits) " +
                              "as total_sum FROM foodItemSubCategory d JOIN (select * from ITEM WHERE " +
                              "category = 'food' and expiry >=CURRENT_DATE )c on " +
                              "c.subcategory=d.subcategory::subcategories GROUP BY d.subcategory;",
                      "update temp1 SET subcategory = 'meat/seafood or dairy/eggs' WHERE " +
                              "subcategory = 'meat/seafood' or subcategory = 'dairy/eggs';",
                      "DROP TABLE IF EXISTS temp2; CREATE TEMP TABLE temp2 AS " +
                              "(select subcategory,SUM(total_sum) as total_sum FROM temp1 GROUP BY subcategory);",
                      "DROP TABLE IF EXISTS foodItemSubCategory; CREATE TEMP TABLE foodItemSubCategory as " +
                              "SELECT 'nuts/grains/beans' as subcategory UNION SELECT 'vegetables' UNION SELECT " +
                              "'meat/seafood or dairy/eggs';",
                      "DROP TABLE IF EXISTS temp3; CREATE TEMP TABLE temp3  AS " +
                              "(select foodItemSubCategory.subcategory, case when total_sum is null " +
                              "then 0 ELSE total_sum end  from temp2 Right JOIN foodItemSubCategory on " +
                              "temp2.subcategory=foodItemSubCategory.subcategory);",
                      "DROP TABLE If exists temp4; create temp table temp4 as (SELECT a.total_sum " +
                      "as maxMealsPossible ,subcategory as subcategoriesneeded  FROM temp3 as a " +
                      "INNER JOIN (SELECT MIN(total_sum) as total_sum FROM temp3 ) as b ON a.total_sum=b.total_sum);",
                      "update temp4 set maxMealsPossible=(select min(total_sum) from temp3);",
                      "select maxMealsPossible as \"Max Meals Possible\", string_agg(temp4.subcategoriesneeded::text, ', ') " +
                      "as \"Sub Categories Needed\" from temp4 group by maxMealsPossible;"},
               new String[] {}),
    viewMeals2(false, new String[]{"select sum(numunits) as count, subcategory  from item where\n" +
            "item.subcategory IN ('vegetables','meat/seafood', 'nuts/grains/beans', 'dairy/eggs') " +
            "   group by subcategory order by subcategory asc;"}, new String[]{}),
    getUser(true, new String[] {"select username, password from \"user\" where username = $$temp_var_replace_xyz$$ AND password = $$temp_var_replace_xyz$$"}, new String[] {}),
    getUserData(true, new String[] {"select * from \"user\" where username = $$temp_var_replace_xyz$$"}, new String[] {}),
    getClintLogs(true, new String [] {"select * from clientlogs where clientid = $$temp_var_replace_xyz$$ order by timest desc"}, new String[] {}),
    getAllServices(true, new String[]{"select a.servicename as \"Service Name\", a.currstate as \"Status\" from \n" +
            "(select 'Food Bank' as servicename, currstate, '1' as rowNum from servicefoodbank where siteid = $$temp_var_replace_xyz$$\n" +
            " union\n" +
            " select 'Soup Kitchen' as servicename, currstate, '2' as rowNum  from servicesoupkitchen where siteid = $$temp_var_replace_xyz$$\n" +
            " union\n" +
            " select 'Shelter' as servicename, currstate, '3' as rowNum from serviceshelter where siteid = $$temp_var_replace_xyz$$\n" +
            " union\n" +
            " select 'Food Pantry' as servicename, currstate, '4' as rowNum from servicefoodpantry where siteid = $$temp_var_replace_xyz$$) as a order by rowNum;"},
                         new String[]{}),
    searchclientbyname(true, new String[] {"select a.clientid as \"Client ID\",a.idnumber as \"ID Number\", " +
                                "a.idtype as \"ID Type\", a.firstname as \"First Name\", a.lastname as \"Last Name\", " +
                                "a.phonenumber as \"Phone Number\" from (SELECT c.*,b.count FROM client c, " +
                                "(SELECT count(*) count from client WHERE " +
                                "lower(firstname) LIKE lower($$%temp_var_replace_xyz%$$) or " +
                                "lower(lastname) LIKE lower($$%temp_var_replace_xyz%$$)) AS b WHERE lower(firstname)" +
                                " LIKE lower($$%temp_var_replace_xyz%$$) or lower(lastname) LIKE lower($abc$%temp_var_replace_xyz%$abc$)) " +
                                "a limit 5;"},
            new String[]{}),
    searchclientbyidtype(true,
                         new String[]{"select a.clientid as \"Client ID\", a.idnumber as \"ID Number\"," +
                                 " a.idtype as \"ID Type\", a.firstname as \"First Name\", a.lastname as \"Last Name\"," +
                                 " a.phonenumber as \"Phone Number\" from (SELECT c.*,b.count FROM " +
                                 "client c, (SELECT count(*) from client "
                                 + "WHERE lower(idnumber)"
                                 + " LIKE lower($$%temp_var_replace_xyz%$$) and idtype = 'temp_var_replace_xyz') as b WHERE lower(idnumber) "
                                 + " LIKE lower($$%temp_var_replace_xyz%$$) and idtype = 'temp_var_replace_xyz') a limit 5;"},
                         new String[]{}),
    addClient(true,
              new String[]{"insert into client (idnumber, idtype, firstname, lastname, phonenumber) " +
                                 "values($$temp_var_replace_xyz$$,$$temp_var_replace_xyz$$, " +
                                  "$$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$);" +
                      "insert into clientlogs(clientid, description) values((select clientid from client where" +
                      " idnumber=$$temp_var_replace_xyz$$ and idtype=$$temp_var_replace_xyz$$), 'profile created');"},
              new String[]{}),
    getWaitList(true,
                new String[]{"select * from waitlist where siteid=$$temp_var_replace_xyz$$ order by waitlistnum asc"},
                new String[]{}),
    insetWaitListEntry1(true,
                        new String[]{"select count(*) from waitlist where siteid=$$temp_var_replace_xyz$$"},
                        new String[]{}),
    insertWaitListEntry2(true,
                         new String[]{"insert into waitlist(clientid, siteid, waitlistnum) " +
                                 "values ($$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$, " +
                                 "(select count(*)+1 from waitlist where siteid=$$temp_var_replace_xyz$$));"},
                         new String[]{}),
    addService(true,
               new String[]{"update temp_var_replace_xyz set currstate='active' where " +
                            "siteid=temp_var_replace_xyz;"},
               new String[]{}),
    deleteService(true,
                  new String[]{"update temp_var_replace_xyz set currstate='inactive' where " +
                               "siteid=temp_var_replace_xyz;"},
                  new String[]{}),
    editClient(true,
               new String[]{"Update client set idnumber=$$temp_var_replace_xyz$$, idtype=$$temp_var_replace_xyz$$," +
                            "firstname=$$temp_var_replace_xyz$$, lastname=$$temp_var_replace_xyz$$," +
                            "phonenumber=$$temp_var_replace_xyz$$ where clientid=$$temp_var_replace_xyz$$;",
                            "Insert into clientlogs(clientid, description) values($$temp_var_replace_xyz$$," +
                            "$$temp_var_replace_xyz$$)"},

               new String[]{}),
    addLogToClient(true,
                   new String[]{"insert into clientlogs(clientid, description) values($$temp_var_replace_xyz$$," +
                                "$$temp_var_replace_xyz$$)"},
                   new String[]{}),

    getOutStandingReqs(true,
                       new String[]{"select request.requestid as \"Request ID\", item.itemid as \"Item ID\"," +
                               " item.name as \"Item Name\"," +
                               " item.category as \"Item Category\", " +
                               "item.subcategory as \"Item SubCat\", item.storagetype as \"Storage Type\"," +
                               " item.numunits as \"Units Available\", item.expiry as \"Expiry\"," +
                               " request.quantity as \"Units Requested\", request.reqstate as \"Status\" from request " +
                               "inner join item on item.itemid = request.itemid where siteid=$$temp_var_replace_xyz$$" +
                               "and request.reqstate != 'closed';"},
                       new String[]{}),
    updateRequestCount(true, new String[] {"update request set quantity = $$temp_var_replace_xyz$$ " +
                            "where requestid = $$temp_var_replace_xyz$$;"},
                            new String[] {}),
    acceptIncomingRequest(true,
                        new String[] {"update item set numunits = numunits - $$temp_var_replace_xyz$$ " +
                                "where itemid = $$temp_var_replace_xyz$$;",
                                "update request set reqstate='closed', quantity=$$temp_var_replace_xyz$$" +
                                      " where requestid=$$temp_var_replace_xyz$$;"},
                        new String[]{}),
    addItem(true,
            new String[]{"Insert into item(name, category, subcategory, storagetype, numunits," +
                         "expiry, siteid) values($$temp_var_replace_xyz$$,$$temp_var_replace_xyz$$," +
                         "$$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$,$$temp_var_replace_xyz$$," +
                         "$$temp_var_replace_xyz$$,$$temp_var_replace_xyz$$);"},
            new String[]{}),
    addItemtryupdate(true,
                     new String[] {"update item set numunits=numunits+$$temp_var_replace_xyz$$ where " +
                        "name=$$temp_var_replace_xyz$$ and subcategory=$$temp_var_replace_xyz$$ and " +
                        "storagetype=$$temp_var_replace_xyz$$ and  expiry=$$temp_var_replace_xyz$$" +
                        "and siteid=$$temp_var_replace_xyz$$"},
                     new String[]{}),
    checkifitemexists(true, new String[]{"select * from item where  name=$$temp_var_replace_xyz$$ and " +
            "subcategory=$$temp_var_replace_xyz$$ and " +
            " storagetype=$$temp_var_replace_xyz$$ and  expiry=$$temp_var_replace_xyz$$" +
            " and siteid=$$temp_var_replace_xyz$$"}, new String[]{}),
    getItemsSearch(true,
                   new String[]{"select itemid as \"Item Id\", name as \"Name\"," +
                            " category as \"Category\", subcategory as \"Sub Category\"," +
                            " storagetype as \"Storage Type\", numunits as \"Units Available\", " +
                            " expiry as \"Expiry Date\", siteid as \"Site ID\" from item where" +
                            " (expiry <= $$temp_var_replace_xyz$$ AND" +
                            " expiry >= $$temp_var_replace_xyz$$ AND" +
                            " category IN temp_var_replace_xyz AND" +
                            " subcategory IN temp_var_replace_xyz AND" +
                            " storagetype IN temp_var_replace_xyz) AND" +
                            " name like $$%temp_var_replace_xyz%$$ AND" +
                            " siteid IN temp_var_replace_xyz AND numunits!=0;"},
                   new String[]{}),
    requestItem(true, new String[] {"insert into request(itemid, reqstate, quantity, username)" +
                                    " values(temp_var_replace_xyz, 'pending', $$temp_var_replace_xyz$$, " +
                                    "$$temp_var_replace_xyz$$);"},
                      new String[] {}),
    getUserRequests(true, new String[] {"select request.requestid, item.name as \"Item Name\", item.category as \"Item Category\", " +
                                        "item.subcategory as \"Item SubCat\", item.expiry as \"Expiry\", " +
                                        "item.siteid as \"Item Site ID\", request.reqstate as \"Status\", " +
                                        "request.quantity as \"Quantity Requested\" " +
                                        "from item join request on item.itemid = request.itemid  " +
                                        "where request.username='temp_var_replace_xyz' ;"},
                          new String[]{}),
    getSitecount(false, new String[]{"select count(*) from site"}, new String[]{}),
    getSitesInfo(false, new String[]{"select siteid, shortname from site"}, new String[]{}),
    viewWaitlist(true, new String[] {"select client.firstname, client.lastname, waitlistnum from client natural join " +
            "waitlist where waitlist.siteid=$$temp_var_replace_xyz$$ order by waitlistnum asc;"}, new String[] {}),
    deleteFromWaitlist(true, new String[]{"create or replace function deleteFromWaitlist(oldwaitlistnumber int, site int)\n" +
            "\treturns void LANGUAGE plpgsql AS\n" +
            "$func$\n" +
            "DECLARE\n" +
            "\t_r RECORD;\n" +
            "BEGIN\n" +
            "\tdelete from waitlist where waitlistnum = oldwaitlistnumber and siteid = site;\n" +
            "    for _r in select waitlistnum from waitlist where waitlistnum > oldwaitlistnumber and siteid = site order by waitlistnum asc\n" +
            "    \tloop\n" +
            "\t\t    update waitlist set waitlistnum = waitlistnum - 1 where waitlistnum = _r.waitlistnum and siteid = site;\n" +
            "    end loop;\n" +
            "END;\n" +
            "$func$;", "select deleteFromWaitlist($$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$);"},
            new String[]{}),
    updateWaitList(true, new String[] {"create or replace function updateWaitlist(oldwaitlistnumber int, newwaitlistnumber int, site int)\n" +
            "\treturns void LANGUAGE plpgsql AS\n" +
            "$func$\n" +
            "DECLARE\n" +
            "\t_r RECORD;\n" +
            "BEGIN\n" +
            "\tupdate waitlist set waitlistnum = (select count(*)+1 from waitlist where siteid = site) where siteid = site and waitlistnum = oldwaitlistnumber;\n" +
            "\tif newwaitlistnumber > oldwaitlistnumber then\n" +
            "    \tfor _r in select * from waitlist where waitlistnum > oldwaitlistnumber and waitlistnum <= newwaitlistnumber and siteid = site order by waitlistnum asc\n" +
            "        \tloop\n" +
            "\t\t        update waitlist set waitlistnum = waitlistnum - 1 where waitlistnum = _r.waitlistnum and siteid = site;\n" +
            "        end loop;\n" +
            "    else\n" +
            "    \tfor _r in select * from waitlist where waitlistnum < oldwaitlistnumber and waitlistnum >= newwaitlistnumber and siteid = site order by waitlistnum desc \n" +
            "        \tloop\n" +
            "        \t\tupdate waitlist set waitlistnum = waitlistnum + 1 where waitlistnum = _r.waitlistnum and siteid = site;\n" +
            "        end loop;\n" +
            "    end if;\n" +
            "    update waitlist set waitlistnum = newwaitlistnumber where siteid = site and waitlistnum = (select count(*)+1 from waitlist where siteid = site);    \n" +
            "END;\n" +
            "$func$;", "select updateWaitlist($$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$, $$temp_var_replace_xyz$$);"}, new String[] {}),
    editItemCount(true, new String[]{"update item set numunits = $$temp_var_replace_xyz$$ where itemid = temp_var_replace_xyz;"},
            new String[]{}),
    deleteFoodbankUpdateItems(true, new String[]{"update item set numunits=0 where siteid=$$temp_var_replace_xyz$$ and category='food';"}, new String[]{}),
    deleteFoodbankUpdateRequests(true, new String[]{"update request set quantity=0, reqstate='closed' where" +
            " request.itemid IN (select itemid from item where siteid=$$temp_var_replace_xyz$$);"}, new String[]{}),
    getInfoServiceFoodbank(true, new String[]{"select siteid as \"Site ID\", currstate as \"Status\"" +
                    " from servicefoodbank where siteid=$$temp_var_replace_xyz$$;"}, new String[]{}),
    getInfoServiceSoupkitchen(true, new String[]{"select siteid as \"Site ID\", currstate as \"Status\"," +
                    " description as \"Description\", conditions as \"Conditions\", starttime as \"Start Time\"," +
                    " endtime as \"End Time\", seats_available as \"Seats Available\"" +
                    " from servicesoupkitchen" +
                    " where siteid=$$temp_var_replace_xyz$$;"},
            new String[]{}),
    getInfoServiceShelter(true, new String[]{"select siteid as \"Site ID\", currstate as \"Status\"," +
                    " description as \"Description\", conditions as \"Conditions\", starttime as \"Start Time\"," +
                    " endtime as \"End Time\", malebunks as \"Male Bunks\", femalebunks as \"Female Bunks\","+
                    " mixedbunks as \"Mixed Bunks\", roomsavail as \"Rooms Available\"" +
                    " from serviceshelter where siteid=$$temp_var_replace_xyz$$;"},
            new String[]{}),
    getInfoServiceFoodpantry(true, new String[]{"select siteid as \"Site ID\", currstate as \"Status\"," +
                    " description as \"Description\", conditions as \"Conditions\", starttime as \"Start Time\"," +
                    " endtime as \"End Time\" from servicefoodpantry where siteid=$$temp_var_replace_xyz$$;"},
            new String[]{}),
    getall_waitlistedsites(true, new String[]{"select string_agg(site.shortname::text, ', ') as \"Wait List\" from waitlist " +
            "natural join site where clientid=$$temp_var_replace_xyz$$"}, new String[]{}),
    getActiveStates(true, new String[]{"select t1.currstate, t2.currstate, t3.currstate from serviceshelter as t1, servicesoupkitchen as t2, \n" +
            "servicefoodpantry as t3 where t1.siteid=$$temp_var_replace_xyz$$ and t2.siteid=$$temp_var_replace_xyz$$ and t3.siteid=$$temp_var_replace_xyz$$"}, new String[]{}),
    canceluserrequest(true, new String[]{"delete from request where requestid=$$temp_var_replace_xyz$$"}, new String[]{}),

    updateservicesoupkitchen(true,
            new String[]{"Update servicesoupkitchen set description=$$temp_var_replace_xyz$$, conditions=$$temp_var_replace_xyz$$," +
                    "starttime=$$temp_var_replace_xyz$$, endtime=$$temp_var_replace_xyz$$," +
                    "seats_available=$$temp_var_replace_xyz$$ where siteid=$$temp_var_replace_xyz$$"},
            new String[]{}),

    updateserviceshelter(true,
            new String[]{"Update serviceshelter set description=$$temp_var_replace_xyz$$, conditions=$$temp_var_replace_xyz$$," +
                    "starttime=$$temp_var_replace_xyz$$, endtime=$$temp_var_replace_xyz$$," +
                    "malebunks=$$temp_var_replace_xyz$$, femalebunks=$$temp_var_replace_xyz$$, mixedbunks=$$temp_var_replace_xyz$$," +
                    "roomsavail=$$temp_var_replace_xyz$$ where siteid=$$temp_var_replace_xyz$$"},
            new String[]{}),

    updateservicefoodpantry(true,
            new String[]{"Update servicefoodpantry set description=$$temp_var_replace_xyz$$, conditions=$$temp_var_replace_xyz$$," +
                    "starttime=$$temp_var_replace_xyz$$, endtime=$$temp_var_replace_xyz$$" +
                    " where siteid=$$temp_var_replace_xyz$$"},
            new String[]{}),
    allocroom(true, new String[]{"update serviceshelter set roomsavail=roomsavail-1" +
            "where siteid=$$temp_var_replace_xyz$$;"}, new String[]{}),
    decrement_bunkcount(true, new String[]{"update serviceshelter set temp_var_replace_xyz=temp_var_replace_xyz-1" +
            " where siteid=$$temp_var_replace_xyz$$"}, new String[]{}),
    getbunkcount(true, new String[]{"select $$temp_var_replace_xyz$$ from serviceshelter where siteid=$$temp_var_replace_xyz$$"},
             new String[]{});



    private final boolean ifParams;
    private final String[] queries;
    private String[] params;

    sqlCall_enum(boolean ifParams, String[] queries, String[] params) {
        this.ifParams = ifParams;
        this.queries = queries;
        this.params = params;
    }

    private int countNumParamsinString(String s) {
        String [] tempStr = s.split("temp_var_replace_xyz");
        return tempStr.length - 1;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    private String[] formQuery() {
        int queryCount = getQueryCount();
        String[] formedQuery = new String[queryCount];
        int k = 0;
        for (int i = 0; i < queryCount; i++) {
            String s = this.queries[i];
            int count = countNumParamsinString(s);
            if (count != 0) {
                for (int j = 0; j < count; j++) {
                    s = s.replaceFirst("temp_var_replace_xyz", this.params[k++]);
                }
            }
            System.out.println(s);
            formedQuery[i] = s;
        }
        if (this == addService) {
            for (String s : formedQuery) {
                System.out.print(s);
            }
        }
        return formedQuery;
    }

    public String[] getQueries() {
        if (!ifParams) {
            return this.queries;
        }
        else {
            return formQuery();
        }
    }

    public int getQueryCount() {
        return this.queries.length;
    }

    public int getParamCount() {
        return this.params.length;
    }
}
