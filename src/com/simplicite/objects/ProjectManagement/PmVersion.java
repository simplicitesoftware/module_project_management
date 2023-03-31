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
		if(!getStatus().equals("PUBLISHED") && Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now)<0){
			msgs.add(Message.formatError("PM_ERR_PUBLICATIONDATE", null, "pmVrsPublicationDate"));
		}
		
		return msgs;
	}
	@Override
	public String preUpdate() {
		String msg="";
		// release management
		if(getStatus().equals("PUBLISHED") && !getOldStatus().equals("PUBLISHED")){
			ObjectDB tmpTask= this.getGrant().getTmpObject("PmTask");
			synchronized(tmpTask){
				tmpTask.resetFilters();
				tmpTask.setFieldFilter("pmTskVrsId", getRowId());
				for(String[] row : tmpTask.search()){
					tmpTask.select(row[0]);
					String status=tmpTask.getStatus();
					if(status.equals("TODO") || status.equals("DOING") || status.equals("DONE") || status.equals("DRAFT")){
							msg=Message.formatError("PM_ERR_PUBLICATION_STATUS", null, "pmVrsStatus");
							break;
						}
				}
			}
		}
		if (!Tool.isEmpty(msg)){
			return super.preUpdate();
		}

		return msg;
	}

	public int completionVersion(){
		int taskCount=0;
		int finishedTaskCount=0;
		ObjectDB tmpTask= this.getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			tmpTask.resetFilters();
			tmpTask.setFieldFilter("pmTskVrsId", getRowId());
			for(String[] row : tmpTask.search()){
				taskCount+=1;
				tmpTask.select(row[0]);
				String status=tmpTask.getStatus();
				if (status.equals("DRAFT") || status.equals("CLOSED") || status.equals("CANCEL") || status.equals("REJECTED")){
					finishedTaskCount+=1;
				}
			}
		}
		if (taskCount==0)return 100;
		return (finishedTaskCount*100)/taskCount;
	}
	public String deferTask(){
		Action a = getAction("PM_DEFER_TASK");
		AppLog.info("DEBUG "+ a.getConfirmField(getGrant().getLang(), "pmDtVrsVersion").getId(), getGrant());
		String msg = new String();
		ObjectDB tmpTask = getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			tmpTask.resetFilters();
			tmpTask.setFieldFilter("pmTskVrsId", getRowId());
			for(String[] row : tmpTask.search()){
				tmpTask.select(row[0]);
				if(tmpTask.getStatus().equals("DRAFT") || tmpTask.getStatus().equals("TODO") || tmpTask.getStatus().equals("DOING") ){
					tmpTask.setFieldValue("pmTskVrsId", "9");
					tmpTask.save();
				}
				
			}
		}
		
		
		return msg;
	}
}
