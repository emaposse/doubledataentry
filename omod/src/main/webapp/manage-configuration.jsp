
<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage DDE Configurations" otherwise="/login.htm" redirect="/admin" />

<%@ include file="doubledataentry-header.jsp" %>

<script type="text/javascript">
    var $j = jQuery.noConflict();
    var pageContext = "${pageContext.request.contextPath}";

    function doubleDataEntryfetchForms(pageContext, searchText) {
        console.log('fetcher is called');
        var url = pageContext + '/ws/module/doubledataentry/configuration/forms?search=' + searchText;
        jQuery.getJSON(url, function(data) {
            console.log(data);
        });
    }

    $j(document).ready(function() {
        console.log('Document is ready');
        $j('#form-search-button').click(function() {
            var toSearch = $j('input[name=search]').val();
            doubleDataEntryfetchForms(pageContext, toSearch);
        });
    });
</script>


<h2><spring:message code="doubledataentry.title" /></h2>

<h4>Frequency Settings</h4><hr/>

Default Frequency: <input type="text" value="${configuration}"/>
<input type="text" placeholder="Type form name..." name="search" />
<button class="btn btn-default" id="form-search-button">Search</button>
<%@ include file="/WEB-INF/template/footer.jsp"%>
