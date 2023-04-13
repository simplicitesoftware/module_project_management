package com.simplicite.objects.ProjectManagement;

import java.util.*;

import org.json.*;

import com.simplicite.util.*;
import com.simplicite.util.Timesheet.*;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.exceptions.SaveException;
import com.simplicite.util.exceptions.ValidateException;
import com.simplicite.util.tools.*;

/**
 * Business object PmImputation
 */
public class PmAssignmentPmTimeSheetAssign extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public List<String> postSaveTimesheet(Timesheet ts) {
		Grant g = getGrant(); 
		
		List<String> msgs= new ArrayList<>();
        List<TimesheetLine> lines = ts.getCurrentLines();
		try {
			if (ts.getObjectResource1().getName().equals("PmUser")){
				for(TimesheetLine tsLine: lines){
					boolean[] oldcrud = g.changeAccess("PmAssignment",true,true,true,false);
					ObjectDB tmpAss = g.getTmpObject("PmAssignment");
					BusinessObjectTool ot = tmpAss.getTool();
					if (ot.getForCreateOrUpdate(new JSONObject()
		                .put("row_id", tsLine.getAssignRowId())
		                )) {
							String conso = g.simpleQuery("select sum(tsh_total1) from pm_assignment_pm_time_sheet_assign where tsh_parent_id="+tsLine.getAssignRowId());
							tmpAss.setFieldValue("pmAssConsumed",conso);
	        				ot.validateAndSave();
						}
					g.changeAccess("PmAssignment",oldcrud);
				}
			}
		} catch (JSONException|GetException|ValidateException|SaveException e) {
			msgs.add(Message.formatError("PM_ERR_EXPTION_TS", null, null));
			AppLog.error("postSaveTimesheet", e, g);
		}
		return super.postSaveTimesheet(ts);
	}

}