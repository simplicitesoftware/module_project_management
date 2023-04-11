var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        try {
            if (typeof Mustache === 'undefined') throw 'Mustache not available';
            var div=$('#recap')
            var template =$('#pm-ass-template').html();
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
            else throw 'No user id';
		} catch(e) {
			console.error('Render error: ' + e.message);
		}
		
    }
    return { render: render };
})();


