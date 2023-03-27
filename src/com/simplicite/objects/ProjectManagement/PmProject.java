package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmProject
 */
public class PmProject extends ObjectDB {
	private static final long serialVersionUID = 1L;
	public String pubHtml(){
		String html="<h1>"+getFieldValue("pmPrjName")+"</h1>"+"\n";

		String sqlQuery = "select row_id, pm_vrs_version, pm_vrs_date_publication from pm_version where pm_vrs_status='PUBLISHED' AND pm_vrs_prj_id="+getRowId()+"order by pm_vrs_date_publication DESC";
		html+= "<ul>"+"\n";
		for(String[] row : getGrant().query(sqlQuery)){
			html += "<li>"+row[1]+": "+row[2]+"</li>\n";
		}
		html+= "</ul>"+"\n";
		return html;
	}
	
}
