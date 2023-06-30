package com.simplicite.objects.ProjectManagement;

import java.util.*;
import org.json.*;

import com.simplicite.commons.ProjectManagement.pmRoleTool;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTask
 */
public class PmTask extends ObjectDB {
	
	private static final long serialVersionUID = 1L;
	private static final String ACT_ASSIGN = "PM_ASSIGN"; 
	/**
	 * Check if user is assign on task
	 * @return true if current user is assign on task.
	 */
	public boolean isUserAssignOn() {
		
		ObjectDB o =getGrant().getTmpObject("PmAssignment");
		synchronized(o){
			o.getLock();
			BusinessObjectTool ot =o.getTool();
			o.setFieldFilter("pmAssPmTaskid", getRowId());
			o.setFieldFilter("pmAssPmUserid", getGrant().getUserId());
			try {
				List<String[]> res = ot.search();
				if (!res.isEmpty()){
					return true;
				}
					
			} catch (SearchException e) {
				AppLog.error(e, getGrant());
			}
		}
		return false;
	}
	@Override
	public boolean isListUpsertable() {
		//Allow upsert in listpanel in version if user is user on project
		ObjectDB parent = getParentObject();	
		if(!Tool.isEmpty(parent) && parent.getName().equals("PmVersion") && pmRoleTool.isRoleOnProject("USER",parent.getFieldValue("pmVrsPrjId"),getGrant())){
			
			return true;
		}
		return super.isListUpsertable();
	}
	@Override
	public boolean isUpdateEnable(String[] row) {
		// set some task readonly for specificprofile
		if(!isNew() && getGrant().hasResponsibility("PM_USER_GROUP") && !getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			if(!isUserAssignOn()) return false;
		}else if(!isNew() && getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			if(!pmRoleTool.isRoleOnProject("MANAGER",row[getFieldIndex("pmVrsPrjId")],getGrant()))
				return false;
		}
		return super.isUpdateEnable(row);
	}


	
	/**
	 * calculate Number of task 
	 * @return the first none used number
	 */
	public String autoGenNumber(){

		
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		int number=1;
		List<Integer> listExist= new ArrayList<>();
		synchronized(tmpTask){
			tmpTask.getLock();
			tmpTask.resetFilters();
			tmpTask.setFieldFilter("pmTskVrsId", getFieldValue("pmTskVrsId"));// number is unique per version
			
			for(String[] row : tmpTask.search()){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
				if(!row[0].equals(getRowId()) ){
					tmpTask.select(row[0]);
					listExist.add(Integer.parseInt(tmpTask.getFieldValue("pmTskNumber")));
				}
			}
		}
		while(listExist.contains(number)){
			number+=1;
		}
		return String.format("%04d", number);
	}
	
	@Override
	public void initRefSelect(ObjectDB parent) {
		// set filter for PmArrayOfTask. display only task of the same project in search.
		if ("PmArrayOfTask".equals(parent.getName()) && (!Tool.isEmpty(parent.getFieldValue("pmAotPrvTskId"))||!Tool.isEmpty(parent.getFieldValue("pmAotNextTskId")))){
			setFieldFilter("pmVrsPrjId", (Tool.isEmpty(parent.getFieldValue("pmAotPrvTskId"))?parent.getFieldValue("pmAotNextTskId.pmTskVrsId.pmVrsPrjId"):parent.getFieldValue("pmAotPrvTskId.pmTskVrsId.pmVrsPrjId")));
	}else resetFilters(); 
	}
	
