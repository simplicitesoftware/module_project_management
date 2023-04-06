package com.simplicite.objects.ProjectManagement;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.simplicite.util.*;
import com.simplicite.util.Timesheet.*;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.tools.*;

/**
 * Business object PmImputation
 */
public class PmImputation extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public List<String> postSaveTimesheet(Timesheet ts) {
		Grant g = getGrant();
        List<TimesheetLine> lines = ts.getCurrentLines();
		try {
			if (ts.getObjectResource1().getName().equals("PmUser")){
				for(TimesheetLine tsLine: lines){
					ObjectDB tmpAss = g.getTmpObject("PmAssignment");
					BusinessObjectTool ot = tmpAss.getTool();
					AppLog.info("Debug: "+tsLine.getAssignRowId(), g);
				}
			}
		} catch (GetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.postSaveTimesheet(ts);
	}

}
/* List<TimesheetLine> h = ts.getCurrentLines();
			if (ts.getObjectResource1().getName().equals("CraEmploye")){
				for (TimesheetLine line : h)
				{
					String objname = "CraCompteCraEmploye";
	        		boolean[] oldcrud = g.changeAccess(objname,true,true,true,false);
	            	ObjectDB o = g.getTmpObject(objname);
	            	o.resetFilters();
		            o.resetValues();
		            BusinessObjectTool ot = o.getTool();
		            if (ot.getForCreateOrUpdate(
		                    new JSONObject()
		                            .put("craAccountempcraCptAId", line.getResource2RowId())
		                            .put("craEmployeId", line.getResource1RowId())
		                            .put("craAppcracomptecraemployeDebut", "='"+line.getBeginKey()+"'")
		                            .put("craAppcracomptecraemployeFin", line.getEndKey()!=null?"='"+line.getEndKey()+"'":"is null")
		                            )) {
			            //double c = Tool.parseDouble(line.getTotals()[0]);
			            String conso = g.simpleQuery("select sum(tsh_total1) from cra_compte_cra_employe_imputation where tsh_parent_id="+line.getAssignRowId());
			            AppLog.debug("postSaveTimesheet**" + conso, g);
			            o.setFieldValue("craAppcracomptecraemployeConso",conso);
			            ot.validateAndSave();
		             }
			         g.changeAccess(objname,oldcrud);
	            }
			} */