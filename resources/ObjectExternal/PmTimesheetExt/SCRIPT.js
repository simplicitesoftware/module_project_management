var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		$('#recap').html('testb');
		app = $ui.getAjax();
		var affect = app.getBusinessObject('PmAssignment');
		affect.search(function(rows) {
			$('#recap').html(affect);
		}, {pmAssPmUserid: userid});
        /* try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			var div = $('#recap');
			
			
			var affect = app.getBusinessObject('PmAssignment');
			
			

	
			
		} catch(e) {
			console.error('Render error: ' + e.message);
		} */
    }
    return { render: render };
})();

