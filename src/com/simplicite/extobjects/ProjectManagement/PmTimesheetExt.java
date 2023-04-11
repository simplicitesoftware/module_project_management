package com.simplicite.extobjects.ProjectManagement;

import java.util.*;

import com.simplicite.util.AppLog;
import com.simplicite.util.ExternalObject;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.Parameters;
import com.simplicite.webapp.web.BootstrapWebPage;

/**
 * External object PmTimesheetExt
 */
public class PmTimesheetExt extends ExternalObject {
	private static final long serialVersionUID = 1L;
	@Override
	public Object display(Parameters params) {
		try {
			BootstrapWebPage wp = new BootstrapWebPage(params.getRoot(), getDisplay());
			wp.appendAjax(true);
			wp.appendMustache();
			wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
			wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
			wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
			return wp.toString();
			
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}