	@Override
	public void preSearch() {
		
		if(isProcessInstance() && getGrant().hasParameter("NEW_TASK_FILTERS")){
			// if is process create task and project have been selected display only the task of this project
			HashMap<String, String>  filters=(HashMap<String, String>) getGrant().getObjectParameter("NEW_TASK_FILTERS");
			setFieldFilter("pmPrjName", filters.get("pmPrjName"));
			//don't display the task creating
			setFieldFilter("row_id", "!= '"+filters.get("tskID")+"'"); 
			
			
		}else if(isHomeInstance() && getGrant().hasResponsibility("PM_USER_GROUP") && !getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			// if user is only user filter the home page display only task assign to the user
			getRowIdField().setAdditionalSearchSpec("EXISTS (SELECT * FROM pm_assignment WHERE pm_ass_pm_taskid = t.row_id AND pm_ass_pm_userid="+getGrant().getUserId()+")");
		}else if(isHomeInstance() && getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			// if user is manager and not admin display only task of project manage by user
			getField("pmVrsPrjId").setAdditionalSearchSpec("EXISTS (SELECT * FROM pm_role WHERE pm_rol_prj_id = t_pmVrsPrjId.row_id AND pm_rol_usr_id="+getGrant().getUserId()+")");
		}else if (isHomeInstance() && getGrant().hasParameter("PROJECT_ID")){
			// if is view in project display task assign to user
			getRowIdField().setAdditionalSearchSpec("EXISTS (SELECT * FROM pm_assignment WHERE pm_ass_pm_taskid = t.row_id AND pm_ass_pm_userid="+getGrant().getUserId()+")");
			setFieldFilter("pmVrsPrjId", getGrant().getParameter("PROJECT_ID"));
		}else if (isHomeInstance() && getGrant().hasParameter("VERSION_ID")){
			// if is view in project display task assign to user
			getRowIdField().setAdditionalSearchSpec("EXISTS (SELECT * FROM pm_assignment WHERE pm_ass_pm_taskid = t.row_id AND pm_ass_pm_userid="+getGrant().getUserId()+")");
			setFieldFilter("pmTskVrsId", getGrant().getParameter("VERSION_ID"));
		}
		super.preSearch();
	}
	@Override
	public List<String[]> postSearch(List<String[]> rows) {
		//reset search spec and filters
		getRowIdField().setAdditionalSearchSpec(null);
		getField("pmVrsPrjId").setAdditionalSearchSpec(null);
		if (isHomeInstance()){
			getField("pmVrsPrjId").resetFilter();
			getGrant().removeParameter("PROJECT_ID");
			getField("pmTskVrsId").resetFilter();
			getGrant().removeParameter("VERSION_ID");
		}
		return super.postSearch(rows);
	}
	@Override
	public void initCreate() {
		HashMap<String, String> filters = new HashMap<>();
		filters.put("pmVrsStatus", "ALPHA;BETA");
		getGrant().setParameter("PARENT_FILTERS", filters);
		super.initCreate();
	}
	@Override
	public void initUpdate(){			
		HashMap<String, String> filters = new HashMap<>();
		filters.put("pmVrsStatus", "ALPHA;BETA");
		filters.put("pmVrsPrjId", getFieldValue("pmVrsPrjId"));
		setFieldValue("pmTskTimeLeft", timeLeft());
		setFieldValue("pmTskActualDuration", actualDuration());
		getGrant().setParameter("PARENT_FILTERS", filters);
	}
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		if(!getStatus().equals("CLOSED") && getFieldValue("pmTskVrsId.pmVrsStatus").equals("PUBLISHED") && !isBatchInstance()){
			msgs.add(Message.formatError("PM_ERR_TSK_VRS_STATUS",null,"pmTskVrsId.pmVrsStatus"));
		}
		
