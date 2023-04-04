package com.simplicite.objects.ProjectManagement;


import com.simplicite.util.*;
import com.simplicite.objects.System.*;

/**
 * Business object PmUser
 */
public class PmUser extends SimpleUser {
	private static final long serialVersionUID = 1L;
	public void increaseNbTask(){
		ObjectField nbTask = this.getField("pmUsrNbTask");
    	nbTask.setValue(nbTask.getInt(0)+1);
    	save();
	}
	public void decreaseNbTask(){
		ObjectField nbTask = this.getField("pmUsrNbTask");
    	if (nbTask.getInt(0) > 0){
				nbTask.setValue(nbTask.getInt(0)-1);	
		}else{
			nbTask.setValue(0);
		}
    	save();
	}
}
