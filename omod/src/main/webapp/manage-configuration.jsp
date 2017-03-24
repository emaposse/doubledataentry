<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage DDE Configurations" otherwise="/login.htm" redirect="/admin" />

<%@ include file="doubledataentry-header.jsp" %>

<openmrs:htmlInclude file="/moduleResources/doubledataentry/bootstrap/css/bootstrap-3.0.3.min.css" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/bootstrap/js/bootstrap-3.0.3.min.js" />
<openmrs:htmlInclude file="/moduleResources/doubledataentry/configuration.css"/>

<script type="text/javascript">
    var $j = jQuery.noConflict();
    var pageContext = "${pageContext.request.contextPath}";
    var defaultFrequency = '${defaultFrequency}';
    var formsTobeConfigured = [];
    function doubleDataEntryfetchForms(pageContext, searchText) {
        console.log('fetcher is called');
        var url = pageContext + '/ws/module/doubledataentry/configuration/forms?search=' + searchText;
        $j.ajax({
            contentType: 'application/json',
            url: url,
            success: function(data) {
                console.log('data: ', data);
            },
            error: function(jqXHR, textStatus, error) {
                console.log('Status ni:', textStatus, 'Error ni:', error);
            }
        });
    }

    function removeUnconfiguredForm(form) {
        $j('#' + form.uuid).remove();
        var indexToRemove = formsTobeConfigured.findIndex(function(f) {
            return f.uuid === form.uuid;
        });
        if(indexToRemove > -1) {
            formsTobeConfigured.splice(indexToRemove, 1);
        }
        if(formsTobeConfigured.length === 0) {
            $j('#unsaved-config-container').hide();
        }
    }

    function saveConfigurations() {
        var url = pageContext + '/ws/module/doubledataentry/configuration';
        $j.ajax({
            contentType: 'application/json',
            type: 'POST',
            url: url,
            data: JSON.stringify(createConfigObjects()),
            success: function(createdConfigs) {
                console.log("Returned: ", JSON.stringify(createdConfigs, null, 2));

            },
            error: function(jqXHR, textStatus, error) {
                console.log('Status ni:', textStatus, 'Error ni:', error);
            }
        });
    }

    function createConfigObjects() {
        var formId = $j('input[name="formId[]"]').map(function() {
            return $j(this).val();
        });
        var frequency = $j('input[name="frequency[]"]').map(function() {
            return $j(this).val();
        });
        var published = $j('input[name="published[]"]:checked').map(function() {
            if($j(this).length > 0) return 'checked';
            return 'unchecked';
        });
        var configs = [];
        for(var i=0; i<formId.length ; i++) {
            configs.push({
                formId: formId[i],
                frequency: frequency[i],
                published: published[i] === 'checked' ? true: false,
            });
        }
        return configs;
    }

    $j(document).ready(function() {
        $j('#unsaved-config-container').hide();

        $j('#form-search-button').click(function() {
            var toSearch = $j('input[name=search]').val();
            doubleDataEntryfetchForms(pageContext, toSearch);
        });

        $j("#forms").autocomplete({
            minLength: 3,
            source: function(request, response) {
                var term = request.term;
                var url = pageContext + '/ws/module/doubledataentry/configuration/forms';
                $j.ajax({
                    contentType: 'application/json',
                    url: url,
                    data: { search: request.term },
                    success: function(forms) {
                        var qualified = forms.filter(function(form) {
                            var matchedForm = formsTobeConfigured.find(function(tobeConfigured) {
                                return form.uuid === tobeConfigured.uuid;
                            });
                            if(matchedForm) return false;
                            return true;
                        });
                        var items = qualified.map(function(form) {
                            return {
                                label: form.name + ' v' + form.version,
                                value: form
                            };
                        });
                        response(items);
                    },
                    error: function(jqXHR, textStatus, error) {
                        console.log('Status ni:', textStatus, 'Error ni:', error);
                    }
                });
            },
            select: function(event, ui) {
                event.preventDefault();
                var form = ui.item.value;
                formsTobeConfigured.push(form);
                var configTableBody = $j('#unsaved-configuration-table > tbody');
                var tableRow = $j('<tr id="' + form.uuid + '">');
                var html = '<td><input type="hidden" name="formId[]" value="' + form.formId + '"/>' + form.name + '</td>' +
                      '<td>1</td>' +
                      '<td><input type="text" name="frequency[]" value=' + defaultFrequency + ' class="form-control"/></td>' +
                      '<td>' + (new Date()).toISOString() + '</td>' +
                      '<td><input type="checkbox" name="published[]" class="form-check" value="checked"/></td>';
                      //'<td><button class="btn btn-xs btn-primary" onclick="removeUnconfiguredForm(form)">Discard</button></td>';
                var removeButton = $j('<button>', { class: "btn btn-xs btn-primary"}).append('Discard').click(function() {
                                        removeUnconfiguredForm(form);
                                   });
                var lastCol = $j('<td>').append(removeButton);
                tableRow.html(html).append(lastCol).appendTo(configTableBody);

                if(!$j('#unsaved-config-container').is(':visible')) {
                    $j('#unsaved-config-container').show();
                }
            },
            _renderItem: function(ul, item) {
                return $j('<li>')
                    .attr('data-value', item.value)
                    .append(item.label)
                    .appendTo(ul);
            }
        });
    });
</script>


<h2><spring:message code="doubledataentry.title" /></h2>

<h4>Frequency Settings</h4><hr/>

<div class="container pull-left">
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
                    <tr>
                        <input type="hidden" name="config${configuration.id}" value="${configuration.uuid}"/>
                        <td><input type="checkbox" name="toBeModified[]" value="${configuration.id}" class="form-control"/></td>
                        <td><c:out value="${configuration.htmlForm.form.name}"/></td>
                        <td><c:out value="${configuration.revision}"/></td>
                        <td><c:out value="${configuration.frequency}"/></td>
                        <td><c:out value="${configuration.dateStarted}"/></td>
                        <td><c:out value="${configuration.published}"/></td>
                        <td>
                            <c:if test="${configuration.revision > 1 }">
                                <a>View History</a>
                            </c:if>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button class="btn btn-sm btn-default">Retire Selected</button>
            <button class="btn btn-sm btn-default">Apply Changes</button>
        </div>
    </fieldset>

    <div class="row">
        Participating Users
    </div>
    <div class="row">
        <input type="text" placeholder="Search user..." name="search" class="form-control"/>
        <button class="btn btn-sm btn-default" id="user-search-button">Search</button>
    </div>
    <div class="row">
        <input type="checkbox" name="useAllUsers"/>All Users in the system
    </div>
    <div class="row">
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
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>
