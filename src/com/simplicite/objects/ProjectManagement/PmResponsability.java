package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;


/**
 * Business object PmResponsability
 */
public class PmResponsability extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void postLoad() {
		// set default to projectManagement
		getField("row_module_id").setDefaultValue(ModuleDB.getModuleId("ProjectManagement"));
		
		super.postLoad();
	}
	@Override
	public void initCreate() {
		setFieldValue("row_module_id",ModuleDB.getModuleId("ProjectManagement"));
		super.initCreate();
	}
	@Override
	public List<String> preValidate() {
		setFieldValue("row_module_id",ModuleDB.getModuleId("ProjectManagement"));
		return super.preValidate();
	}
}
