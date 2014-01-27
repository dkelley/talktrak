$(function() {
	var baseUrl = "localhost:8080";
// Hardcode for now, there is no sign-in
//  var ownerApiToken = "644fcf6d-19db-4625-ba53-4895fe4954fa";
//  var gameId;
//  var gameName;
//  var gameUrlPath;
//  var game;
//
//  var questionTypeNameThatMeme = 2;
//  var questionTypeAutocompleteThis = 3;
//
//  var initialState = stack.getUrlParameter("state");
//
//  if (initialState && initialState == "start")
//    transitionToStart();
//  else
//    transitionToHome();
//
//  $("body").delegate("[home-start-button]", "click", function() {
//    transitionToStart();
//    return false;
//  });
//
//  $("body").delegate("[game-url-path], [game-name]", "keypress", function(e) {
//    if (e.keyCode === 13) {
//      startGame();
//      return false;
//    }
//  });
//
//  $("body").delegate("[start-game-button]", "click", function() {
//    startGame();
//  });
//
//  function startGame() {
//    var urlPath = $("[game-url-path]").val();
//    var name = $("[game-name]").val();
//    stack.api({
//      url : "/api/games",
//      method : "POST",
//      stateful : false,
//      apiToken : ownerApiToken,
//      data : {
//        name : name,
//        urlPath : urlPath
//      },
//      onSuccess : function(response) {
//        gameId = response.gameId;
//        gameName = name;
//        gameUrlPath = urlPath;
//        transitionToWaiting();
//        $(".betaTag").show();
//      },
//      onFailure : function(error) {
//        alert(error.description);
//      }
//    });
//  }
//
//  function transitionToHome() {
//    cancelTasks();
//
//    var source = $("#home-template").html();
//    var template = Handlebars.compile(source);
//
//    $("body").removeClass().addClass("body-home");
//    $("#content").html(template);
//
//    function applyBigtext() {
//      $(".lpi-header").bigtext();
//    }
//
//    applyBigtext();
//
//    // a bug in chrome forces us to reapply bigtext once the page has finished
//    // rendering
//    setTimeout(function() {
//      applyBigtext();
//    }, 300);
//  }
//
//  function transitionToStart() {
//    cancelTasks();
//
//    var source = $("#start-template").html();
//    var template = Handlebars.compile(source);
//
//    $("body").removeClass().addClass("body-start");
//    $("#content").html(template);
//
//    stack.focus($("[game-url-path]"));
//  }
//
//  function transitionToWaiting() {
//    cancelTasks();
//    startDotsTask();
//    startWaitingTask();
//
//    var source = $("#waiting-template").html();
//    var template = Handlebars.compile(source);
//
//    var data = {
//      description : gameName,
//      urlPath : gameUrlPath
//    };
//
//    $("body").removeClass().addClass("body-waiting");
//    $("#content").html(template(data));
//  }
//
//  function transitionToAutocompleteQuestion(question) {
//    cancelTasks();
//    startPlayerAnswerTask(question.questionId);
//
//    var source = $("#autocomplete-template").html();
//    var template = Handlebars.compile(source);
//
//    var data = {
//      gameName : game.name,
//      gameDescription : "Autocomplete This!",
//      gameInstructions : "What's the FIRST autocomplete Google suggests?",
//      urlPath : game.urlPath,
//      question : question.question,
//      answers : question.answers
//    };
//
//    $("body").removeClass().addClass("body-autocomplete").addClass("body-game");
//    $("#content").html(template(data));
//  }
//
//  function transitionToNameThatMemeQuestion(question) {
//    cancelTasks();
//    startPlayerAnswerTask(question.questionId);
//
//    var source = $("#name-that-meme-template").html();
//    var template = Handlebars.compile(source);
//
//    var data = {
//      gameName : game.name,
//      gameDescription : "Name That Meme",
//      gameInstructions : "What is the name of this meme?",
//      urlPath : game.urlPath,
//      imageUrl : question.imageUrl,
//      answers : question.answers
//    };
//
//    $("body").removeClass().addClass("body-namethatmeme").addClass("body-game");
//    $("#content").html(template(data));
//  }
//
//  function transitionToAnswer(question, answer) {
//    cancelTasks();
//
//    var source = $("#answer-template").html();
//    var template = Handlebars.compile(source);
//
//    var description = "";
//
//    if (question.questionTypeId == questionTypeNameThatMeme)
//      description = "Name That Meme!";
//    else if (question.questionTypeId == questionTypeAutocompleteThis)
//      description = question.question;
//
//    var data = {
//      imageUrl : question.imageUrl,
//      answer : answer.answer,
//      description : description,
//      urlPath : game.urlPath
//    };
//
//    $("body").removeClass().addClass("body-answer");
//    $("#content").html(template(data));
//
//    // Load up the winners
//    stack.api({
//      url : "/api/games/" + gameUrlPath + "/question/" + question.questionId + "/player-answers",
//      method : "GET",
//      stateful : false,
//      apiToken : ownerApiToken,
//      onSuccess : function(response) {
//        var ul = $("[users-for-correct-answer]");
//
//        var names = response[answer.answerId];
//
//        if (names) {
//          $(names).each(function(i, name) {
//            var li = $("<li class='lpi-user'>");
//            li.css("left", (i * 65) + "px");
//            li.text(name);
//            ul.append(li)
//          });
//        } else {
//          console.log("No one got the right answer");
//          $("[result-comment]").text("You're all losers!").addClass("youre-all-losers");
//        }
//      },
//      onFailure : function(error) {
//        console.log("Unable to load answers", error);
//      }
//    });
//  }
//
//  function performGameLogic() {
//    cancelTasks();
//
//    if (game.questions.length === 0) {
//      console.log("Game over!");
//      return;
//    }
//
//    var delayInSeconds = game.secondsUntilNextQuestion;
//
//    $.each(game.questions, function(i, question) {
//      setTimeout(function() {
//        if (question.questionTypeId == questionTypeNameThatMeme)
//          transitionToNameThatMemeQuestion(question);
//        else if (question.questionTypeId == questionTypeAutocompleteThis)
//          transitionToAutocompleteQuestion(question);
//        else
//          console.log("Unknown question type ID: " + question.questionTypeId);
//      }, delayInSeconds * 1000);
//
//      delayInSeconds += question.secondsToAnswer;
//
//      var correctAnswer;
//
//      $.each(question.answers, function(j, answer) {
//        if (answer.correct) {
//          correctAnswer = answer;
//          return false;
//        }
//      });
//
//      setTimeout(function() {
//        transitionToAnswer(question, correctAnswer);
//      }, delayInSeconds * 1000);
//
//      delayInSeconds += game.secondsBetweenQuestions;
//    });
//  }
//
//  function cancelTasks() {
//    if (dotsInterval) {
//      clearInterval(dotsInterval);
//      dotsInterval = null;
//    }
//
//    if (waitingWebSocket) {
//      waitingWebSocket.close();
//      waitingWebSocket = null;
//    }
//
//    if (waitingTimeout) {
//      clearTimeout(waitingTimeout);
//      waitingTimeout = null;
//    }
//
//    if (playerAnswerWebSocket) {
//      playerAnswerWebSocket.close();
//      playerAnswerWebSocket = null;
//    }
//
//    if (playerAnswerTimeout) {
//      clearTimeout(playerAnswerTimeout);
//      playerAnswerTimeout = null;
//    }
//  }
//
//  var dotsInterval;
//
//  function startDotsTask() {
//    var dots = 0;
//    dotsInterval = setInterval(function type() {
//      if (dots < 3) {
//        $('.dots').append('.');
//        dots++;
//      } else {
//        $('.dots').html('');
//        dots = 0;
//      }
//    }, 600);
//  }
//
	console.log("WebSocket js loaded");
	
  function startWaitingTask() {
    if (window.WebSocket)
      startWaitingTaskUsingWebSocket();
    else
      startWaitingTaskUsingPolling();
  }

  var waitingWebSocket;

  function startWaitingTaskUsingWebSocket() {
    if (waitingWebSocket)
      waitingWebSocket.close();

    waitingWebSocket = new WebSocket("ws://" + baseUrl + "/websocket/point/update");

    waitingWebSocket.onopen = function(open) {
      console.log("WebSocket opened.", open);

//      var command = {
//        ownerApiToken : ownerApiToken,
//        gameId : gameId
//      }
//
//      waitingWebSocket.send(stack.toJson(command));
    };

    waitingWebSocket.onmessage = function(message) {
      waitingWebSocket.close();
      waitingWebSocket = null;

      console.log("WebSocket received message.", message);
//      game = stack.toJavascript(message.data);
//      performGameLogic();
    };

    waitingWebSocket.onclose = function(close) {
      console.log("WebSocket closed.", close);
    };

    waitingWebSocket.onerror = function(error) {
      console.log("WebSocket error occurred.", error);
    };
  }

  var waitingTimeout;

  function startWaitingTaskUsingPolling() {
    function waitingTask() {
      stack.api({
        url : "/api/games/" + gameUrlPath,
        method : "GET",
        stateful : false,
        apiToken : ownerApiToken,
        onSuccess : function(response) {
          if (waitingTimeout)
            clearTimeout(waitingTimeout);

          game = response;
          performGameLogic();
        },
        onFailure : function(error) {
          waitingTimeout = setTimeout(waitingTask, 500);
        }
      });
    }

    waitingTask();
  }
  startWaitingTask();

//
//  function startPlayerAnswerTask(questionId) {
//    if (window.WebSocket)
//      startPlayerAnswerTaskUsingWebSocket(questionId);
//    else
//      startPlayerAnswerTaskUsingPolling(questionId);
//  }
//
//  var playerAnswerWebSocket;
//
//  function startPlayerAnswerTaskUsingWebSocket(questionId) {
//    if (playerAnswerWebSocket)
//      playerAnswerWebSocket.close();
//
//    playerAnswerWebSocket = new WebSocket("ws://" + baseUrl + "/websocket/game/player-answers");
//
//    playerAnswerWebSocket.onopen = function(open) {
//      console.log("WebSocket opened.", open);
//
//      var command = {
//        ownerApiToken : ownerApiToken,
//        questionId : questionId
//      }
//
//      playerAnswerWebSocket.send(stack.toJson(command));
//    };
//
//    playerAnswerWebSocket.onmessage = function(message) {
//      console.log("WebSocket received message.", message);
//      renderPlayerAnswerResponse(stack.toJavascript(message.data));
//    };
//
//    playerAnswerWebSocket.onclose = function(close) {
//      console.log("WebSocket closed.", close);
//    };
//
//    playerAnswerWebSocket.onerror = function(error) {
//      console.log("WebSocket error occurred.", error);
//    };
//  }
//
//  var playerAnswerTimeout;
//
//  function startPlayerAnswerTaskUsingPolling(questionId) {
//    function playerAnswerPollingTask() {
//      stack.api({
//        url : "/api/games/" + gameUrlPath + "/question/" + questionId + "/player-answers",
//        method : "GET",
//        stateful : false,
//        apiToken : ownerApiToken,
//        onSuccess : function(response) {
//          if (playerAnswerTimeout)
//            clearTimeout(playerAnswerTimeout);
//
//          renderPlayerAnswerResponse(response);
//
//          playerAnswerTimeout = setTimeout(playerAnswerPollingTask, 500);
//        },
//        onFailure : function(error) {
//          if (playerAnswerTimeout)
//            clearTimeout(playerAnswerTimeout);
//
//          playerAnswerTimeout = setTimeout(playerAnswerPollingTask, 500);
//        }
//      });
//    }
//
//    playerAnswerPollingTask();
//  }
//
//  function renderPlayerAnswerResponse(response) {
//    $("[users-for-answer]").empty();
//
//    $.each(response, function(answerId, names) {
//      var ul = $("[users-for-answer-" + answerId + "]").empty();
//
//      $(names).each(function(i, name) {
//        var li = $("<li class='lpi-user'>");
//        li.css("left", (i * 65) + "px");
//        li.text(name);
//        ul.append(li)
//      });
//    });
//  }
});