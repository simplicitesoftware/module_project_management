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
	public void initCreate() {
		String prv=getFieldValue("pmAotPrvTskId");
		String next=getFieldValue("pmAotNextTskId");
		AppLog.info("DEBUG: previous: "+prv, getGrant());
		AppLog.info("DEBUG: next: "+next, getGrant());
		super.initUpdate();
	}
}
