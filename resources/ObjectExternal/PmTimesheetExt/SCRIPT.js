var PmTimesheetExt = (function() {
	//CRA
	app.getBusinessObject('PmAssignment').search(function() {
		$('#recap').html(rows[0]);
	}, {pmAssPmUserid: userid});
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        /* try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			var div = $('#recap');
			app = $ui.getAjax();
			
			var affect = app.getBusinessObject('PmAssignment');
			
			

	
			
		} catch(e) {
			console.error('Render error: ' + e.message);
		} */
    }
    return { render: render };
})();

