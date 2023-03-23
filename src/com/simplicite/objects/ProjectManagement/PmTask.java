package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTask
 */
public class PmTask extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public String postUpdate() {
		// TODO Auto-generated method stub
		if(getFieldValue("pmTskStatus").equalsIgnoreCase("CLOSED") && getFieldValue("pmTskClose").equalsIgnoreCase("") ){
			AppLog.info(" DEBUG "+"Update close", getGrant());
		}
		return super.postUpdate();
	}
}
