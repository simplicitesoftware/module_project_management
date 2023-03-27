package com.simplicite.objects.ProjectManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String now = dateObj.format(formatter);
		String html="<head><title>"+getFieldValue("pmPrjName")+": "+now+"</title><style type='text/css'>table {\nborder: 1px solid #333;\nborder-collapse: collapse;\n}</style></head>"+"\n";
		html+="<h1>"+getFieldValue("pmPrjName")+" "+now+"</h1>"+"\n";
		String sqlQuery = "select row_id, pm_vrs_version, pm_vrs_date_publication from pm_version where pm_vrs_status='PUBLISHED' AND pm_vrs_prj_id="+getRowId()+"order by pm_vrs_date_publication DESC";
		
		for(String[] row : getGrant().query(sqlQuery)){
			html += "<h1>"+row[1]+"</h1>\n";
			html += "Published on "+row[2]+"\n";
			String sqlQueryTask = "select row_id, pm_tsk_number, pm_tsk_title,pm_tsk_description,pm_tsk_status,pm_tsk_priority,pm_tsk_effective_closing_date,pm_tsk_expected_duration,pm_tsk_creation  from pm_task where pm_tsk_vrs_id="+row[0]+"order by pm_tsk_creation DESC";
			int id = 0; int number = 1; int title= 2; int description= 3; int status= 4; int priority= 5; int effectiveClosingDate= 6; int expectedDuration=7;int creation=8;
			html += "<table>"+"\n";
			html += "<thead><tr><th>Number</th><th>Title</th><th>Description</th><th>Status</th><th>Priority</th><th>Creation date</th><th>Effective closing date</th><th>Expeted Duration</th></tr></thead>"+"\n";
			html += "<tbody>"+"\n";
			for(String[] rowTask : getGrant().query(sqlQueryTask)){
				html += "<tr>"+"\n";
				html += "<td>"+rowTask[number]+"</td>"+"\n";
				html += "<td>"+rowTask[title]+"</td>"+"\n";
				html += "<td>"+rowTask[description]+"</td>"+"\n";
				html += "<td>"+rowTask[status]+"</td>"+"\n";
				html += "<td>"+rowTask[priority]+"</td>"+"\n";
				html += "<td>"+rowTask[creation]+"</td>"+"\n";
				html += "<td>"+rowTask[effectiveClosingDate]+"</td>"+"\n";
				html += "<td>"+rowTask[expectedDuration]+"</td>"+"\n";
				html += "</tr>"+"\n";
			}
			html += "</tbody>"+"\n";
			html += "</table>"+"\n";


			
		}
		
		return html;
	}
	
}
