var PmTimesheetRecapExt = PmTimesheetRecapExt || (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        try {
            
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
            var div=$('#recap')
            var template =$('#pm-ass-template').html();
            
            if (userid) {
                // Search the client
                app = $ui.getAjax();
                var affect = app.getBusinessObject('PmAssignment');
                affect.search(function(list) {
                    div.html(Mustache.render(template,toDict(list)));
                    affect.resetFilters();
                }, {
                    pmAssPmUserid: userid
                });
            }
            else throw 'No id';
        } catch(e) {
			console.error('Render error: ' + e.message);
		}
    }        
    
    function toDict(list) {
        var lang = app.getGrant().getLang();
        var data ={
        	labeltache: 'task',
            labelRole:'Role',
            labelQuantity:'Quantity',
            labelConsumed:'Consumed',
            listNotEmpty: false,
            ass: []
        }
        if (lang == "FRA"){
            data.labelRole = "Rôle";
            data.labelQuantity = "Droit";
            data.labelConsumed = "Consommée";
        }
        if (list && list.length){
            data.listNotEmpty = true;
            list.forEach(ass => (function(assign){
                var now = new Date(); 
                now.setHours(0,0,0,0);
                var statusList=['DRAFT','TODO', 'DOING', 'DONE']
                if(statusList.includes(assign.pmAssPmTaskid__pmTskStatus)){
                    var objAss = {
                        pmAssTskName: assign.pmAssPmTaskid__pmTskTitle,
                        pmAssRole: assign.pmAssRole,
                        pmAssConsumed: assign.pmAssConsumed,
                        pmAssQuantity:' ',
                        setProgress:false,
                        isOutTime: app.parseDateValue(assign.pmAssPmTaskid__pmTskClose) < now
                    };
                    if(assign.pmAssQuantity){
                        objAss.setProgress=true;
                        objAss.pmAssQuantity =assign.pmAssQuantity;
                    }
                    data.ass.push(objAss);
                }
                return;

            }(ass)));
            
        }
        return data;
    }
    return { render: render };
})(jQuery);