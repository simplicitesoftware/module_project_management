var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			var div = $('#recap');
			app = $ui.getAjax();
	
			var affect = app.getBusinessObject('PmAssignment');

			div.html(affect.search(null, {'pmAssPmUserid':userid}, null));

	
			
		} catch(e) {
			console.error('Render error: ' + e.message);
		}
    }
    return { render: render };
})();

