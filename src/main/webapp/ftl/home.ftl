<#include "header.ftl">

<div class="row">
	<div class="large-12 medium-12 small-12 columns">
        <#if photoEntries??>
            <#list photoEntries as photoEntry>
            <div class="panel">
                <a class="th" href="${_contextRoot}/photos/${photoEntry.photoId}/image.jpg?api_key=${api_key}">
                    <img src="${_contextRoot}/photos/${photoEntry.photoId}/image.jpg?api_key=${api_key}">
                </a>
                <div style="text-align: center; padding-top: 10px;">
                    <a href="${_contextRoot}/photos/${photoEntry.photoId}/image.jpg?api_key=${api_key}">${_contextRoot}/photos/${photoEntry.photoId}/image.jpg?api_key=${api_key}</a>
                </div>
            </div>
            </#list>
        </#if>
	</div>
</div>

<#include "footer.ftl">
