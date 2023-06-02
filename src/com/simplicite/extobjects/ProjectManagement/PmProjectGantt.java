package com.simplicite.extobjects.ProjectManagement;

import static org.mockito.ArgumentMatchers.contains;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * External object PmProjectGantt
 */
public class PmProjectGantt extends ExternalObject { // or com.simplicite.webapp.web.ResponsiveExternalObject for a custom UI component
	                                                 // or com.simplicite.webapp.services.RESTServiceExternalObject for a custom API
	                                                 // etc.
	private static final long serialVersionUID = 1L;

	/**
	 * Display method (only relevant if extending the base ExternalObject)
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		//gen the json of element in gantt (only unclosed task and unpublished vertion)
		try {
			JSONArray tasks= new JSONArray();
			ObjectDB oV = getGrant().getTmpObject("PmVersion");
			synchronized(oV){	
				oV.getLock();
				BusinessObjectTool oVT = oV.getTool();
				oV.resetFilters();
				oV.setFieldFilter("pmVrsPrjId", params.getRowId(null));
				oV.setFieldFilter("pmVrsStatus","in (\'ALPHA\',\'BETA\')");
				//version
				for(String[] rowV:oV.search()){	
					oVT.get(rowV[0]);
					
					tasks.put(new JSONObject()
						.put("id", "V#"+oV.getRowId())
						.put("name", oV.getFieldValue("pmVrsName"))
						.put("start", oV.getCreatedDate() )
						.put("end", oV.getFieldValue("pmVrsPublicationDate"))
						.put("progress", oV.getFieldValue("pmVrsCompletion"))
						.put("dependencies", "")
						.put("url",oV.getDirectURL(true))
						.put("allow_progress_update",false)
						.put("allow_dragging",Arrays.asList("ALPHA","BETA").contains(oV.getStatus()))
						.put("custom_class","bar_"+oV.getStatusField().getStyle(oV, rowV[oV.getStatusIndex()])+" bar_vrs")
						
					); 
					ObjectDB o = getGrant().getTmpObject("PmTask");
					synchronized(o){
						o.getLock();
						int idIndex = o.getRowIdFieldIndex();
						int titleIndex = o.getFieldIndex("pmTskTitle");
						int startIndex = o.getFieldIndex("pmTskCreation");
						int endIndex = o.getFieldIndex("pmTskClose");
						int progressIndex= o.getFieldIndex("pmTskCompletion");
						o.resetFilters();
						BusinessObjectTool ot = o.getTool();
						String filter = "in (\'DRAFT\',\'TODO\',\'DOING\',\'DONE\')";
						o.setFieldFilter("pmTskVrsId", rowV[0]);
						o.setFieldFilter("pmTskStatus",filter);
						//task
						for(String[] row : o.search()){
							ObjectDB oDep = getGrant().getTmpObject("PmArrayOfTask");
							synchronized(oDep){
								oDep.getLock();
								List<String> dep = new ArrayList<>();
								// add dependecies with version
								dep.add("V#"+rowV[0]);
								oDep.resetFilters();
								oDep.setFieldFilter("pmAotNextTskId",row[0]);
								oDep.setFieldFilter("pmAotPrvTskId.pmTskStatus",filter);
								// add all previous task in dependencie
								for(String[] rowDep : oDep.search()){
									dep.add(rowDep[oDep.getFieldIndex("pmAotPrvTskId")]);
								}
								ot.get(row[idIndex]);
								
								tasks.put(new JSONObject()
									.put("id", row[idIndex])
									.put("name", row[titleIndex])
									.put("start", row[startIndex])
									.put("end", row[endIndex])
									.put("progress", row[progressIndex])
									.put("dependencies", String.join(", ", dep))
									.put("url",o.getDirectURL(true))
									.put("allow_progress_update",Arrays.asList("TODO","DOING").contains(o.getStatus()))
									.put("allow_dragging",Arrays.asList("TODO","DOING","DRAFT").contains(o.getStatus()))
									.put("custom_class","bar_"+o.getStatusField().getStyle(o, row[o.getStatusIndex()]))
									);
							}
							
						}

					}
				}
			}
			return javascript(getName() + ".render(ctn,"+tasks.toString()+");");
		} catch (Exception e) {
			AppLog.error(null, e, getGrant());
			return e.getMessage();
		}
	}
	
	
}
