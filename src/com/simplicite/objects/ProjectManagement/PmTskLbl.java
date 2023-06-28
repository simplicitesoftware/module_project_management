package com.simplicite.objects.ProjectManagement;
import com.simplicite.util.*;

/**
 * Business object PmTskLbl
 */
public class PmTskLbl extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public void initUpdate() {
		if(isProcessInstance()){
			for(ObjectField field :getFields()){
				if (Tool.isEmpty(field.getRefObjectName()) && !field.getName().equals("pmTsklblTskId")||!Tool.isEmpty(field.getRefObjectName()) && field.getRefObjectName().equals("PmLabel")){
					field.setValue("");
				}
			}
			save();
		}
		super.initCreate();
	}
}