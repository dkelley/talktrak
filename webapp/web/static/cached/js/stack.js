window.stack = {};

stack.debuggingEnabled = true;
stack.characterEncoding = "UTF-8";

/**
 * Global collection of API calls that are currently executing -
 * used by stack.api(params) to prevent double-click issues.
 */
stack.apiCallsInProgress = {};

stack.displayErrors = function(error) {
	  var errors = [];

	  if (error.fieldErrors)
	    $.each(error.fieldErrors, function(i, fieldError) {
	      errors.push(fieldError.error);
	    });

	  if (error.globalErrors)
	    $.each(error.globalErrors, function(i, globalError) {
	      errors.push(globalError);
	    });

	  if (errors.length === 0)
	    errors.push(error.description);

	  $("#alertify-logs").empty();
	  alertify.set({ delay: 10000 });
	  $.each(errors, function(i, error) {
	    alertify.error(error, "", 0);
	  });
	};

stack.api = function(params) {
    if (!params)
        throw "You must supply parameters.";
    
    if(!params.url)
    	throw "You must supply an API URL.";    
    
    if(!params.method)
    	throw "You must supply a method, e.g. POST.";

    var url = params.url;
    var jsonIndentation = stack.debuggingEnabled ? 2 : 0;    
    var method = $.trim(params.method).toUpperCase();

    var headers = {
    		// Lets the server know what page we're on
    		"X-Page-Url": location.href,
    		// By default, API requests are stateful (use the session) unless we explicitly override
    		"X-API-Stateful": params.stateful !== undefined ? params.stateful : "true"
    		};
    if (params.apiToken !== undefined)
        headers["X-Api-Token"] = params.apiToken; 

    var data;
    
    // If we're already processing a call to this endpoint, don't process another one until the current call ends.
    if (stack.apiCallsInProgress[url] === true) {
    	stack.log("Ignoring duplicate request - we're still busy processing an existing call to " + url);
        return;
    }

    stack.apiCallsInProgress[url] = true;

    // We handle GET requests differently from non-GETs (e.g. POSTs).
    //
    // Since GET requests cannot include data in the request body, we pass everything over as
    // URL parameters and add an extra "json" parameter that is a JSON object containing all
    // the parameters and their values.
    //
    // For non-GETs, we take the parameters and turn them into a JSON object and pass that along
    // in the request body.
    if (params.data === undefined) {
        stack.log("Performing API " + method + " for " + url + " with no parameters.");
        data = method == "GET" ? {} : "{}";
    } else {
        if (method == "GET") {
            data = {};

            // Copy out all provided data into an object that we'll send to the server
            $.each(params.data, function(key, value) {
                data[key] = value;
            });

            // Turn all params into a big JSON string parameter named "json"
            data.json = stack.toJson(data, jsonIndentation);

            stack.log("Performing API GET for " + url + " with parameters: " + data.json);
        } else {
            data = stack.toJson(params.data, jsonIndentation);
            stack.log("Performing API POST for " + url + " with parameters: " + data);
        }
    }

    $.ajax({
        type: method,
        url: url,
        data: data,
        headers: headers,
        contentType: (method == "GET" ? "application/x-www-form-urlencoded" : "application/json") + "; charset=" + stack.characterEncoding,
        dataType: "json",
        success: function(response, textStatus, jqXHR) {
            if (stack.debuggingEnabled)
                stack.log("API " + method + " for " + url + " succeeded (status " + jqXHR.status + "). Server said:", stack.toJson(response, jsonIndentation));

            try {
                if (params.onSuccess !== undefined)
                    params.onSuccess(response, jqXHR.status);
            } finally {
                delete stack.apiCallsInProgress[url];
            }
        },
        error: function(xmlHttpRequest, textStatus, errorThrown) {
            stack.log("API " + method + " for " + url + " failed. Server status code was " + xmlHttpRequest.status + " (" + (stack.isBlank(errorThrown) ? "no response from server" : errorThrown) + ").");

            var aborted = textStatus === "abort";

            try {
                if (xmlHttpRequest.status === 0) {
                	// Wrap this in a setTimeout to handle the case where an API call is cancelled by the user navigating away.
                	// We don't have any way of differentiating between that and a call that failed because the server is down,
                	// so the hack is to wait long enough that the page unloads before displaying the error.
                	// Without the setTimeout, if you leave a page while an API call is in progress, you'll see this error in an alert box.
          	        setTimeout(function() {          	        	
                        if (!aborted || (aborted && callOnFailureIfAborted)) {                        	
                            handleErrorResponse({
                                description: "Oops! The internet connection seems to be down at the moment or the server is unavailable. Please try again in a few seconds."
                            }, xmlHttpRequest.status);
                        }          	        	
          	        }, 5000);
                } else {
                    var response = {};

                    try {
                        response = stack.toJavascript(xmlHttpRequest.responseText);

                        // Redundant "is dev" check here to avoid overhead of JSON marshaling in non-dev environments
                        if (stack.debuggingEnabled) {
                            var responseWithElidedStackTrace = {};
                            $.each(response, function(key, value) {
                                responseWithElidedStackTrace[key] = key === "stackTrace" ? "(elided)" : value;
                            });

                            stack.log("Response body is", stack.toJson(responseWithElidedStackTrace, jsonIndentation));
                        }

                        // Special behavior...if we get a 401 or 403 from an API call, redirect to wherever the API response tells us to (e.g. the sign-in page).
                        // TODO: revisit this
                        if (response.destination && (xmlHttpRequest.status === 401 || xmlHttpRequest.status === 403)) {
                            location.href = response.destination;
                            return;
                        }
                    } catch(e) {}

                    handleErrorResponse(response, xmlHttpRequest.status);
                }
            } finally {
                delete stack.apiCallsInProgress[url];
            }
        }
    });

    function handleErrorResponse(response, status) {
        var defaultErrorMessage = "Sorry, an unexpected error has occurred. The technical team has been notified of the problem.";        
        var description = (response === undefined || stack.isBlank(response.description)) ? defaultErrorMessage : response.description;
        var errorCode = (response === undefined || response.errorCode === undefined) ? 1000 /* TODO: plug in the real default */ : response.errorCode;
        status = status || 0;
        
        // If there's no response provided, make a fake one so client code doesn't choke.
        if (response === undefined) {
            if (params.onFailure === undefined) {
                alert(description);
            } else {
                params.onFailure({
                    description: description,
                    errorCode: errorCode 
                }, status);
            }
        } else {
        	// There is a response - pass it along to the client.
            if (stack.debuggingEnabled && response !== undefined && response.stackTrace !== undefined)
                stack.log("Stack trace:\n", response.stackTrace);            
            
            if (params.onFailure === undefined) {
                alert(description);
            } else {
            	// Make sure it has correct defaults in case the server didn't return one of these fields
            	response.description = description;
            	response.errorCode = errorCode;
                params.onFailure(response, status);
            }
        }
    }
};

