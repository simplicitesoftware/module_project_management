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
		AppLog.info("DEBUG: postLoad", getGrant());
		getField("row_module_id").setDefaultValue(this.getModuleId());
		super.postLoad();
	}
	@Override
	public List<String> preValidate() {
		List<String> msgs = new ArrayList<>();
		AppLog.info("DEBUG: preValidate", getGrant());
		//msgs.add(Message.formatInfo("INFO_CODE", "Message", "fieldName"));
		//msgs.add(Message.formatWarning("WARNING_CODE", "Message", "fieldName"));
		//msgs.add(Message.formatError("ERROR_CODE", "Message", "fieldName"));
		
		
		
		return msgs;
	}
	@Override
	public String preCreate() {
		AppLog.info("DEBUG: preCreate", getGrant());
		
		//return Message.formatInfo("INFO_CODE", "Message", "fieldName");
		//return Message.formatWarning("WARNING_CODE", "Message", "fieldName");
		//return Message.formatError("ERROR_CODE", "Message", "fieldName");
		return null;
	}
	@Override
	public String postCreate() {
		BusinessObjectTool uot =getTool();
		try {
			AppLog.info("DEBUG: "+getModuleId(), getGrant());
			setFieldValue("row_module_id",getModuleId());
			uot.validateAndSave();
		} catch (Exception e) {
			AppLog.error(e, getGrant());
		}
		//return Message.formatInfo("INFO_CODE", "Message", "fieldName");
		//return Message.formatWarning("WARNING_CODE", "Message", "fieldName");
		//return Message.formatError("ERROR_CODE", "Message", "fieldName");
		//return HTMLTool.redirectStatement(HTMLTool.getFormURL("Object", null, "123", "nav=add"));
		//return HTMLTool.redirectStatement(HTMLTool.getListURL("Object", null, "nav=add"));
		//return HTMLTool.javascriptStatement("/* code */");
		return null;
	}
}
