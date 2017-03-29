<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage DDE Configurations" otherwise="/login.htm" redirect="/admin" />

<%@ include file="doubledataentry-header.jsp" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<openmrs:htmlInclude file="/moduleResources/doubledataentry/bootstrap/css/bootstrap-3.0.3.min.css" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/datatables.min.css" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/bootstrap/js/bootstrap-3.0.3.min.js" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/datatables.min.js" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/configuration.css"/>
<openmrs:htmlInclude file="/moduleResources/doubledataentry/configuration.js"/>

<script type="text/javascript">
    var pageContext = "${pageContext.request.contextPath}";
    var defaultFrequency = '${defaultFrequency}';
</script>

<h2><spring:message code="doubledataentry.title" /></h2>

<h4>Frequency Settings</h4><hr/>

<div style="width:80%;">
    <fieldset class="custom-border">
        <legend class="custom-border">Add New Configurations</legend>
        <div class="ui-widgets">
            <input type="text" placeholder="Search form to add (Type name)..."
                name="search" id="forms" class="form-control"/>
        </div>
        <br/>
        <!-- Table for newly created configurations (i.e. not yet saved) -->
        <div id="unsaved-config-container" style="display:none;">
            <table id="unsaved-configuration-table" class="table table-stripped table-bordered">
                <thead>
                    <tr>
                        <th>Form Name</th>
                        <th>Revision</th>
                        <th>Frequency</th>
                        <th>Date Started</th>
                        <th>Published</th>
                        <th>&nbsp;&nbsp;</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
            <button class="btn btn-sm btn-default" onclick="saveConfigurations()">Save Configuration(s)</button>
        </div>
    </fieldset>

    <fieldset class="custom-border">
        <legend class="custom-border">Existing Configurations</legend>
        <div class="alert alert-warning" id="no-configuration-message"
                            style="display:${empty configurations ? 'block' : 'none'}">
            No configurations added yet.
        </div>
        <div id="configuration-container" style="display:${empty configurations ? 'none' : 'block'}">
            <table id="configuration-table" class="table table-stripped table-bordered">
                <thead>
                    <tr>
                        <th>&nbsp;&nbsp;</th>
                        <th>Form Name</th>
                        <th>Revision</th>
                        <th>Frequency</th>
                        <th>Date Started</th>
                        <th>Published</th>
                        <th>&nbsp;&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="configuration" items="${configurations}">
                    <tr id="config-tr-${configuration.uuid}">
                        <input type="hidden" name="config${configuration.id}" value="${configuration.uuid}"/>
                        <td>
                            <input type="checkbox" name="toBeModified[]" value="${configuration.uuid}"
                                onclick="toggleRetireButton()" class="form-check"/>
                        </td>
                        <td><c:out value="${configuration.htmlForm.form.name}"/></td>
                        <td><c:out value="${configuration.revision}"/></td>
                        <td>
                            <fmt:formatNumber value="${configuration.frequency}" type="percent" var="percentFrequency"/>
                            <c:choose>
                                <c:when test="${configuration.retired}">
                                    <c:out value="${percentFrequency}"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="config${configuration.id}Frequency" value="${percentFrequency}" class="form-control"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${configuration.dateChanged != null ? configuration.dateChanged : configuration.dateCreated}"/></td>
                        <td>
                            <c:choose>
                            <c:when test="${configuration.published}">
                                <input type="checkbox" name="published[]" class="form-check" checked value="${configuration.uuid}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="published[]" class="form-check" value="${configuration.uuid}"/>
                            </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${configuration.revision > 1 }">
                                <a>View History</a>
                            </c:if>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button class="btn btn-sm btn-default" onclick="retireConfigurations()" disabled="true" name="retire-button">Retire Selected</button>
            <button class="btn btn-sm btn-default">Apply Changes</button>
            <div id="configurations-retire-reason" style="display:none;" class="top-buffer">
                <span>Provide Reason</span>
                <textarea name="retireReason" class="form-control" placeholder="I don't like these configurations"></textarea>
                <button class="btn btn-sm btn-warning" disabled="true" id="save-retired" onclick="saveRetiredConfigurations()">
                    Save
                    <img src="${pageContext.request.contextPath}/moduleResources/doubledataentry/images/busy.gif" id="save-retired-busy-image" style="display:none;"/>
                </button>
            </div>
        </div>
    </fieldset>

    <fieldset class="custom-border">
        <legend class="custom-border">Participating Users</legend>
        <div>
            <input type="text" placeholder="Search user..." name="search" class="form-control"/>
            <button class="btn btn-sm btn-default" id="user-search-button">Search</button>
        </div>
        <div>
            <input type="checkbox" name="useAllUsers"/>All Users in the system
        </div>
        <div>
            <table id="users-table">
                <thead>
                    <tr>
                        <th></th>
                        <th>User Name</th>
                        <th>System ID</th>
                        <th>Roles</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${participants}">
                    <tr>
                        <input type="hidden" name="user${user.id}" value="${user.uuid}"/>
                        <td><input type="checkbox" name="usersToBeModified[]" value="${user.id}"/></td>
                        <td><c:out value="${user.username}"/></td>
                        <td><c:out value="${user.systemId}"/></td>
                        <td>
                            <c:forEach var="role" items="${user.roles}">
                                <c:out value="${role.role}"/> ,
                            </c:forEach>
                        </td>
                    </c:forEach>
                </tr>
                </tbody>
            </table>
            <button class="btn btn-sm btn-default">Remove Selected</button>
        </div>
    </fieldset>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>
