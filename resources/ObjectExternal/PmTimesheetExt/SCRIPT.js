var PmTimesheetExt = (function($) {
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
                    //div.html(Mustache.render(template,list))
                    if (list && list.length) {
                        list.forEach(ass => div.html(div.html()+formatLineAss(ass)));
                    }
                }, {
                    pmAssPmUserid: userid
                });
            }
            else $ui.alert("No client id");
        } catch(e) {
			console.error('Render error: ' + e.message);
		}
    }        
    return { render: render };
})(jQuery);
function formatLineAss(ass) {
    var quantity = ass.pmAssQuantity || ' ';
    var progress = ' ';
    if(ass.pmAssQuantity){
        progress = '<progress value='+ass.pmAssConsumed+' max='+ass.pmAssQuantity+'></progress>'
    }
    return '<div class="table-row"><div class="table-data small">'+ass.pmAssRole+'</div><div class="table-data">'+quantity+'</div><div class="table-data big">'+ass.pmAssConsumed+'</div><div class="table-data big">'+progress+'</div></div>';
  }
  

