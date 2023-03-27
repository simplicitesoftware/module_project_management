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
		AppLog.info(getResource("pmPrjPubHtmlStyle").getCode(), getGrant())
		String html="<head><title>"+getFieldValue("pmPrjName")+": "+now+"</title><style type='text/css'></style></head>"+"\n";
		html+="<h1>"+getFieldValue("pmPrjName")+" "+now+"</h1>"+"\n";
		String sqlQuery = "select row_id, pm_vrs_version, pm_vrs_date_publication from pm_version where pm_vrs_status='PUBLISHED' AND pm_vrs_prj_id="+getRowId()+"order by pm_vrs_date_publication DESC";
		
		for(String[] row : getGrant().query(sqlQuery)){
			html += "<h1>"+row[1]+"</h1>\n";
			html += "Published on "+row[2]+"\n";
			String sqlQueryTask = "select row_id, pm_tsk_number, pm_tsk_title,pm_tsk_description,pm_tsk_status,pm_tsk_priority,pm_tsk_effective_closing_date,pm_tsk_expected_duration,pm_tsk_creation  from pm_task where pm_tsk_vrs_id="+row[0]+"order by pm_tsk_creation DESC";
			int id = 0; int number = 1; int title= 2; int description= 3; int status= 4; int priority= 5; int effectiveClosingDate= 6; int expectedDuration=7;int creation=8;
			String table="";
			for(String[] rowTask : getGrant().query(sqlQueryTask)){
				String sqlQueryLabel = "select l.pm_lbl_name from pm_label l  left join pm_tsk_lbl tl on tl.pm_tsklbl_lbl_id=l.row_id left join pm_task t on t.row_id=tl.pm_tsklbl_tsk_id where t.row_id = "+rowTask[id];
				String labels= "";
				for(String[] rowLabel : getGrant().query(sqlQueryLabel)){
					labels += rowLabel[0]+=", ";
				}
				if (labels != ""){
					labels = labels.substring(0, labels.length()-2);
				}
				table += "<tr>"+"\n";
				table += "<td>"+rowTask[number]+"</td>"+"\n";
				table += "<td>"+rowTask[title]+"</td>"+"\n";
				table += "<td>"+rowTask[description]+"</td>"+"\n";
				table += "<td>"+rowTask[status]+"</td>"+"\n";
				table += "<td>"+rowTask[priority]+"</td>"+"\n";
				table += "<td>"+rowTask[creation]+"</td>"+"\n";
				table += "<td>"+rowTask[effectiveClosingDate]+"</td>"+"\n";
				table += "<td>"+rowTask[expectedDuration]+"</td>"+"\n";
				table += "<td>"+labels+"</td>"+"\n";
				table += "</tr>"+"\n";
			}
			if(table != ""){
				html += "<table>"+"\n";
				html += "<thead><tr><th>Number</th><th>Title</th><th>Description</th><th>Status</th><th>Priority</th><th>Creation date</th><th>Effective closing date</th><th>Expeted Duration</th><th>labels</th></tr></thead>"+"\n";
				html += "<tbody>"+"\n";
				html += table;
				html += "</tbody>"+"\n";
				html += "</table>"+"\n";
			}
			


			
		}
		
		return html;
	}
	
}
