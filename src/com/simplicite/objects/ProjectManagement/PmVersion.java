package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Business object PmVersion
 */
public class PmVersion extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void initUpdate(){			
		HashMap<String, String> filters = new HashMap<>();
		filters.put("pmVrsPrjId", getFieldValue("pmVrsPrjId"));
		filters.put("pmVrsStatus", "ALPHA;BETA");
		getGrant().setParameter("PARENT_FILTERS", filters);
	}
	@Override
	public void preSearch(){		
		if(getInstanceName().equals("ref_ajax_PmVersion") && getGrant().hasParameter("PARENT_FILTERS")){
			HashMap<String, String> filters = (HashMap<String, String>) getGrant().getObjectParameter("PARENT_FILTERS");			
			setFieldFilter("pmVrsPrjId", filters.get("pmVrsPrjId"));
			setFieldFilter("pmVrsStatus", filters.get("pmVrsStatus"));
			getGrant().removeParameter("PARENT_FILTERS");
		}
	}
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = dateObj.format(formatter);
		if(!getStatus().equals("PUBLISHED") && Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now)<0 && !isBatchInstance()){
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
		if (Tool.isEmpty(msg)){
			return super.preUpdate();
		}

		return msg;
	}
	/*
		Function for calculated expression of field pmVrsCompletion
	*/
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
	/*
		Function of action PM_DEFER_TASK
	*/ 
	public String deferTask(){
		String msg = new String();
		List<String> taskSelected= getSelectedIds();
		AppLog.info("DEBUG "+taskSelected, getGrant());
		String[] selected= getAction("PM_DEFER_TASK").getConfirmField(getGrant().getLang(), "pmDtVrsVersion").getValue().split(":");
		if(!selected[0].equals("PmVersion")){
			msg= Message.formatError("PM_ERR_DEFER_TASK_OBJECT_TYPE", null, "pmVrsStatus");
		}else{
			ObjectDB tmpVrs = this.getGrant().getTmpObject("PmVersion");
			synchronized(tmpVrs){
				tmpVrs.select(selected[1]);
				if (tmpVrs.getStatus().equals("PUBLISHED")){
					msg= Message.formatError("PM_ERR_TSK_VRS_STATUS", null, null);
				}else{
					ObjectDB tmpTask = getGrant().getTmpObject("PmTask");
					synchronized(tmpTask){
						tmpTask.resetFilters();
						tmpTask.setFieldFilter("pmTskVrsId", getRowId());
						for(String[] row : tmpTask.search()){
							tmpTask.select(row[0]);
							if(tmpTask.getStatus().equals("DRAFT") || tmpTask.getStatus().equals("TODO") || tmpTask.getStatus().equals("DOING") ){
								tmpTask.setFieldValue("pmTskVrsId", selected[1]);
								tmpTask.save();
							}
							
						}
					}
				}
				
			}
		}
		
		
		
		return msg;
	}
	
	
}
