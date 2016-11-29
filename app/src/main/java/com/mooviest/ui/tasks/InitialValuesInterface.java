package com.mooviest.ui.tasks;

import com.mooviest.ui.rest.responses.MooviestApiResult;

/**
 * Created by jesus on 25/10/16.
 */

public interface InitialValuesInterface {

    public void swipeResponse(MooviestApiResult mooviestApiResult);

    public void listsResponse(String list_name, MooviestApiResult mooviestApiResult);

}
