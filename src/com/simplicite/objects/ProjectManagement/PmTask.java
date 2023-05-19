package com.simplicite.objects.ProjectManagement;

import java.util.*;
import org.json.*;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTask
 */
public class PmTask extends ObjectDB {
	private static final long serialVersionUID = 1L;

	/*
		Function for calculat Number of task 
		(return the first none used number)
	*/ 
	public boolean isInt(String str){
        return str.matches("[-,+]?\\d+");
    }
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
		
		if ("PmArrayOfTask".equals(parent.getName()) && (!Tool.isEmpty(parent.getFieldValue("pmAotPrvTskId"))||!Tool.isEmpty(parent.getFieldValue("pmAotNextTskId"))))
			setFieldFilter("pmVrsPrjId", (Tool.isEmpty(parent.getFieldValue("pmAotPrvTskId"))?parent.getFieldValue("pmAotNextTskId.pmTskVrsId.pmVrsPrjId"):parent.getFieldValue("pmAotPrvTskId.pmTskVrsId.pmVrsPrjId")));
		else resetFilters(); 
	}
	
	@Override
	public void preSearch() {
		if("bpm_ajax_PmTask".equals(getInstanceName()) && getGrant().hasParameter("NEW_TASK_FILTERS")){
			HashMap<String, String>  filters=(HashMap<String, String>) getGrant().getObjectParameter("NEW_TASK_FILTERS");
			setFieldFilter("pmPrjName", filters.get("pmPrjName"));
			setSearchSpec(getSearchSpec()+" AND NOT t.row_id = "+filters.get("tskID"));
		}
		super.preSearch();
	}
	@Override
	public List<String[]> postSearch(List<String[]> rows) {
		setSearchSpec(getDefaultSearchSpec());
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
		getGrant().setParameter("PARENT_FILTERS", filters);
	}
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		if(!getStatus().equals("CLOSED") && getFieldValue("pmTskVrsId.pmVrsStatus").equals("PUBLISHED") && !isBatchInstance()){
			msgs.add(Message.formatError("PM_ERR_TSK_VRS_STATUS",null,"pmTskVrsId.pmVrsStatus"));
		}
		if (getFieldValue("pmTskNumber").equals("0")){
			setFieldValue("pmTskNumber", autoGenNumber());
			save();
		}
		
		List<String> msgsSuper =super.postValidate();
		if (!Tool.isEmpty(msgsSuper)) msgs.addAll(msgsSuper);
		return msgs;
	}
	@Override
	public String postUpdate() {
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
	private static final String ACT_ASSIGN = "PM_ASSIGN"; 
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
	
}