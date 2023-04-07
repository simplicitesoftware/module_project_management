var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		$('#recap').html('testb');
		app = $ui.getAjax();
		var affect = app.getBusinessObject('PmAssignment');
		var tmp ="test: ";
		affect.search(function(rows) {
			tmp+="1 ";
		},null,null);
		$('#recap').html(tmp);
    }
    return { render: render };
})();

