package com.simplicite.objects.ProjectManagement;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;
import java.util.List;
import com.simplicite.objects.System.*;

/**
 * Business object PmUser
 */
public class PmUser extends SimpleUser {
	private static final long serialVersionUID = 1L;
	@Override
	public void postLoad() {
		getField("row_module_id").setDefaultValue(ModuleDB.getModuleId("ProjectManagement"));
		super.postLoad();
	}
	@Override
	public List<String> preValidate() {
		setFieldValue("row_module_id",ModuleDB.getModuleId("ProjectManagement"));
		return super.preValidate();
	}
	@Override
	public String preCreate() {
		setFieldValue("usr_active",GrantCore.STATUS_ACTIVE);
		return super.preCreate();
	}
	@Override()
	public String postCreate() {
		ObjectDB tmpResp = this.getGrant().getTmpObject("PmResponsability");
		synchronized(tmpResp){
			tmpResp.getLock();
			BusinessObjectTool ot = tmpResp.getTool();
			ObjectDB tmpGroup = this.getGrant().getTmpObject("PmGroup");
			synchronized(tmpGroup){
				tmpGroup.getLock();
				BusinessObjectTool otG = tmpGroup.getTool();
				
				tmpGroup.setFieldFilter("grp_name","PM_USER_GROUP");
				try {
					List<String[]> rows = otG.search();
					if (!rows.isEmpty()){
						ot.selectForCreate();
						tmpResp.setFieldValue("rsp_group_id", rows.get(0)[0]);
						tmpResp.setFieldValue("rsp_login_id", getRowId());
						tmpResp.setFieldValue("row_module_id", getModuleId());
						ot.validateAndSave();
						
					}
					
				} catch (SearchException|GetException|ValidateException|SaveException e) {
					AppLog.error(e, getGrant());
				}}
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
}