package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTskLbl
 */
public class PmTskLbl extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void initCreate() {
		if(isProcessInstance()){
			for(ObjectField field :getFields()){
				AppLog.info("DEBUG: "+field.getName()+" "+field.getRefObjectName() , getGrant());
				if (Tool.isEmpty(field.getRefObjectName()) && !field.getName().equals("pmTsklblTskId")||!Tool.isEmpty(field.getRefObjectName()) && field.getRefObjectName().equals("PmLabel")){
					field.setValue("");
				}
			}
			save();
		}
		super.initCreate();
	}
}
