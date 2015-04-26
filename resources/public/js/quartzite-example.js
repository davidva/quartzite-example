var form = $("form")
var jobsScheduledCounter = 0;

var updateJobScheduledCounter = function() {
  $(".jobs-scheduled-counter").text("" + jobsScheduledCounter);
}

var updateJobTriggeredCounter = function() {
  $.ajax({
    url: "/counter"
  }).done(function(data) {
    $(".jobs-triggered-counter").text("" + data.counter);
    setTimeout(updateJobTriggeredCounter, 5000);
  });
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
  updateJobTriggeredCounter();

	form.on("submit", function() {
    var message = $("input[name='message']").val();

    if (message.length > 0) {
      scheduleJob(message);
    }
		return false;
	})
})