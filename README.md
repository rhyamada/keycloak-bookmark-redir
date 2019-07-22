# Problem

[https://lists.jboss.org/pipermail/keycloak-user/2017-November/012321.html]

[https://lists.jboss.org/pipermail/keycloak-user/2017-August/011623.html]


# Solution proposed

Edit login.ftl and put this code on end

    <script>
    window.history.replaceState(null, null, location.href.replace(/([&?])state=/,'$1bookmarked='));
    </script>


Then when user gets login page the "state" param is changed to "bookmarked" 
If user bookmark this url we can detect a bookmarked page and redirect user back
client base url to starts a correct OIDC flow with new state

# Installation

    git clone https://github.com/rhyamada/keycloak_bookmark_redir
    cd keycloak_bookmark_redir
    mvn package

Copy gerenated .jar file into standalone/deployments folder in keycloak instalation

Don't forget edit themes/base/login.ftl and add

    <script>
    window.history.replaceState(null, null, location.href.replace(/([&?])state=/,'$1bookmarked='));
    </script>

# Test

On Keycloak admin create a new browser authflow,

Add execution "Bookmark redirect" on flow, as first execution!

Create a client in keycloak, fill Base URL

Access that client url in browser, on keycloak login page then <Ctrl>+D to bookmark-it

Use bookmark, keycloak should redirect back base url and follow login correctly

