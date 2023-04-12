package com.simplicite.objects.ProjectManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTask
 */
public class PmTask extends ObjectDB {
	private static final long serialVersionUID = 1L;
	public int autoGenNumber(){
		int number =1;
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		List<Integer> listExist= new ArrayList<>();
		tmpTask.resetFilters();
		tmpTask.setFieldFilter("pmTskVrsId", getFieldValue("pmTskVrsId"));// number is unique per version
		synchronized(tmpTask){
			for(String[] row : tmpTask.search()){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
				tmpTask.select(row[0]);
				listExist.add(Integer.parseInt(tmpTask.getFieldValue("pmTskNumber")));
			}
		}
		while(listExist.contains(number)){
			number+=1;
		}
		return number;
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
		if(getFieldValue("pmTskVrsId.pmVrsStatus").equals("PUBLISHED") && !isBatchInstance()){
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
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row : tmpAssignment.search()){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("increaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
			}
		
		}else if((getStatus().equals("DONE") || getStatus().equals("CANCEL") || getStatus().equals("REJECTED")) && (getOldStatus().equals("TODO") || getOldStatus().equals("DOING") )){// if task is done,cancel or rejected from to do or doing status we have to decrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row :tmpAssignment.search()){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
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
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				for(String[] row : tmpAssignment.search()){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
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
		if(Tool.isEmpty(end)){
			LocalDate dateObj = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			end = dateObj.format(formatter);
		}
		return Tool.diffDate(begin, end);
	}
	/*
		Function for calculated expression of field pmTskCompletion in PmTask
	*/ 
	public int completionDuration(){//used by the calculated field pmTskActualDuraition
		String  expeted = getFieldValue("pmTskExpectedDuration");
		if(Tool.isEmpty(getFieldValue("pmTskEffectiveClosingDate"))){
			return actualDuration()*100/Integer.parseInt(expeted);
			
		}
		return 100;
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
			for(String id : selected){
				tmpTask.select(id);
				ObjectDB tmpMsg = getGrant().getTmpObject("PmMessage");
				BusinessObjectTool ot = tmpMsg.getTool();
				synchronized(tmpMsg){
					tmpMsg.resetFilters();
					tmpMsg.setFieldFilter("pmMsgTskId", tmpTask.getRowId());
					for(String[] row : tmpMsg.search()){
						try {
							ot.getForDelete(row[0]);
						} catch (Exception e) {
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
		List<String> selected= getSelectedIds();
		List<String> msgs = new ArrayList<>();
		
		String[] sltUser= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssRole").getValue().split(":");
		String[] sltRole= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssUser").getValue().split(":");
		String[] sltQuantity= getAction(ACT_ASSIGN).getConfirmField(getGrant().getLang(), "pmTskActAssQuantity").getValue().split(":");
		//DEBUG
		String tmpMsg="DEBUG: sltUser ";
		for (String tmp:sltUser) tmpMsg+=tmp+", ";
		tmpMsg+="sltRole ";
		for (String tmp:sltRole) tmpMsg+=tmp+", ";
		tmpMsg+="sltQuantity ";
		for (String tmp:sltQuantity) tmpMsg+=tmp+", ";
		AppLog.info(tmpMsg, getGrant());
		if(!sltUser[0].equals("PmUser")){
			msgs.add(Message.formatError("PM_ERR_ASSIGN_OBJECT_TYPE", null, "pmTskActAssUser"));
		}else{
			
			AppLog.info("DEBUG: selectedid "+selected, getGrant());
		}
		return msgs;
	}
}