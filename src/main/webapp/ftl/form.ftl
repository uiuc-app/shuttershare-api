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
                <label>latitude
                    <input type="text" name="latitude" value="40.11"/>
                </label>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <label>longitude
                    <input type="text" name="longitude" value="-88.24"/>
                </label>
            </div>
            <div id="group-ids-placeholder">
                <div class="large-12 medium-12 small-12 columns">
                    <label>Group id to share
                        <input type="text" name="groupIds" value="0" />
                    </label>
                </div>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <label>Choose a photo
                    <input type="file" name="image" />
                </label>
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <input id="add-group-id-button" type="button" class="button large-12 medium-12 small-12" value="Add a group to share" />
            </div>
            <div class="large-12 medium-12 small-12 columns">
                <input type="submit" class="button large-12 medium-12 small-12" value="Upload photo" />
            </div>
        </div>
    </form>
</div>

<#include "footer.ftl">

<script>
$(function() {
   function addGroup() {
       $('#group-ids-placeholder').append(
               '<div class="large-12 medium-12 small-12 columns">           \
                    <label>Group id to share                                \
                        <input type="text" name="groupIds" value="0" />     \
                    </label>                                                \
               </div>');
   }

   $('#add-group-id-button').click(function() {
       addGroup();
   });
});
</script>