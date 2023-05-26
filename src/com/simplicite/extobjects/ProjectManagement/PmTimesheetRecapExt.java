package com.simplicite.extobjects.ProjectManagement;


import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * External object PmTimesheetRecapExt
 */
public class PmTimesheetRecapExt extends ExternalObject { // or com.simplicite.webapp.web.ResponsiveExternalObject for a custom UI component
	                                                 // or com.simplicite.webapp.services.RESTServiceExternalObject for a custom API
	                                                 // etc.
	private static final long serialVersionUID = 1L;

	/**
	 * Display method (only relevant if extending the base ExternalObject)
	 * display resume in time sheet
	 * @param params Request parameters
	 */
	@Override
	public Object display(Parameters params) {
		// ctn is the "div.extern-content" to fill on UI
		addMustache();
		return javascript(getName() + ".render(ctn);");
		
	}
}