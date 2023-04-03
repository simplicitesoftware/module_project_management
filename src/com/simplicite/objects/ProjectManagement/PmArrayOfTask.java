package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmArrayOfTask
 */
public class PmArrayOfTask extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void initUpdate() {
		String prv=getFieldValue("pmAotPrvTskId");
		String next=getFieldValue("pmAotNextTskId");
		if(!Tool.isEmpty(prv)){
			AppLog.info("DEBUG: previous: "+prv, getGrant());
		}else if (Tool.isEmpty(next)){
			AppLog.info("DEBUG: next: "+next, getGrant());
		}else{
			AppLog.info("DEBUG: nothing "+next, getGrant());
		}
		super.initUpdate();
	}
}
