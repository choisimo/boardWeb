<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>충돌</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/game/collision.css">
</head>
<body>
<div class="start-screen">
  <div class="game-data">
    <span class="name"><span>Color</span> <span>Collision</span></span>
  </div>
  <span class="info">
    <span class="highlight _1">FOCUS</span> is the weapon.
    Don't make the wrong move.
    Tap / click to change the color and save the central balls.
    Because if the central balls don't stay alive <span class="highlight _2">YOU DIE</span>.
    Better play <span class="highlight _3">SAFE</span> and make a new highscore.
  </span>
  <button class="btn play">PLAY</button>
</div>

<span class="retry-text hide" data-splitting></span>
<button class="retry-btn hide" data-splitting>RETRY</button>
<canvas data-canvas></canvas>


<div class="support">
  <a href="https://github.com/devloop01/color-collision" target="_blank"><span>fork at</span><i class="fab fa-github"></i></a>
</div>
<script src="<%=request.getContextPath()%>/assets/js/game/collision.js"></script>
</body>
</html>