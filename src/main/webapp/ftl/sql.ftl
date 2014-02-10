<!DOCTYPE html>
<html>
<head>
	<title>SQL Query Runner</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css" rel="stylesheet">

	<script src="//oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="//oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</head>
<body>
<h1>SQL Query Runner</h1>
<#if isResult??>
<table class="table">
	<#if columnKeys??>
	<tr>
		<#list columnKeys as columnKey>
			<th>${columnKey}</th>
		</#list>
	</tr>
	</#if>
	<#if rows??>
	<#list rows as row>
		<tr>
			<#list columnKeys as columnKey>
				<#if columnKey?ends_with("_AT")>
					<td>${row[columnKey]?number_to_datetime}</td>
				<#else>
					<#if row[columnKey]??>
						<#if row[columnKey]?length &gt; 100>
							<td>${row[columnKey]?substring(0, 100)} ...</td>
						<#else>
							<td>${row[columnKey]}</td>
						</#if>
					<#else>
						<td></td>
					</#if>
				</#if>
			</#list>
		</tr>
	</#list>
	</#if>
</table>
</#if>
<form role="form" class="form-div" method="post" >
	<div class="form-group">
		<label for="sql">SQL</label>
		<textarea cols="80" rows="10" id="sql" name="sql"><#if sql??>${sql}</#if></textarea>
		<p class="help-block">Type SQL Select query!</p>
	</div>
	<button type="submit" class="btn btn-default">Submit</button>
</form>
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="//code.jquery.com/jquery.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>
</body>
</html>