var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		var div=$('#recap')
		
		
		if (userid) {
            // Search the client
			app = $ui.getAjax();
			var affect = app.getBusinessObject('PmAssignment');
			var tmp=affect.getDisplay();
			
            affect.search(function(list) {
                if (list && list.length) {
                    ass =  list[0];
                    tmp+=" "+ass.pmAssRole;
                    
                }
            }, {
                pmAssPmUserid: userid
            });
			div.html(tmp);
        }
        else $ui.alert("No client id");
    }
    return { render: render };
})();

