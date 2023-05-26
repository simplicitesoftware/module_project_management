package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmDocument
 */
public class PmDocument extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public List<String> postValidate() {
		//Display error if validating an  empty document
		List<String> msgs = new ArrayList<>();
		if(getStatus().equals("V") && Tool.isEmpty(getFieldValue("pmDocAttachment"))){
			msgs.add(Message.formatError("PM_NULL_ATTACHMENT", null, "pmDocAttachment"));
			setStatus("WFV");
		}
		
	
		return msgs;
	}
}
