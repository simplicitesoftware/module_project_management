package com.simplicite.objects.ProjectManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.github.openjson.JSONArray;
import com.github.openjson.JSONObject;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmProject
 */
public class PmProject extends ObjectDB {
	private static final long serialVersionUID = 1L;
	public String pubHtml(){
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String now = dateObj.format(formatter);
		JSONObject projectJson= new JSONObject();
		projectJson.put("pmPrjName",getFieldValue("pmPrjName"));
		projectJson.put("timeStamp",now);
		projectJson.put("labelPublicationDate",getGrant().getTmpObject("PmVersion").getField("pmVrsPublicationDate").getDisplay());
		projectJson.put("labelNumber",getGrant().getTmpObject("PmTask").getField("pmTskNumber").getDisplay());
		projectJson.put("labelTitle",getGrant().getTmpObject("PmTask").getField("pmTskTitle").getDisplay());
		projectJson.put("labelDescription",getGrant().getTmpObject("PmTask").getField("pmTskDescription").getDisplay());
		projectJson.put("labelStatus",getGrant().getTmpObject("PmTask").getField("pmTskStatus").getDisplay());
		projectJson.put("labelPriority",getGrant().getTmpObject("PmTask").getField("pmTskPriority").getDisplay());
		projectJson.put("labelCreationDate",getGrant().getTmpObject("PmTask").getField("pmTskCreation").getDisplay());
		projectJson.put("labelEffectiveClosingDate",getGrant().getTmpObject("PmTask").getField("pmTskEffectiveClosingDate").getDisplay());
		projectJson.put("labelExpetedDuration",getGrant().getTmpObject("PmTask").getField("pmTskExpectedDuration").getDisplay());
		projectJson.put("labelLabels",getGrant().getTmpObject("PmLabel").getPluralDisplay());

		ObjectDB tmpVrs = this.getGrant().getTmpObject("PmVersion");
		synchronized(tmpVrs){
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
				ObjectDB tmpTsk = this.getGrant().getTmpObject("PmTask");
				synchronized(tmpTsk){
					tmpTsk.resetFilters();
					tmpTsk.setFieldFilter("pmTskVrsId", tmpVrs.getRowId());
					for(String[] rowTsk : tmpTsk.search()){//for all task of version
						tmpTsk.select(rowTsk[0]);
						JSONObject taskJson= new JSONObject();
						taskJson.put("pmTskNumber",tmpTsk.getFieldValue("pmTskNumber"));
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
				versionJson.put("PmTask",taskArray);
				versionArray.put(versionJson);
			}
			projectJson.put("PmVersion", versionArray );
		}
		projectJson.put("css", HTMLTool.getResourceCSSContent(this, "pmPrjPubStyle") );
		return MustacheTool.apply(HTMLTool.getResourceHTMLContent(this, "pmPrjPubHtml"),projectJson.toString());
	}
	
}
