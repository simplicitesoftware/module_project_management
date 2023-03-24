package com.simplicite.objects.ProjectManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmTask
 */
public class PmTask extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public String postUpdate() {
		// TODO Auto-generated method stub
		if(getStatus().equals("CLOSED") && getFieldValue("pmTskClose").equals("") ){
			AppLog.info(" DEBUG "+"Update close", getGrant());
		}
		if(getStatus().equals("TODO") && !getOldStatus().equals("DOING")){// If task is toDo status from other status expte Doing we have to inrease the number of task of user
			ObjectDB prd = this.getGrant().getTmpObject("PmAssignment");
			synchronized(prd){
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
					AppLog.info("DEBUG "+"sqlQuery: " +sqlQuery, getGrant());
					AppLog.info("DEBUG "+"id ass: " +row[0], getGrant());
					prd.select(this.getFieldValue(row[0]));
					AppLog.info("DEBUG "+"post select: " +prd.getFieldValue("pmAssRole"), getGrant());
					try {
						prd.invokeMethod("increaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					prd.save();
				}
				
			}
		
		}else if((getStatus().equals("DONE") || getStatus().equals("CANCEL") || getStatus().equals("REJECTED")) && (getOldStatus().equals("TODO") || getOldStatus().equals("DOING") )){// if task is done,cancel or rejected from to do or doing status we have to decrease the number of task of user
			ObjectDB prd = this.getGrant().getTmpObject("PmAssignment");
			synchronized(prd){
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					prd.select(this.getFieldValue(row[0]));
					try {
						prd.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					prd.save();
				}
				
			}
		
		}
		
		return super.postUpdate();
	}
	@Override
	public String preDelete() {
		if(getStatus().equals("TODO") || getStatus().equals("DOING")){// if task is to do or doing status at delete  we have to decrease the number of task of user
			ObjectDB prd = this.getGrant().getTmpObject("PmAssignment");
			synchronized(prd){
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					prd.select(this.getFieldValue(row[0]));
					try {
						prd.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					prd.save();
				}
				
			}
		
		}
		return super.preDelete();
	}
	public int ActualDuration(){//used by the calculated field pmTskActualDuraition
		var begin = this.getFieldValue("pmTskCreation");
		var  end = this.getFieldValue("pmTskEffectiveClosingDate");
		if(end.equals("") ){
			LocalDate dateObj = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			end = dateObj.format(formatter);
		}
		return Tool.diffDate(begin, end);
	}

}
