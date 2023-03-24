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
	@Override
	public String postUpdate() {
		String msg="";
		// release management
		String sqlQuery = "select ROW_ID from pm_label where pm_lbl_name=Feature";	
		// lblName is key so we have allmost 1 row
		List<String[]> queryResult =getGrant().query(sqlQuery);
		if (queryResult.size()>0){
			String labelId=queryResult.get(0)[0];
			sqlQuery = "select row_id,pm_tsk_status from pm_task where pm_vrs_prj_id="+getRowId();
			for(String[] row : getGrant().query(sqlQuery)){
				if(row[1].equals("TODO") || row[1].equals("DOING") || row[1].equals("DONE") || row[1].equals("DRAFT")){
					String sqlQueryLabel = "select ROW_ID from pm_tsk_lbl where pm_tsklbl_tsk_id="+row[0]+" AND pm_tsklbl_lbl_id="+labelId;	// select labeling where label is feature and task is not do task of version.
					if(getGrant().query(sqlQueryLabel).size()==0){
						msg="All Feature task must be closed, rejected or cancel";
						break;
					}
				}
				
			}

		}else{
			AppLog.info(getClass(),"postUpdate" ,"label Feature not found" , getGrant());
		}
		
		if (msg.equals("")){
			return super.postUpdate();
		}
		return msg;
	}
	public int completionVersion(){
		String sqlQuery = "select pm_tsk_status from pm_task where pm_tsk_vrs_id="+getRowId(); //select all task of curent project version
		AppLog.info("DEBUG "+"sqlQuery: "+sqlQuery, getGrant());
		int taskCount=0;
		int finishedTaskCount=0;
		for(String[] row : getGrant().query(sqlQuery)){// for all task update counter
			taskCount+=1;
			if (row[0].equals("DRAFT") || row[0].equals("CLOSED") || row[0].equals("CANCEL") || row[0].equals("REJECTED")){
				finishedTaskCount+=1;
			}
		}
		return (finishedTaskCount*100)/taskCount;
	}
	
}
