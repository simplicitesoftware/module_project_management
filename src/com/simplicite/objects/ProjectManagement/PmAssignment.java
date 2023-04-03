package com.simplicite.objects.ProjectManagement;

import java.util.*;

import com.simplicite.util.*;
import com.simplicite.util.exceptions.*;
import com.simplicite.util.tools.*;

/**
 * Business object PmAssignment
 */
public class PmAssignment extends ObjectDB {
	private static final long serialVersionUID = 1L;
	@Override
	public String preDelete() {// if we deleted assignment we have to update the number of task of user
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(tmpTask.getStatus().equals("TODO") ||tmpTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we decrease the number of task
				decreaseUserNbTask();
			}
		}
		return super.preDelete();
	}
	@Override
	public String postCreate() {// if we created assignment we have to update the number of task of user
		ObjectDB tmpTask = this.getGrant().getTmpObject("PmTask");
		synchronized(tmpTask){
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(tmpTask.getStatus().equals("TODO") ||tmpTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we increase the number of task
				increaseUserNbTask();
			}
		}
		return super.postCreate();
	}
	public void increaseUserNbTask(){// invoke the increase methode of object user
		ObjectDB tmpUser = this.getGrant().getTmpObject("PmUser");
        synchronized(tmpUser){
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpUser.select(this.getFieldValue("pmAssPmUserid"));
			try {
				tmpUser.invokeMethod("increaseNbTask",null, null);
			} catch (Exception e) {
				AppLog.error(getClass(),"increaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            tmpUser.save();
        }
	}
	public void decreaseUserNbTask(){// invoke the decrease methode of object user
		ObjectDB tmpUser = this.getGrant().getTmpObject("PmUser");
        synchronized(tmpUser){
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            tmpUser.select(this.getFieldValue("pmAssPmUserid"));
			try {
				tmpUser.invokeMethod("decreaseNbTask",null, null);
			} catch (Exception e) {
				AppLog.error(getClass(),"decreaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            tmpUser.save();
        }
	}
	
	
}
