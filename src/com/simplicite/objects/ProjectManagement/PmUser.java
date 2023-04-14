package com.simplicite.objects.ProjectManagement;


import com.simplicite.util.*;
import com.simplicite.util.exceptions.ActionException;
import com.simplicite.util.exceptions.DeleteException;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.exceptions.SaveException;
import com.simplicite.util.exceptions.ValidateException;
import com.simplicite.util.tools.BusinessObjectTool;

import java.util.ArrayList;
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
		/* AppLog.info("DEBUG "+new Object() {}
		.getClass()
		.getEnclosingMethod()
		.getName() , getGrant()); */
		//setFieldValue("usr_active", 1);
		
		return super.postCreate();
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
	/*
		Function of action PM_USER_GROUP
	*/ 
	
	public List<String> pmUserGroup(){
		List<String> msgs = new ArrayList<>();
		String[] sltGroup= getAction("PM_USER_GROUP").getConfirmField(getGrant().getLang(), "pmUserGroup").getValue().split(":");
		ObjectDB tmpResp = this.getGrant().getTmpObject("Responsability");
		BusinessObjectTool ot = tmpResp.getTool();
		tmpResp.resetFilters();
		tmpResp.setFieldFilter("rsp_login_id", getGrant().getUserId());
		synchronized(tmpResp){
			try{
				/* for(String[] row : tmpResp.search()){
						
						ot.getForDelete(row[0]);
						ot.delete();
						
					
					
				} */
				ot.selectForCreate();
				tmpResp.setFieldValue("rsp_login_id", getGrant().getUserId());
				tmpResp.setFieldValue("rsp_group_id", sltGroup[1]);
				tmpResp.setFieldFilter("row_module_id", getModuleId());
				tmpResp.setFieldFilter("rsp_start_dt", Tool.getCurrentDate());
				ot.validateAndSave();
				
			}catch(GetException|ValidateException|SaveException/*| DeleteException */ e){
				AppLog.error(e, getGrant());
				msgs.add(Message.formatError("PM_RESP_ERR", null, null));
			}
		}
				
		return msgs;
	}
}
