$(document).ready(function() {
	var form = $("form")

	form.on("submit", function() {
		$.ajax({
			url: "/schedule",
			method: "post",
			data: form.serialize()
		}).done(function(data) {
			form.trigger("reset")
		});
		return false;
	})
})