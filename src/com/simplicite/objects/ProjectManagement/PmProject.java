package com.simplicite.objects.ProjectManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.json.*;

import com.google.api.services.sheets.v4.model.BubbleChartSpec;
import com.simplicite.util.*;
import com.simplicite.util.annotations.BusinessObject;
import com.simplicite.util.exceptions.CreateException;
import com.simplicite.util.exceptions.GetException;
import com.simplicite.util.exceptions.SearchException;
import com.simplicite.util.exceptions.ValidateException;
import com.simplicite.util.tools.*;

/**
 * Business object PmProject
 */
public class PmProject extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public String postCreate() {
		ObjectDB o = getGrant().getTmpObject("PmDocument");
		String url ="";
		synchronized(o){
			o.getLock();
			BusinessObjectTool ot = o.getTool();
			try {
				ot.getForCreate();
				o.setFieldValue("pmDocTitle", "Fiche d’expression des besoins");
				o.setFieldValue("pmDocType", "REQ");
				o.setFieldValue("pmDocPrjId", getRowId());
				ot.validateAndCreate();
				url=o.getDirectURL(true);
			} catch (GetException|ValidateException|CreateException e) {
				AppLog.error(e, getGrant());
			}
		}
		return super.postCreate();
	}
	@Override
	public boolean isReadOnly() {
		
		if(getGrant().hasResponsibility("PM_MANAGER") && !getGrant().hasResponsibility("PM_SUPERADMIN")){
			ObjectDB o =getGrant().getTmpObject("PmRole");
			synchronized(o){
				o.getLock();
				BusinessObjectTool ot =o.getTool();
				o.setFieldFilter("pmRolPrjId", getRowId());
				o.setFieldFilter("pmRolUsrId", getGrant().getUserId());
				o.setFieldFilter("pmRolRole", "MANAGER");
				try {
					List<String[]> res = ot.search();
					if (res.isEmpty()){
						return true;
					}
					
				} catch (SearchException e) {
					AppLog.error(e, getGrant());
				}
			}
				
			
		}
		return super.isReadOnly();
	}
	@Override
	public void initUpdate() {
		getGrant().setParameter("PROJECT_ID", getRowId());
		super.initUpdate();
	}
	/*
		fonction for publication in PmProject
	 */
	public String pubHtml(){
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String now = dateObj.format(formatter);
		JSONObject projectJson= new JSONObject();
		String objTask ="PmTask";
		projectJson.put("pmPrjName",getFieldValue("pmPrjName"));
		projectJson.put("timeStamp",now);
		projectJson.put("labelPublicationDate",getGrant().getTmpObject("PmVersion").getField("pmVrsPublicationDate").getDisplay());
		projectJson.put("labelTitle",getGrant().getTmpObject(objTask).getField("pmTskTitle").getDisplay());
		projectJson.put("labelDescription",getGrant().getTmpObject(objTask).getField("pmTskDescription").getDisplay());
		projectJson.put("labelStatus",getGrant().getTmpObject(objTask).getField("pmTskStatus").getDisplay());
		projectJson.put("labelPriority",getGrant().getTmpObject(objTask).getField("pmTskPriority").getDisplay());
		projectJson.put("labelCreationDate",getGrant().getTmpObject(objTask).getField("pmTskCreation").getDisplay());
		projectJson.put("labelEffectiveClosingDate",getGrant().getTmpObject(objTask).getField("pmTskEffectiveClosingDate").getDisplay());
		projectJson.put("labelExpetedDuration",getGrant().getTmpObject(objTask).getField("pmTskExpectedDuration").getDisplay());
		projectJson.put("labelLabels",getGrant().getTmpObject("PmLabel").getPluralDisplay());

		ObjectDB tmpVrs = this.getGrant().getTmpObject("PmVersion");
		synchronized(tmpVrs){
			tmpVrs.getLock();
			tmpVrs.resetFilters();
			tmpVrs.setFieldFilter("pmVrsPrjId", getRowId());
			tmpVrs.setFieldFilter("pmVrsStatus", "PUBLISHED");
			JSONArray versionArray = new JSONArray();
			for(String[] rowVrs : tmpVrs.search()){// for all published verion
				tmpVrs.select(rowVrs[0]);
				JSONObject versionJson= new JSONObject();
				versionJson.put("pmVrsVersion",tmpVrs.getFieldValue("pmVrsVersion"));
				versionJson.put("pmVrsPublicationDate",tmpVrs.getFieldValue("pmVrsPublicationDate"));
				JSONArray taskArray = new JSONArray();
				ObjectDB tmpTsk = this.getGrant().getTmpObject(objTask);
				synchronized(tmpTsk){
					tmpTsk.getLock();
					tmpTsk.resetFilters();
					tmpTsk.setFieldFilter("pmTskVrsId", tmpVrs.getRowId());
					for(String[] rowTsk : tmpTsk.search()){//for all task of version
						tmpTsk.select(rowTsk[0]);
						JSONObject taskJson= new JSONObject();
						taskJson.put("pmTskTitle",tmpTsk.getFieldValue("pmTskTitle"));
						taskJson.put("pmTskDescription",tmpTsk.getFieldValue("pmTskDescription"));
						taskJson.put("pmTskStatus",tmpTsk.getField("pmTskStatus").getDisplayValue());
						taskJson.put("pmTskPriority",tmpTsk.getField("pmTskPriority").getDisplayValue());
						taskJson.put("pmTskEffectiveClosingDate",tmpTsk.getFieldValue("pmTskEffectiveClosingDate"));
						taskJson.put("pmTskExpectedDuration",tmpTsk.getFieldValue("pmTskExpectedDuration"));
						taskJson.put("pmTskCreation",tmpTsk.getFieldValue("pmTskCreation"));
						StringBuilder labelArray = new StringBuilder();
						ObjectDB tmpLbl = this.getGrant().getTmpObject("PmTskLbl");
						synchronized(tmpLbl){
							tmpLbl.getLock();
							tmpLbl.resetFilters();
							tmpLbl.setFieldFilter("pmTsklblTskId", tmpTsk.getRowId());
							for(String[] rowLbl : tmpLbl.search()){ //for all label of task
								tmpLbl.select(rowLbl[0]);
								labelArray.append(tmpLbl.getFieldValue("pmTsklblLblId.pmLblName")+", ");
							}
						}
						String labelString=labelArray.toString();
						if(!Tool.isEmpty(labelString)){
							taskJson.put("PmLabel",labelString.substring(0,labelString.length()-2));
						}else{
							taskJson.put("PmLabel","");
						}
						
						taskArray.put(taskJson);

					}
				}
				
				versionJson.put("NotEmpty",!Tool.isEmpty(taskArray));
				versionJson.put(objTask,taskArray);
				versionArray.put(versionJson);
			}
			projectJson.put("PmVersion", versionArray );
		}
		projectJson.put("css", HTMLTool.getResourceCSSContent(this, "pmPrjPubStyle") );
		return MustacheTool.apply(HTMLTool.getResourceHTMLContent(this, "pmPrjPubHtml"),projectJson.toString());
	}
	
}