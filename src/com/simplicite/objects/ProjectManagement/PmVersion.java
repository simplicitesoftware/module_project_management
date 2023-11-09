package com.simplicite.objects.ProjectManagement;
import java.util.*;
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
	private static String statusPUBLISHED ="PUBLISHED";
	private static String fieldStatus="pmVrsStatus";
	private static String fieldPrjId="pmVrsPrjId";
	@Override
	public boolean isUpdateEnable(String[] row) {
		if(!isNew() && getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			if(!pmRoleTool.isRoleOnProject("MANAGER",row[getFieldIndex("pmVrsPrjId")],getGrant()))
				return false;
		}
		return super.isUpdateEnable(row);
	}

	@Override
	public void initUpdate(){			
		HashMap<String, String> filters = new HashMap<>();
		filters.put(fieldPrjId, getFieldValue(fieldPrjId));
		filters.put(fieldStatus, "ALPHA;BETA");
		filters.put("row_id", getRowId());// use for deffer task
		getGrant().setParameter("PARENT_FILTERS", filters);
		AppLog.info("debug version",getGrant());
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
	public String postCreate() {
		completionVersion();
		return super.postCreate();
	}
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = dateObj.format(formatter);
		if(!getStatus().equals(statusPUBLISHED) && Tool.compareDate(getFieldValue("pmVrsPublicationDate"), now)<0 && !isBatchInstance() && !"pm_completion_task".equals(getInstanceName())){
			msgs.add(Message.formatError("PM_ERR_PUBLICATIONDATE", null, "pmVrsPublicationDate"));
		}else if("pm_completion_task".equals(getInstanceName())){
			msgs.add(Message.formatWarning("PM_ERR_PUBLICATIONDATE", null, "pmVrsPublicationDate"));
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
		int result =  (taskCount==0)?100:(finishedTaskCount*100)/taskCount;
		setFieldValue("pmVrsCompletion", result);
		return result;
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
								tmpTask.setInstanceName("PM_DEFER_TASK");
								tmpTask.save();
								
							}
							
						}
					}
				}
				
			}
			
			try {
				completionVersion();
				new BusinessObjectTool(this).validateAndSave();
				ObjectDB o = getGrant().getTmpObject("PmVersion");
				synchronized(o){
					o.getLock();
					BusinessObjectTool ot = o.getTool();
					try {
						ot.selectForUpdate(selected[1]);
						o.setInstanceName("pm_completion_task");
						o.invokeMethod("completionVersion", null, null);
						ot.validateAndUpdate();
					} catch (GetException | MethodException | UpdateException | ValidateException e) {
						AppLog.error(e, getGrant());
					}
				}
			} catch (ValidateException| SaveException e) {
				AppLog.error(e, getGrant());
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
			for (Iterator<String> iterator = tsks.keys(); iterator.hasNext();) {
			String rowid = iterator.next();
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