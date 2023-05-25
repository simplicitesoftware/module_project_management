package com.simplicite.extobjects.ProjectManagement;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * External object PmProjectCalendar
 */
public class PmProjectCalendar extends ExternalObject { // or com.simplicite.webapp.web.ResponsiveExternalObject for a custom UI component
	                                                 // or com.simplicite.webapp.services.RESTServiceExternalObject for a custom API
	                                                 // etc.
	private static final long serialVersionUID = 1L;

	/**
	 * Display method (only relevant if extending the base ExternalObject)
	 * @param params Request parameters
	 */
	public String color(String color) {
		switch(color){
			case "orangebg":
				return "rgb(249, 203, 156)";
			case "yellowbg":
				return "rgb(255, 229, 153)";
			case "whitebg":
				return "rgb(255, 255, 255)";
			case "greenbg":
				return "rgb(182, 215, 168)";
			case "greybg":
				return "rgb(171, 171, 171)";
			case "brownbg":
				return "rgb(221, 126, 107)";
			case "redbg":
				return "rgb(255, 0, 0)";
			default:
				return color;

		}
		
	}
	@Override
	public Object display(Parameters params) {
		try {
			// ctn is the "div.extern-content" to fill on UI
			JSONArray events= new JSONArray();
			ObjectDB o = getGrant().getTmpObject("PmTask");
			synchronized(o){
				o.getLock();
				o.resetFilters();
				int titleIndex = o.getFieldIndex("pmTskTitle");
				int idIndex=0;
				int startIndex =o.getFieldIndex("pmTskClose");
				int statusIndex = o.getStatusIndex();
				BusinessObjectTool ot = o.getTool();
				o.setFieldFilter("pmVrsPrjId", params.getRowId(null));
				for(String[] row : o.search()){
					ot.get(row[0]);
					AppLog.info("DEBUG: "+row[titleIndex]+" url: "+o.getDirectURL(true) , getGrant());

					events.put(new JSONObject()
						.put("id", row[idIndex])
						.put("title", row[titleIndex])
						.put("start",row[startIndex])
						.put("extendedProps",new JSONObject()).put("object", "pmTask")
						.put("backgroundColor",color(o.getStatusField().getStyle(o, row[statusIndex])))
						.put("textColor","black")
						.put("url",o.getDirectURL(true))
						.put("allDay", false)
					);
				}
			}
			o = getGrant().getTmpObject("PmVersion");
			synchronized(o){
				o.getLock();
				o.setFieldFilter("pmVrsPrjId", params.getRowId(null));
				BusinessObjectTool ot = o.getTool();
				for(String[] row : o.search()){
					AppLog.info("Debug: "+row[o.getFieldIndex("pmVrsPublicationDate")], getGrant());
					ot.get(row[0]);
					String bgcolor = o.getStatus().equals("PUBLISHED")?"lightseagreen":"#b4a7d6";
					events.put(new JSONObject()
						.put("id", row[o.getRowIdFieldIndex()])
						.put("title", row[o.getFieldIndex("pmVrsName")])
						.put("start",row[o.getFieldIndex("pmVrsPublicationDate")])
						.put("extendedProps",new JSONObject()).put("object", "PmVersion")
						.put("backgroundColor",bgcolor)
						.put("textColor","white")
						.put("url",o.getDirectURL(true))
					);
				}
				
			}
			addFullCalendar(getGrant().getLang());
			return javascript(getName() + ".render(ctn,"+events.toString()+");");
		} catch (Exception e) {
			AppLog.error(null, e, getGrant());
			return e.getMessage();
		}
		
	}
}
