package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmArrayOfTask
 */
public class PmArrayOfTask extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<>();
		//Display error if tasks are not in same project
		if (!getFieldValue("pmAotNextTskId.pmTskVrsId.pmVrsPrjId").equals(getFieldValue("pmAotPrvTskId.pmTskVrsId.pmVrsPrjId")))
			msgs.add(Message.formatInfo("PM_ERR_AOT_DIFF_PRJ", null, "pmAotNextTskId.pmTskVrsId.pmVrsPrjId"));
		return msgs;
	}
	@Override
	public String getUserKeyLabel(String[] row) {
		if(isTreeviewInstance()){
			ObjectDB o = getGrant().getTmpObject("PmTask");
			synchronized(o){
				o.getLock();
				o.select(getFieldValue("pmAotNextTskId"));
			return o.getFieldValue("pmTskTitle");
			}
			
		}
		return super.getUserKeyLabel(row);
	}
	@Override
	public String[] getTargetObject(String rowId, String[] row) {
		AppLog.info("DEBUG| "+getClassName()+": "+getInstanceName()+" Check: "+isPanelInstance(), getGrant());
		if(!isPanelInstance()){
			String[] Trigrame = new String[3];
			Trigrame[0]="PmTask";
			Trigrame[1]="the_ajax_PmTask";
			Trigrame[2]=row[getFieldIndex("pmAotNextTskId")];
			return Trigrame;
		}
		return super.getTargetObject(rowId, row);
	}
}
