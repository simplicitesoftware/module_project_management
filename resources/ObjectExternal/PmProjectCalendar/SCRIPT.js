var PmProjectCalendar = (function() {
	function render(params,events=[]) {
		
		const style = getComputedStyle(document.body);
		var calendarEl = document.getElementById('calendar');
		var calendar = new FullCalendar.Calendar(calendarEl, {
			initialView: 'dayGridMonth',
			headerToolbar: { end: 'dayGridMonth,timeGridWeek' },
			events: events
		});
		calendar.render();
		var div=$("#pmprojectcalendar").parents(".tab-pane");
		var observer = new MutationObserver(function (event) {
			if(div.hasClass( "active" )) calendar.render();
		  })
		  
		  observer.observe(div.get(0), {
			attributes: true, 
			attributeFilter: ['class'],
			childList: false, 
			characterData: false
		  })
	}

	return { render: render };
})();
