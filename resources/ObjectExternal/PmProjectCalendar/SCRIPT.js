var PmProjectCalendar = (function() {
	function render(params,events=[]) {
		$('#pmprojectcalendar').append('Hello world!');
		const style = getComputedStyle(document.body);
		const orangebg = style.getPropertyValue('--orangebg');
		console.log("debug: "+orangebg);
		var calendarEl = document.getElementById('calendar');
		var calendar = new FullCalendar.Calendar(calendarEl, {
			initialView: 'dayGridMonth',
			headerToolbar: { end: 'dayGridMonth,timeGridWeek' },
			events: events
		});
		console.log("render calendar");
		calendar.render();
	}

	return { render: render };
})();