stack.log = function() {
    if(!window.console || !window.console.log || !window.console.log.apply)
        return;

    console.log.apply(console, arguments);
};

stack.toJson = function(object, indentation) {
    return JSON.stringify(object, null, indentation);
};

stack.toJavascript = function(json) {
    return JSON.parse(json);
};

stack.urlEncode = function(value) {
    return encodeURIComponent(value).replace("'", "%27");
};

stack.stringEndsWith = function(string, endsWith) {
    return new RegExp(stack.escapeRegex(endsWith) + "$").test(string);
};

stack.escapeHtml = function(string) {
    var element = $("<div>");
    element.text(string);
    return element.html();
};

// Escapes all special regex characters in string and returns the result.
// Useful for using a user-entered string as a regex.
// Thanks to Theodor Zoulias        
stack.escapeRegex = function(string) {
    return string.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
};

// Takes a base URL without a parameter string and a dictionary of parameters (optional).
stack.constructUrl = function(urlBase, parameters) {
    var url = urlBase;
    parameters = parameters || {};

    if (stack.countKeysInObject(parameters) == 0)
        return url;

    url += "?";

    $.each(parameters, function(key, value) {
        url += (stack.stringEndsWith(url, "?") ? "" : "&") + stack.urlEncode(key) + "=" + stack.urlEncode(value);
    });

    return url;
};

stack.countKeysInObject = function (object) {
    var count = 0;
    for (var key in object)
        // if (object.hasOwnProperty(key)) // doesn't work in IE!
        ++count;

    return count;
};

stack.scrollTo = function(scrollTop, duration) {
    scrollTop = scrollTop || 0;

    if (duration === undefined)
        duration = "slow";

    $("html:not(:animated), body:not(:animated)").animate({scrollTop: scrollTop}, duration);
};

stack.getUrlParameter = function(name, url) {
    return stack.getUrlParameters(url)[name];
};

stack.getUrlParameters = function(url) {
    url = url || window.location.href;
    var map = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
        map[key] = value;
    });

    return map;
};

stack.focus = function(selector) {
    if (!selector.focus)
        return;

    setTimeout(function() {
        try {
        	selector.focus();
        } catch(e) {}
    }, 10);
};

stack.isBlank = function(string) {
	return string === undefined || $.trim(string).length === 0;
};

