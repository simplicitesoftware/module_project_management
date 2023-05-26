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
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		//Display error if tasks are not in same project
		if (!getFieldValue("pmAotNextTskId.pmTskVrsId.pmVrsPrjId").equals(getFieldValue("pmAotPrvTskId.pmTskVrsId.pmVrsPrjId")))
			msgs.add(Message.formatInfo("PM_ERR_AOT_DIFF_PRJ", null, "pmAotNextTskId.pmTskVrsId.pmVrsPrjId"));
		return msgs;
	}
}
