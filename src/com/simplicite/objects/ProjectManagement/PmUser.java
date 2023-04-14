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
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		getField("row_module_id").setDefaultValue(this.getModuleId());
		super.postLoad();
	}
	@Override
	public String postCreate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		//setFieldValue("usr_active", 1);
		ObjectDB tmpUser = this.getGrant().getTmpObject("SimpleUser");
		synchronized(tmpUser){
			
			AppLog.info("DEBUG POST CREATE user: select "+getRowId()+" "+tmpUser.select(getRowId()), getGrant());
			
		}
		return super.postCreate();
	}
	@Override
	public List<String> postValidate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.postValidate();
	}
	@Override
	public String preCreate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.preCreate();
	}
	@Override
	public void preLoad() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		super.preLoad();
	}
	@Override
	public String preSave() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.preSave();
	}
	@Override
	public String preUpdate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.preUpdate();
	}
	@Override
	public List<String> preValidate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.preValidate();
	}
	@Override
	public String postSave() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		ObjectDB tmpUser = this.getGrant().getTmpObject("SimpleUser");
		synchronized(tmpUser){
			
			AppLog.info("DEBUG POST CREATE user: select "+getRowId()+" "+tmpUser.select(getRowId()), getGrant());
			
		}
		return super.postSave();
	}
	@Override
	public String postUpdate() {
		AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant());
		return super.postUpdate();
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
