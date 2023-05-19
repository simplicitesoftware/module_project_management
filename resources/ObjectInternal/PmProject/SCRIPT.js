// PmProject front side hook
/*(function(ui) {
	if (!ui) return;
	const app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmProject = function(o, cbk) {
		try {
			console.log("PmProject hooks loading...");
			const p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					//...
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmProject: " + e.message);
		} finally {
			console.log("PmProject hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);*/
(function(ui) {
	var options = {
	    type: 'bar',
	    data: {
	        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
	        datasets: [{
	            label: '# of Votes',
	            data: [12, 19, 3, 5, 2, 3],
	            backgroundColor: [
	                'rgba(255, 99, 132, 0.2)',
	                'rgba(54, 162, 235, 0.2)',
	                'rgba(255, 206, 86, 0.2)',
	                'rgba(75, 192, 192, 0.2)',
	                'rgba(153, 102, 255, 0.2)',
	                'rgba(255, 159, 64, 0.2)'
	            ],
	            borderColor: [
	                'rgba(255,99,132,1)',
	                'rgba(54, 162, 235, 1)',
	                'rgba(255, 206, 86, 1)',
	                'rgba(75, 192, 192, 1)',
	                'rgba(153, 102, 255, 1)',
	                'rgba(255, 159, 64, 1)'
	            ],
	            borderWidth: 1
	        }]
	    },
	    options: {
	        scales: {
	            yAxes: [{
	                ticks: {
	                    beginAtZero:true
	                }
	            }]
	        }
	    }
	};
	
    if (!ui) return;
    var app = ui.getAjax();
    Simplicite.UI.hooks.PmProject = function(o, cbk) {
		try {
			console.log("PmProject hooks loading...");
			const p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					 $ui.loadCharts(function(){
						debugger;
						$('#PmProject-2').after('<div id="chart-container"></div>');
						//$ui.charts.chart($("#chart-container"), options);
						$ui.displayCrosstab($("#chart-container"), "PmTask", "PmUserTask");
					});
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmProject: " + e.message);
		} finally {
			console.log("PmProject hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
   
})(window.$ui);