package com.simplicite.workflows.ProjectManagement;

import java.util.*;

import com.simplicite.bpm.*;
import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Process PmTaskCreate
 */
public class PmTaskCreate extends Processus {
	private static final long serialVersionUID = 1L;
	@Override
	public void postCancel(ActivityFile context) {
		String step = context.getActivity().getStep();
		if(step.equals("PMT-400")){
			getGrant().removeParameter("NEW_TASK_FILTERS");
		}
		super.postCancel(context);
	}
	
	@Override
	public void postAbandon() {
		getGrant().removeParameter("NEW_TASK_FILTERS");
		super.postAbandon();
	}
	@Override
	public void postValidate(ActivityFile context) {
		
		String step = context.getActivity().getStep();
		String tskId="";
		String[] values;
		switch (step) {
			case "PMTC-200":
				String prjName = context.getDataValue("Field", "pmTskVrsId.pmVrsPrjId.pmPrjName");
				String tskID = context.getDataValue("Field", "row_id");
				HashMap<String, String> filters = new HashMap<>();
				filters.put("pmPrjName",prjName);
				filters.put("tskID",tskID);
				getGrant().setParameter("NEW_TASK_FILTERS", filters);
				AppLog.info("DEBUG getGrant().setParameter(\"NEW_TASK_FILTERS\", "+prjName+");", getGrant());
				break;
			case "PMTC-300":
				
				tskId = getContext(getActivity("PMTC-200")).getDataValue("Field", "row_id");
				values = context.getDataFile("Field", "row_id" , true).getValues();
				for(String prvTskId: values){
					ObjectDB tmpArrTask = getGrant().getTmpObject("PmArrayOfTask");
					BusinessObjectTool toolArrTsk = tmpArrTask.getTool();
					try {
						toolArrTsk.getForCreate();
						tmpArrTask.setFieldValue("pmAotPrvTskId", prvTskId);
						tmpArrTask.setFieldValue("pmAotNextTskId", tskId);
						toolArrTsk.validateAndSave();
						
					} catch (GetException | ValidateException | SaveException e) {
						AppLog.error(e, getGrant());
						
					}
				}
				break;
			case "PMTC-400":
				getGrant().removeParameter("NEW_TASK_FILTERS");
				values = context.getDataFile("Field", "row_id" , true).getValues();
				tskId = getContext(getActivity("PMTC-200")).getDataValue("Field", "row_id");
				for(String nxtTskId: values){
					ObjectDB tmpArrTask = getGrant().getTmpObject("PmArrayOfTask");
					BusinessObjectTool toolArrTsk = tmpArrTask.getTool();
					try {
						toolArrTsk.getForCreate();
						tmpArrTask.setFieldValue("pmAotPrvTskId", tskId);
						tmpArrTask.setFieldValue("pmAotNextTskId", nxtTskId);
						toolArrTsk.validateAndSave();
					} catch (GetException | ValidateException | SaveException e) {
						AppLog.error(e, getGrant());
						
					}
				}
				break;
			case "PMTC-500":
				
				tskId = getContext(getActivity("PMTC-200")).getDataValue("Field", "row_id");
				values = context.getDataFile("Field", "row_id" , true).getValues();
				for(String lblId : values){
					ObjectDB tmpTskLbl = getGrant().getTmpObject("PmTskLbl");
					BusinessObjectTool toolTskLbl = tmpTskLbl.getTool();
					try {
						toolTskLbl.getForCreate();
						tmpTskLbl.setFieldValue("pmTsklblTskId", tskId);
						tmpTskLbl.setFieldValue("pmTsklblLblId", lblId);
						toolTskLbl.validateAndSave();
					} catch (GetException | ValidateException | SaveException e) {
						AppLog.error(e, getGrant());
						
					} 
				}
				break;
			default:
				AppLog.info(Message.formatInfo("STEP_WITHOUT_TREATMENT", " "+context.getActivity().getName()+" ", null), getGrant());
				break;
		}
		super.postValidate(context);
	}
}
