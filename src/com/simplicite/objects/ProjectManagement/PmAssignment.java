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
		ObjectDB prdTask = this.getGrant().getTmpObject("PmTask");
		synchronized(prdTask){
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            prdTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(prdTask.getStatus().equals("TODO") ||prdTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we decrease the number of task
				decreaseUserNbTask();
			}
		}
		return super.preDelete();
	}
	@Override
	public String postCreate() {// if we created assignment we have to update the number of task of user
		ObjectDB prdTask = this.getGrant().getTmpObject("PmTask");
		synchronized(prdTask){
			// select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            prdTask.select(this.getFieldValue("pmAssPmTaskid"));
			if(prdTask.getStatus().equals("TODO") ||prdTask.getStatus().equals("DOING")){// if the task assigned is in to do or doing status we increase the number of task
				increaseUserNbTask();
			}
		}
		return super.postCreate();
	}
	public void increaseUserNbTask(){// invoke the increase methode of object user
		ObjectDB prd = this.getGrant().getTmpObject("PmUser");
        synchronized(prd){
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            prd.select(this.getFieldValue("pmAssPmUserid"));
            // lecture de la quantité commandée sur l'instance courante et du stock du produit sur l'instance chargée
			try {
				prd.invokeMethod("increaseNbTask",null, null);
			} catch (Exception e) {
				AppLog.error(getClass(),"increaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            prd.save();
        }
	}
	public void decreaseUserNbTask(){// invoke the decrease methode of object user
		ObjectDB prd = this.getGrant().getTmpObject("PmUser");
        synchronized(prd){
            // select = chargement dans l'instance des valeurs en base à partir d'une clef technique (id)
            prd.select(this.getFieldValue("pmAssPmUserid"));
            // lecture de la quantité commandée sur l'instance courante et du stock du produit sur l'instance chargée
			try {
				prd.invokeMethod("decreaseNbTask",null, null);
			} catch (Exception e) {
				AppLog.error(getClass(),"decreaseUserNbTask","invokeMethod exception",e, getGrant());
			}
			
            prd.save();
        }
	}
}
