var PmTimesheetExt = (function($) {
	//CRA
    function render(params) {
        try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			var userid =$ui.grant.getUserID();
            $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
            var div=$('#recap')
            var template =div.html();
            if (userid) {
                // Search the client
                app = $ui.getAjax();
                var affect = app.getBusinessObject('PmAssignment');
                affect.search(function(list) {
                    div.html(Mustache.render(template,list));
                }, {
                    pmAssPmUserid: userid
                });
            }
            else $ui.alert("No user id");
		} catch(e) {
			console.error('Render error: ' + e.message);
		}


    }
    return { render: render };
})();


