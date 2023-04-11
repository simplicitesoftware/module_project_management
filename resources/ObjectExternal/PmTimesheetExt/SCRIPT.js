var PmTimesheetExt = (function($) {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		var div=$('#recap')
        var template =div.html();
		if (userid) {
            // Search the client
			app = $ui.getAjax();
			var affect = app.getBusinessObject('PmAssignment');
            affect.search(function(list) {
                div.html(Mustache.render(template,list))
            }, {
                pmAssPmUserid: userid
            });
        }
        else $ui.alert("No user id");
    }
    return { render: render };
})();


