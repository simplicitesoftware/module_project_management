package com.simplicite.extobjects.ProjectManagement;

import java.util.*;

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
	@Override
	public Object display(Parameters params) {
		try {
			// ctn is the "div.extern-content" to fill on UI
			AppLog.info("DEBUG: js "+String.join(", ",HTMLTool.fullcalendarJS​(getGrant().getLang())), getGrant());
			HTMLTool.jsIncludes(HTMLTool.fullcalendarJS​(getGrant().getLang()));
			return javascript(getName() + ".render(ctn);");
		} catch (Exception e) {
			AppLog.error(null, e, getGrant());
			return e.getMessage();
		}
	}
}
