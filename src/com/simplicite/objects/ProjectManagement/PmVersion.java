package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;
import com.simplicite.util.Tool;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Business object PmVersion
 */
public class PmVersion extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = dateObj.format(formatter);
		//msgs.add(Message.formatInfo("INFO_CODE", "Message", "fieldName"));
		//msgs.add(Message.formatWarning("WARNING_CODE", "Message", "fieldName"));
		//msgs.add(Message.formatError("ERROR_CODE", "Message", "fieldName"));
		//AppLog.info(" DEBUG "+"now: "+now+" datePub: "+getFieldValue("pmVrsPublicationDate")+" diff: "+Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now), getGrant());
		if(Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now)<0){
			msgs.add(Message.formatError("PM_ERR_PUBLICATIONDATE", "Publication date must be in the futur", "pmVrsPublicationDate"));
		}
		
		
		return msgs;
	}
	@Override
	public String postUpdate() {
		// TODO Auto-generated method stub
		if(getFieldValue("pmTskStatus").equalsIgnoreCase("CLOSED") && getFieldValue("pmTskClose").equalsIgnoreCase("") ){
			AppLog.info(" DEBUG "+"Update close", getGrant());
		}
		return super.postUpdate();
	}
}
