<#include "header.ftl">

<form method="post" action="/j_spring_security_check">
    <div class="row">
        <#if error??>
            <div class="large-12 medium-12 small-12 columns">
                Your login attempt was not successful, try again.
            </div>
        </#if>
        <div class="large-12 medium-12 small-12 columns">
            <label>Email
                <input type="text" placeholder="Your Email" name="j_username"/>
            </label>
        </div>
        <div class="large-12 medium-12 small-12 columns">
            <label>Password
                <input type="password" placeholder="Password" name="j_password"/>
            </label>
        </div>
        <div class="large-12 medium-12 small-12 columns">
            <input class="button small large-12 medium-12 small-12" type="submit" value="Login" />
        </div>
        <hr/>
        <div class="large-12 medium-12 small-12 columns" style="text-align: center;">
            <p>or</p>
        </div>
        <div class="large-12 medium-12 small-12 columns">
            <input class="button small large-12 medium-12 small-12" type="submit" value="Create Account" />
        </div>
    </div>
</form>

<#include "footer.ftl">
