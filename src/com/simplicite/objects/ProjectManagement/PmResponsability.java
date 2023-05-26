package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmResponsability
 */
public class PmResponsability extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void postLoad() {
		// set default to projectManagement
		getField("row_module_id").setDefaultValue(ModuleDB.getModuleId("ProjectManagment"));
		super.postLoad();
	}
	
	@Override
	public String postCreate() {
		BusinessObjectTool uot =getTool();
		try {
			// force module to projectManagement
			setFieldValue("row_module_id",ModuleDB.getModuleId("ProjectManagment"));
			uot.validateAndSave();
		} catch (Exception e) {
			AppLog.error(e, getGrant());
		}
		return null;
	}
}
