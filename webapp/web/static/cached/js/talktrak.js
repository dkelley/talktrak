$(function() {	
 	$('a[signoutlink]').bind('click', function() {
 		stack.api({
			url: "/api/accounts/sign-out",
			method: "POST",
			onSuccess: function(response) {
				var destinationUrl = stack.getUrlParameter("destinationUrl");				
				location.href = destinationUrl ? destinationUrl : response.destinationUrl;
			},
			onFailure: function(error) {
				$("[errors]").text(error.description).show();
			}		
		});
	});
});

