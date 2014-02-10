<!DOCTYPE html>
<html>
<head>
	<title>Bootstrap 101 Template</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css" rel="stylesheet">

	<script src="//oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="//oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</head>
<body>
<#if mapper??>
<h1>Generated Source</h1>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">${mapperClassName}.java</h3>
	</div>
	<div class="panel-body">
		<pre>${mapper}</pre>
	</div>
</div>
<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">MySQL schema for table "${tableName}"</h3>
	</div>
	<div class="panel-body">
		<pre>${tableSchema}</pre>
	</div>
</div>
<#else>
<h1>MyBatis Mapper Generator!</h1>
<form role="form" class="form-div" method="post" enctype="multipart/form-data">
	<div class="form-group">
		<label for="source">Source File</label>
		<input type="file" id="source" name="source"/>
		<p class="help-block">Upload VO file.</p>
	</div>
	<button type="submit" class="btn btn-default">Submit</button>
</form>
</#if>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="//code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
</body>
</html>