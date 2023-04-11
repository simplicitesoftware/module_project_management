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
            affect.search(function(list) {
                if (list && list.length) {
                    list.forEach(ass => div.html(div.html()+'<br>'+formatLineAss(ass)));
                }
            }, {
                pmAssPmUserid: userid
            });
        }
        else $ui.alert("No client id");
    }
    return { render: render };
})();
function formatLineAss(ass) {
    return ass.pmAssRole;
  }

