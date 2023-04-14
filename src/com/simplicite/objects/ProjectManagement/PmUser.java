package com.simplicite.objects.ProjectManagement;


import com.simplicite.util.*;
import com.simplicite.util.exceptions.ActionException;

import java.util.List;

import com.simplicite.objects.System.*;

/**
 * Business object PmUser
 */
public class PmUser extends SimpleUser {
	private static final long serialVersionUID = 1L;
	@Override
	public void postLoad() {
		getField("row_module_id").setDefaultValue(this.getModuleId());
		super.postLoad();
	}
	@Override
	public String postCreate() {
		//setFieldValue("usr_active", 1);
		ObjectDB tmpUser = this.getGrant().getTmpObject("SimpleUser");
		synchronized(tmpUser){
			
			AppLog.info("DEBUG POST CREATE user: select "+getRowId()+": "+tmpUser.select(getRowId()), getGrant());
			
		}
		return super.postCreate();
	}
	@Override
	public String postUpdate() {
		ObjectDB tmpUser = this.getGrant().getTmpObject("SimpleUser");
		synchronized(tmpUser){
			
			AppLog.info("DEBUG POST Validate user: select "+getRowId()+": "+tmpUser.select(getRowId()), getGrant());
			
		}
		return super.postValidate();
	}
	/* 
		Fonctions for update pmUsrNbTask
	 */
	public void increaseNbTask(){
		ObjectField nbTask = this.getField("pmUsrNbTask");
    	nbTask.setValue(nbTask.getInt(0)+1);
    	save();
	}
	public void decreaseNbTask(){
		ObjectField nbTask = this.getField("pmUsrNbTask");
    	if (nbTask.getInt(0) > 0){
				nbTask.setValue(nbTask.getInt(0)-1);	
		}else{
			nbTask.setValue(0);
		}
    	save();
	}
}
