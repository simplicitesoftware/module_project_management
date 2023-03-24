package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

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
			msgs.add(Message.formatError("PM_ERR_PUBLICATIONDATE", null, "pmVrsPublicationDate"));
		}
		
		
		return msgs;
	}
	public int completionVersion(){
		String sqlQuery = "select pm_tsk_status from pm_task where pm_tsk_vrs_id="+getRowId(); //select all task of curent project version
		AppLog.info("DEBUG "+"sqlQuery: "+sqlQuery, getGrant());
		int taskCount=0;
		int finishedTaskCount=0;
		for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
			taskCount+=1;
			if (row[0].equals("DRAFT") || row[0].equals("CLOSED") || row[0].equals("CANCEL") || row[0].equals("REJECTED")){
				finishedTaskCount+=1;
			}
		}
		return (finishedTaskCount*100)/taskCount;
	}
	
}
