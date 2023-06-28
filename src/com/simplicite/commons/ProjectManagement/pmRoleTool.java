package com.simplicite.commons.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.bpm.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Shared code pmRoleTool
 */
public class pmRoleTool implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * check if user has a specific role on specific project
	 * @param role role to check
	 * @param id id of project
	 * @param g grant
	 * @return true if user has role
	 */
	public static boolean isRoleOnProject(String role,String id,Grant g) {
		ObjectDB o =g.getTmpObject("PmRole");
			synchronized(o){
				o.getLock();
				BusinessObjectTool ot =o.getTool();
				o.setFieldFilter("pmRolPrjId", id);
				o.setFieldFilter("pmRolUsrId", g.getUserId());
				o.setFieldFilter("pmRolRole", role);
				try {
					List<String[]> res = ot.search();
					if (!res.isEmpty()){
						return true;
					}
				} catch (SearchException e) {
					AppLog.error(e, g);
				}
			}
		return false;

	}
	
}
