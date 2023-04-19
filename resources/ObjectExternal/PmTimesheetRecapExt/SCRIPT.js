var PmTimesheetRecapExt = PmTimesheetRecapExt || (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        try {
            console.log("DEBUG in PmTimesheetRecapExt")
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
            var div=$('#recap')
            var template =$('#pm-ass-template').html();
            if (userid) {
                // Search the client
                app = $ui.getAjax();
                var affect = app.getBusinessObject('PmAssignment');
                
                affect.search(function(list) {
                    div.html(Mustache.render(template,toDict(list)));
                }, {
                    pmAssPmUserid: userid
                });
            }
            else throw 'No client id';
        } catch(e) {
			console.error('Render error: ' + e.message);
		}
    }        
    function getTskName(id){
        var obj = app.getBusinessObject("PmTask");
        obj.search(function(l) {
            console.log(l[0].pmTskTitle);
            return l[0].pmTskTitle;
        }, {
            row_id: id
        });
        return "not title"
        
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
            list.forEach(ass => data.ass.push(function(assign){
                var objAss = {
                	pmAssTskName: getTskName(assign.pmAssPmTaskid),
                    pmAssRole: assign.pmAssRole,
                    pmAssConsumed: assign.pmAssConsumed,
                    pmAssQuantity:' ',
                    setProgress:false
                };
                var obj = app.getBusinessObject("PmTask");
                obj.search(function(l) {
                    objAss.pmAssTskName=l[0].pmTskTitle;
                    console.log(l[0].pmTskTitle)
                }, {
                    row_id: assign.pmAssPmTaskid
                });
                if(assign.pmAssQuantity){
                    objAss.setProgress=true;
                    objAss.pmAssQuantity =assign.pmAssQuantity;
                }
                return objAss;
            }(ass)));
            
        }
        return data;
    }
    return { render: render };
})(jQuery);