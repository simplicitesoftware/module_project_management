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
		if(getFieldValue("pmTskStatus").equalsIgnoreCase("CLOSED") && getFieldValue("pmTskClose").equalsIgnoreCase("") ){
			AppLog.info(" DEBUG "+"Update close", getGrant());
		}
		return super.postUpdate();
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
