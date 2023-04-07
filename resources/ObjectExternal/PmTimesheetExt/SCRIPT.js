var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		var div=$('#recap')	
		var mustacheTemplate=div.text();
		var Mustache=$ui.loadMustache();
		div.text("");
		if (userid) {
            // Search the client
			app = $ui.getAjax();
			var affect = app.getBusinessObject('PmAssignment');
            affect.search(function(list) {
                if (list && list.length) {
                    ass =  list[0];
                    div.text(Mustache.render('mustacheTemplate',{Role:ass.pmAssRole}));
                }
            }, {
                pmAssPmUserid: userid
            });
        }
        else $ui.alert("No client id");
    }
    return { render: render };
})();