		List<String> msgsSuper =super.postValidate();
		if (!Tool.isEmpty(msgsSuper)) msgs.addAll(msgsSuper);
		return msgs;
	}
	@Override
	public String postCreate() {
		ObjectDB o = getGrant().getTmpObject("PmVersion");
		synchronized(o){
			o.getLock();
			BusinessObjectTool ot = o.getTool();
			try {
				ot.selectForUpdate(getFieldValue("pmTskVrsId"));
				o.invokeMethod("completionVersion", null, null);
				ot.validateAndUpdate();
			} catch (GetException | MethodException | UpdateException | ValidateException e) {
				AppLog.error(e, getGrant());
			}
		}
		return super.postCreate();
	}
	@Override
	public String postUpdate() {
		List<String> endList = new ArrayList<>(Arrays.asList("CLOSED", "CANCEL", "REJECTED"));
		ObjectField status = getField("pmTskStatus");
		if(getField("pmTskVrsId").hasChanged() && !"PM_DEFER_TASK".equals(getInstanceName())|| (status.hasChanged() && ((endList.contains(status.getOldValue()) && !endList.contains(status.getValue()))|| (!endList.contains(status.getOldValue()) && endList.contains(status.getValue()))))){
			ObjectDB o = getGrant().getTmpObject("PmVersion");
			synchronized(o){
				o.getLock();
				BusinessObjectTool ot = o.getTool();
				try {
					ot.selectForUpdate(getFieldValue("pmTskVrsId"));
					o.setInstanceName("pm_completion_task");
					o.invokeMethod("completionVersion", null, null);
					ot.validateAndUpdate();
				} catch (GetException | MethodException | UpdateException | ValidateException e) {
					AppLog.error(e, getGrant());
				}
			}
			if(getField("pmTskVrsId").hasChanged()){
				o = getGrant().getTmpObject("PmVersion");
				synchronized(o){
					o.getLock();
					BusinessObjectTool ot = o.getTool();
					try {
						ot.selectForUpdate(getFieldOldValue("pmTskVrsId"));
						o.setInstanceName("pm_completion_task");
						o.invokeMethod("completionVersion", null, null);
						ot.validateAndUpdate();
					} catch (GetException | MethodException | UpdateException | ValidateException e) {
						AppLog.error(e, getGrant());
					}
				}
			}
			
			
		}
		
		if(getStatus().equals("TODO") && !getOldStatus().equals("DOING")){// If task is toDo status from other status expte Doing we have to inrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				tmpAssignment.getLock();
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row : tmpAssignment.search()){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("increaseUserNbTask",null, null);
					} catch (MethodException e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
			}
		
		}else if((getStatus().equals("DONE") || getStatus().equals("CANCEL") || getStatus().equals("REJECTED")) && (getOldStatus().equals("TODO") || getOldStatus().equals("DOING") )){// if task is done,cancel or rejected from to do or doing status we have to decrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				tmpAssignment.getLock();
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row :tmpAssignment.search()){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (MethodException e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
				
			}
		
		}
		
		return super.postUpdate();
	}
	@Override
	public String postDelete() {
		if(getStatus().equals("TODO") || getStatus().equals("DOING")){// if task is to do or doing status at delete  we have to decrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				tmpAssignment.getLock();
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row : tmpAssignment.search()){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (MethodException e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
				
			}
		
		}
		return super.preDelete();
	}
	/*
		Function for calculated expression of field pmTskActualDuration in PmTask
	*/ 
	public int actualDuration(){//used by the calculated field pmTskActualDuraition
		String begin = getFieldValue("pmTskCreation");
		String  end = getFieldValue("pmTskEffectiveClosingDate");
		if(Tool.isEmpty(end))
			end=Tool.getCurrentDate();
		return Tool.diffDate(begin, end);
	}
	/*
		Function for calculated expression of field pmTskTimeLeft in PmTask
	*/ 
	public int timeLeft(){//used by the calculated field pmTskActualDuraition
		switch(getFieldValue("pmTskStatus")){
			case "CANCEL":
			case "REJECTED":
			case "CLOSED":
				return 1000;
			default:
				return Tool.diffDate(Tool.getCurrentDate(),getFieldValue("pmTskClose"));
				
		}
	}
	/*
		Function of action PM_TASK_MSG_DELETION
	*/ 
	public List<String> taskMsgDeletion(){
		List<String> selected= getSelectedIds();
		
		List<String> msgs = new ArrayList<>();
		if(Tool.isEmpty(selected)){
			msgs.add(Message.formatError("PM_ERR_EMPTY_SELECT",null,null));
			return msgs;
		}
		ObjectDB tmpTask= getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			tmpTask.getLock();
			for(String id : selected){
				tmpTask.select(id);
				ObjectDB tmpMsg = getGrant().getTmpObject("PmMessage");
				BusinessObjectTool ot = tmpMsg.getTool();
				synchronized(tmpMsg){
					tmpMsg.getLock();
					tmpMsg.resetFilters();
					tmpMsg.setFieldFilter("pmMsgTskId", tmpTask.getRowId());
					for(String[] row : tmpMsg.search()){
						try {
							ot.getForDelete(row[0]);
						} catch (GetException e) {
							msgs.add(Message.formatError(null,e.toString(),null));
							continue;
						}
						tmpMsg.select(row[0]);
						String errDelete =tmpMsg.delete();
						if (errDelete != null){
							msgs.add(Message.formatError(null,errDelete,null));
						}
					}
				}
			}
			
		}
		return msgs;
	}
	/*
		Function of action PM_ASSIGN
	*/ 
	public List<String> pmTaskAssign(){
		List<String> sltTsks= getSelectedIds();
		List<String> msgs = new ArrayList<>();
		if(Tool.isEmpty(sltTsks)){
			msgs.add(Message.formatError("PM_ERR_ASSIGN_NO_TASK", null, null));
			return msgs;
		}
		String[] sltUser= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssUser").getValue().split(":");
		if(!sltUser[0].equals("PmUser")){
			msgs.add(Message.formatError("PM_ERR_ASSIGN_OBJECT_TYPE", null, "pmTskActAssUser"));
		}else{
			String sltRole= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssRole").getValue();
			String sltQuantity= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssQuantity").getValue();
			String sltUsrId= sltUser[1];
			ObjectDB tmpAss= getGrant().getTmpObject("PmAssignment");
			BusinessObjectTool ot = tmpAss.getTool();
			synchronized(tmpAss){	
				tmpAss.getLock();	
				for(String sltTskId:sltTsks){
					try {
						
						// Get an existing record or an empty record based on functional keys filters
						// False means no record was found => creation
						if (!ot.getForCreateOrUpdate(new JSONObject() // or its alias getForUpsert 
							.put("pmAssPmUserid", sltUsrId)
							.put("pmAssPmTaskid", sltTskId)
							)) {
							// Set functional keys fields
							tmpAss.setFieldValue("pmAssPmUserid", sltUsrId);
							tmpAss.setFieldValue("pmAssPmTaskid", sltTskId);
						}
						tmpAss.setFieldValue("pmAssRole", sltRole);
						tmpAss.setFieldValue("pmAssQuantity", sltQuantity);
						ot.validateAndSave();
					} catch (JSONException|GetException|ValidateException|SaveException e) {
						AppLog.error(e, getGrant());
						msgs.add(Message.formatError("PM_ERR_TSK_SAVE_UP_ASSIGN", sltTskId, null));
					}
				}
			}
		}
		
		return msgs;
		
		
	}
	public void pmTaskNotify() {
		getField("pmTskClose").setAdditionalSearchSpec("t.pm_tsk_close < CAST(CURRENT_TIMESTAMP as DATE) and t.pm_tsk_status in ('DRAFT','TODO', 'DOING', 'DONE')");
		for (String[] row : search()){
			if(Integer.parseInt(row[getFieldIndex("pmTskTimeLeft")])<0){
				ObjectDB o = getGrant().getTmpObject("PmTask");
				synchronized(o){
					o.getLock();
					o.select(row[0]);
					BusinessObjectTool ot = o.getTool();
					try {
						ot.selectForUpdate(row[0]);
						o.setFieldValue("pmTskLastCron", Tool.getCurrentDateTime());
						ot.validateAndSave();
					} catch (GetException | ValidateException | SaveException e) {
						AppLog.error(e, getGrant());
						return;
					}
				} 
			}
		}
		getField("pmTskClose").setAdditionalSearchSpec(null);
	}

	/**
	 * For action PM_UPDATE_GANTT use in project gantt view
	 * @param params {tsk: <JSON of task to update {tsk_id: {start:<date>,end:<date>,progress: <%>},...}>}
	 * @throws GetException
	 * @throws ValidateException
	 * @throws SaveException
	 */
	public void actionUpdateGantt(Map<String, String> params) throws GetException, ValidateException, SaveException {
		JSONObject tsks = new JSONObject(params.get("tsk").toString());
		BusinessObjectTool ot = this.getTool();
		for (Iterator<String> iterator = tsks.keys(); iterator.hasNext();) {
			String rowid = iterator.next();
			JSONObject data= tsks.getJSONObject(rowid);
			synchronized(this){
				getLock();
				ot.getForUpdate(rowid);
				if(data.has("start"))
					setFieldValue("pmTskCreation", data.get("start"));
				if(data.has("end"))
					setFieldValue("pmTskClose", data.get("end"));
				if(data.has("progress"))
					setFieldValue("pmTskCompletion", data.get("progress"));
				ot.validateAndSave();
				
			}
			
		}
		
		
	}
}