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
		if(getStatus().equals("TODO") && !getOldStatus().equals("DOING")){// If task is toDo status from other status expte Doing we have to inrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				tmpAssignment.resetFilters();
				tmpAssignment.setFieldFilter("pmAssPmTaskid", getRowId());
				AppLog.info("DEBUG 2 postUpdate "+tmpAssignment.search().get(0).toString(), getGrant());
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke increaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("increaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
				
			}
		
		}else if((getStatus().equals("DONE") || getStatus().equals("CANCEL") || getStatus().equals("REJECTED")) && (getOldStatus().equals("TODO") || getOldStatus().equals("DOING") )){// if task is done,cancel or rejected from to do or doing status we have to decrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
				
			}
		
		}
		
		return super.postUpdate();
	}
	@Override
	public String preDelete() {
		if(getStatus().equals("TODO") || getStatus().equals("DOING")){// if task is to do or doing status at delete  we have to decrease the number of task of user
			ObjectDB tmpAssignment = this.getGrant().getTmpObject("PmAssignment");
			synchronized(tmpAssignment){
				String sqlQuery = "select pm_ass_id from pm_assignment where pm_ass_pm_taskid="+getRowId(); //select all assignement of curent task
				for(String[] row : getGrant().query(sqlQuery)){// for all assignment invoke decreaseUserNbtask methode to update the nbTask of user assigned on task
					tmpAssignment.select(row[0]);
					try {
						tmpAssignment.invokeMethod("decreaseUserNbTask",null, null);
					} catch (Exception e) {
						AppLog.error(getClass(),"postUpdate","invokeMethod exception",e, getGrant());
					}
					tmpAssignment.save();
				}
				
			}
		
		}
		return super.preDelete();
	}
	public int ActualDuration(){//used by the calculated field pmTskActualDuraition
		String begin = getFieldValue("pmTskCreation");
		String  end = getFieldValue("pmTskEffectiveClosingDate");
		if(end.length() == 0 ){
			LocalDate dateObj = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			end = dateObj.format(formatter);
		}
		return Tool.diffDate(begin, end);
	}

}
