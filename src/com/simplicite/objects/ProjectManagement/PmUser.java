package com.simplicite.objects.ProjectManagement;


import com.simplicite.util.*;
import com.simplicite.util.exceptions.ActionException;
import com.simplicite.util.exceptions.DeleteException;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.exceptions.SaveException;
import com.simplicite.util.exceptions.ValidateException;
import com.simplicite.util.tools.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
		ObjectDB tmpResp = this.getGrant().getTmpObject("Responsability");
		BusinessObjectTool ot = tmpResp.getTool();
		try {
			ot.selectForCreate();
			tmpResp.setFieldValue("grp_name", "PM_USER_GROUP");
			tmpResp.setFieldValue("rsp_login_id", getRowId());
			tmpResp.setFieldValue("row_module_id", getModuleId());
			ot.validateAndSave();
		} catch (GetException|ValidateException|SaveException e) {
			AppLog.error(e, getGrant());
			
		}	
		
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
		String moduleId = getModuleId();
		tmpResp.resetFilters();
		tmpResp.setFieldFilter("rsp_login_id", getRowId()); 
		tmpResp.setFieldFilter("row_module_id",moduleId );
		AppLog.info("DEBUG USER id: "+getRowId(), getGrant());
		synchronized(tmpResp){
			try{
				for(String[] row : tmpResp.search()){
						
						ot.getForDelete(row[0]);
						ot.delete();
						
					
					
				}
				ot.selectForCreate();	
				tmpResp.setFieldValue("rsp_group_id", sltGroup[1]);
				tmpResp.setFieldValue("rsp_login_id", getRowId());
				tmpResp.setFieldValue("row_module_id", moduleId);
				ot.validateAndSave();
				
			}catch(GetException|ValidateException|SaveException| DeleteException e){
				AppLog.error(e, getGrant());
				msgs.add(Message.formatError("PM_RESP_ERR", null, null));
			}
		}
				
		return msgs;
	}
	/*
		Function for calculated expression of field pmUsrCurrentGroup in PmUser
	*/ 
	public String pmUsrCurentGroup() {
		String groupDisplay = "No group";
		ObjectDB tmpResp = this.getGrant().getTmpObject("Responsability");
		ObjectDB tmpTrad= this.getGrant().getTmpObject("TranslateGroup");
		BusinessObjectTool ot = tmpTrad.getTool();
		tmpResp.setFieldFilter("rsp_login_id", getRowId()); 
		tmpResp.setFieldFilter("row_module_id",getModuleId() );
		synchronized(tmpResp){
			List<String[]> SearchResult=tmpResp.search();
			if (SearchResult.size() > 1){
				AppLog.info(Message.formatError("PM_ERR_TOO_MANY_RESP", null, null), getGrant());
			}else if(SearchResult.size() >0){
				tmpResp.select(SearchResult.get(0)[0]);
				try {
					if (!ot.getForCreateOrUpdate(new JSONObject() // or its alias getForUpsert 
									.put("tsl_id",tmpResp.getFieldValue("rsp_group_id"))
									.put("tsl_lang",getGrant().getLang() )
									)){
										AppLog.info(Message.formatError("PM_GROUP_NO_TRAD", null, null), getGrant());
										groupDisplay = tmpResp.getFieldValue("grp_name") ;
									}else groupDisplay=tmpTrad.getFieldValue("tsl_value");
				} catch (GetException|JSONException e) {
					AppLog.error(e, getGrant());
				}
			}
			
			
		}
		return  groupDisplay;
		
	}
}
