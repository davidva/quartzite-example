var form = $("form")
var jobsScheduledCounter = 0;

var updateJobScheduledCounter = function() {
  $(".jobs-scheduled-counter").text("" + jobsScheduledCounter);
}

var scheduleJob = function(message) {
  $.ajax({
    url: "/schedule",
    method: "post",
    data: "message=" + message
  }).done(function(data) {
    jobsScheduledCounter++;
    updateJobScheduledCounter();

    form.trigger("reset");
  });
}

$(document).ready(function() {
  updateJobScheduledCounter();

	form.on("submit", function() {
    var message = $("input[name='message']").val();

    if (message.length > 0) {
      scheduleJob(message);
    }
		return false;
	})
})