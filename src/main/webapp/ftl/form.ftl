<#include "header.ftl">

<div class="panel">
    <form action="${_contextRoot}/upload" method="post" enctype="multipart/form-data">
        <div class="row" style="line-height: 1.5em;">
            <div class="large-12 medium-12 small-12 columns">
                <label>api_key
                    <input type="text" name="api_key" value="<#if api_key??>${api_key}</#if>"/>
                </label>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <label>Group id to share
                    <input type="text" name="groupIds" value="0" />
                </label>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <label>Choose a photo
                    <input type="file" name="image" />
                </label>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <input type="submit" class="button large-12 medium-12 small-12" value="Upload photo" />
            </div>
        </div>
    </form>
</div>

<#include "footer.ftl">
