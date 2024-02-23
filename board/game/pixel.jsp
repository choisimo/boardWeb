<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>픽셀 그림</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/game/pixel.css">
</head>
<body>
<div class="wrapper">
  <header>
    <h1>Pixel Art Studio</h1>
    <p>Make your own single-div pixel art with CSS.</p>
  </header>
  <div class="controls">
    <label for="colorPicker">Color picker</label>
    <input type="color" id="colorPicker">
    <label for="cellSize">Columns</label>
    <input type="range" min="10" max="60" id="columns" step="10" value="30">
    <button data-undo>Undo</button>
    <button data-clear>Clear</button>
    <button data-save>Save</button>
  </div>
  <div class="canvas">
    <div class="draw-area">
      <div class="marker"></div>
    </div>
  </div>
  <div class="css">
    <label for="css">CSS:</label>
    <textarea name="cssCode" id="css" rows="10"></textarea>
    <button data-copy>Copy to clipboard</button>
  </div>
</div>
<script src="https://d3js.org/d3.v6.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/game/pixel.js"></script>
</body>
</html>