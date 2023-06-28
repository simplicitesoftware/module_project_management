package com.simplicite.objects.ProjectManagement;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.MethodException;

/**
 * Business object PmAssignment
 */
public class PmAssignment extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void initUpdate() {
		if(isProcessInstance()){
			//specific init for process
			for(ObjectField field :getFields()){
				if (Tool.isEmpty(field.getRefObjectName()) && !field.getName().equals("pmAssPmTaskid")||!Tool.isEmpty(field.getRefObjectName()) && field.getRefObjectName().equals("PmUser")){
					field.setValue("");
				}
			}
			save();
		}
		super.initUpdate();
	}
	@Override
	public String postCreate() {// if we created assignment we have to update the number of task of user
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			tmpTask.getLock();
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(tmpTask.getStatus().equals("TODO") ||tmpTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we increase the number of task
				increaseUserNbTask();
			}
		}
		return super.postCreate();
	}
	@Override
	public String preDelete() {// if we deleted assignment we have to update the number of task of user
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			tmpTask.getLock();
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(tmpTask.getStatus().equals("TODO") ||tmpTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we decrease the number of task
				decreaseUserNbTask();
			}
		}
		return super.preDelete();
	}
	/**
	 * Fonction for increase pmUsrNbTask of PmUser
	 * invoke the increase methode of object user
	 */
	public void increaseUserNbTask(){
		ObjectDB tmpUser = this.getGrant().getTmpObject("PmUser");
        synchronized(tmpUser){
			tmpUser.getLock();
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpUser.select(this.getFieldValue("pmAssPmUserid"));
			try {
				tmpUser.invokeMethod("increaseNbTask",null, null);
			} catch (MethodException e) {
				AppLog.error(getClass(),"increaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            tmpUser.save();
        }
	}
	/**
	 * Fonction for decrease pmUsrNbTask of PmUser
	 * invoke the decrease methode of object user
	 */
	public void decreaseUserNbTask(){
		ObjectDB tmpUser = this.getGrant().getTmpObject("PmUser");
        synchronized(tmpUser){
			tmpUser.getLock();
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpUser.select(this.getFieldValue("pmAssPmUserid"));
			try {
				tmpUser.invokeMethod("decreaseNbTask",null, null);
			} catch (MethodException e) {
				AppLog.error(getClass(),"decreaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            tmpUser.save();
        }
	}
	

	
	
}