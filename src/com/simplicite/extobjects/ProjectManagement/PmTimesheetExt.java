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
			boolean pub = isPublic();
			setDecoration(!pub);
			String render = getName() + ".render();";
			if (pub) { // Public page version (standalone Bootstrap page)
				AppLog.info("in If", getGrant());
				BootstrapWebPage wp = new BootstrapWebPage(params.getRoot(), getDisplay());
				wp.appendAjax(true);
				wp.appendMustache();
				wp.appendJSInclude(HTMLTool.getResourceJSURL(this, "SCRIPT"));
				wp.appendCSSInclude(HTMLTool.getResourceCSSURL(this, "STYLES"));
				wp.append(HTMLTool.getResourceHTMLContent(this, "HTML"));
				wp.setReady(render);
				return wp.toString();
			} else { // Private page version
				AppLog.info("in Else", getGrant());
				addMustache();
				return javascript(render);
			}
		} catch (Exception e) {
			AppLog.error(getClass(), "display", null, e, getGrant());
			return e.getMessage();
		}
	}
}