package com.simplicite.objects.ProjectManagement;

import java.util.*;

import javax.validation.constraints.NotEmpty;

import org.json.JSONObject;

import com.simplicite.commons.ProjectManagement.pmRoleTool;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.BusinessObjectTool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Business object PmVersion
 */
public class PmVersion extends ObjectDB {
	private static final long serialVersionUID = 1L;
	static String statusPUBLISHED ="PUBLISHED";
	static String fieldStatus="pmVrsStatus";
	static String fieldPrjId="pmVrsPrjId";
	@Override
	public boolean isReadOnly() {
		if(!isNew() && getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			ObjectDB o =getGrant().getTmpObject("PmProject");
			synchronized(o){
				o.getLock();
				o.select(getFieldValue("pmVrsPrjId"));
				if(o.isReadOnly()) return true;
			}
		}
		
		return super.isReadOnly();
	}
	@Override
	public void initUpdate(){			
		HashMap<String, String> filters = new HashMap<>();
		filters.put(fieldPrjId, getFieldValue(fieldPrjId));
		filters.put(fieldStatus, "ALPHA;BETA");
		filters.put("row_id", getRowId());// use for deffer task
		getGrant().setParameter("PARENT_FILTERS", filters);
		getGrant().setParameter("VERSION_ID", getRowId());
	}
	
	@Override
	public void preSearch(){
		if(isRefInstance() && getGrant().hasParameter("PARENT_FILTERS")){
			HashMap<String, String> filters = (HashMap<String, String>) getGrant().getObjectParameter("PARENT_FILTERS");
			String vrsRowId=filters.get("row_id");
			if(!Tool.isEmpty(vrsRowId)){//dont show curent version when defer task
				setFieldFilter("row_id", "!= '"+vrsRowId+"'");
			}			
			setFieldFilter(fieldPrjId, filters.get(fieldPrjId));
			setFieldFilter(fieldStatus, filters.get(fieldStatus));
			getGrant().removeParameter("PARENT_FILTERS");
		}
	}
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = dateObj.format(formatter);
		if(!getStatus().equals(statusPUBLISHED) && Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now)<0 && !isBatchInstance()){
			msgs.add(Message.formatError("PM_ERR_PUBLICATIONDATE", null, "pmVrsPublicationDate"));
		}
		
		return msgs;
	}
	@Override
	public String preUpdate() {
		String msg="";
		// release management
		if(getStatus().equals(statusPUBLISHED) && !getOldStatus().equals(statusPUBLISHED)){
			ObjectDB tmpTask= this.getGrant().getTmpObject("PmTask");
			synchronized(tmpTask){
				tmpTask.getLock();
				tmpTask.resetFilters();
				tmpTask.setFieldFilter("pmTskVrsId", getRowId());
				for(String[] row : tmpTask.search()){
					tmpTask.select(row[0]);
					String status=tmpTask.getStatus();
					if(status.equals("TODO") || status.equals("DOING") || status.equals("DONE") || status.equals("DRAFT")){
							msg=Message.formatError("PM_ERR_PUBLICATION_STATUS", null, fieldStatus);
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
			tmpTask.getLock();
			tmpTask.resetFilters();
			tmpTask.setFieldFilter("pmTskVrsId", getRowId());
			for(String[] row : tmpTask.search()){
				taskCount+=1;
				tmpTask.select(row[0]);
				String status=tmpTask.getStatus();
				if (status.equals("CLOSED") || status.equals("CANCEL") || status.equals("REJECTED")){
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
		String msg = "";
		String[] selected= getAction("PM_DEFER_TASK").getConfirmField(getGrant().getLang(), "pmDtVrsVersion").getValue().split(":");
		if(!selected[0].equals("PmVersion")){
			msg= Message.formatError("PM_ERR_DEFER_TASK_OBJECT_TYPE", null, fieldStatus);
		}else{
			ObjectDB tmpVrs = this.getGrant().getTmpObject("PmVersion");
			synchronized(tmpVrs){
				tmpVrs.getLock();
				tmpVrs.select(selected[1]);
				if (tmpVrs.getStatus().equals(statusPUBLISHED)){
					msg= Message.formatError("PM_ERR_TSK_VRS_STATUS", null, null);
				}else{
					ObjectDB tmpTask = getGrant().getTmpObject("PmTask");
					synchronized(tmpTask){
						tmpTask.getLock();
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

	/**
	 * For action PM_UPDATE_GANTT use in project gantt view
	 * @param params {tsk: <JSON of version to update {vrs_id: {end:<date>},...}>}
	 *  @return list of message throw by get validate and save method.
	 */
	public List<String> actionUpdateGantt(Map<String, String> params)  {
		List<String> msgs = new ArrayList<>();
		JSONObject tsks = new JSONObject(params.get("tsk").toString());
		BusinessObjectTool ot = this.getTool();
		try {
			for (Iterator iterator = tsks.keys(); iterator.hasNext();) {
			String rowid = iterator.next().toString();
			JSONObject data= tsks.getJSONObject(rowid);
			synchronized(this){
				getLock();
				
					ot.getForUpdate(rowid);
				
				if(data.has("end"))
					setFieldValue("pmVrsPublicationDate", data.get("end"));
				ot.validateAndSave();
				
			}
		}
		} catch (GetException | ValidateException | SaveException e) {
			msgs.addAll(Arrays.asList(e.getMessages(getGrant())));
		}
		return msgs;
	}
	
	
}