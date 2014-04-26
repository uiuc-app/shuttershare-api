<#include "header.ftl">

<div class="row">
	<div class="large-12 medium-12 small-12 columns">
        <#if photos??>
            <#list photos as photo>
            <div class="panel">
                <a class="th" href="${_contextRoot}/photos/${photo.id}/image.jpg?api_key=${api_key}">
                    <img src="${_contextRoot}/photos/${photo.id}/image.jpg?api_key=${api_key}">
                </a>
                <div style="text-align: center; padding-top: 10px;">
                    <a href="${_contextRoot}/photos/${photo.id}/image.jpg?api_key=${api_key}">${_contextRoot}/photos/${photo.id}/image.jpg?api_key=${api_key}</a>
                </div>
            </div>
            </#list>
        </#if>
	</div>
</div>

<#include "footer.ftl">
