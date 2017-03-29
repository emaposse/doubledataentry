/**
 * Created by Willa Mhawila on 3/20/17.
 */
var $j = jQuery.noConflict();
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
            $j('#unsaved-configuration-table > tbody').empty();
            formsTobeConfigured = [];
            $j('#unsaved-config-container').hide();

            // Append to configs
            var configTableBody = $j('#configuration-table > tbody');
            createdConfigs.forEach(function(configuration) {
                var tableRow = $j('<tr>').attr('id', 'config-tr-' + configuration.uuid);
                var html = '<td><input type="checkbox" name="toBeModified[]" value="' + configuration.uuid + '" class="form-check"/></td>' +
                    '<td>' + configuration.htmlForm.name + '</td>' +
                    '<td>' + configuration.revision + '</td>' +
                    '<td><input type="text" name="configFrequency[]" value="' + configuration.frequency + '" class="form-control"/></td>';

                if(configuration.dateChanged) {
                    html += '<td>' + (new Date(configuration.dateChanged)).toISOString().replace('T',' ').replace('Z','') + '</td>';
                }
                else {
                    html += '<td>' + (new Date(configuration.dateCreated)).toISOString().replace('T',' ').replace('Z','') + '</td>';
                }

                if(configuration.published) {
                    html += '<td><input type="checkbox" name="published[]" class="form-check" checked ' +
                        'value="' + configuration.id + '"/></td>';
                }
                else {
                    html += '<td><input type="checkbox" name="published[]" class="form-check" ' +
                        'value="' + configuration.id + '"/></td>';
                }

                if(configuration.revision > 1){
                    html += '<td><a>View History</a><td>';
                }
                else {
                    html += '<td></td>';
                }

                tableRow.html(html).prependTo(configTableBody);

                if(!$j('#configuration-container').is(':visible')) {
                    $j('#configuration-container').show();
                    $j('#no-configuration-message').hide();
                }
            });
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

function toggleRetireButton() {
    var someChecked = $j('input[name="toBeModified[]"]:checked').length;

    if(someChecked > 0) {
        $j('button[name="retire-button"]').prop('disabled', false);
    }
    else {
        $j('button[name="retire-button"]').prop('disabled', true);
        toggleRetireReasonTextArea(false);
    }
}

function retireConfigurations() {
    toggleRetireReasonTextArea(true);
    // createRetiredConfigurations();
}

function toggleRetireReasonTextArea(show) {
    if(show) {
        $j('#configurations-retire-reason').show();
    }
    else {
        $j('#configurations-retire-reason').hide();
    }
}

function createRetiredConfigurations() {
    var reason = $j('textarea[name="retireReason"]').val().trim();
    var checked = $j('input[name="toBeModified[]"]:checked');

    var configMaps = [];
    for(var i=0; i < checked.length ; i++) {
        configMaps.push({
            uuid: $j(checked[i]).val(),
            reason: reason
        });
    }

    return configMaps;
}

function saveRetiredConfigurations() {
    var url = pageContext + '/ws/module/doubledataentry/configuration';
    var configsToRetire = createRetiredConfigurations();
    $j('#save-retired-busy-image').show();
    $j.ajax({
        contentType: 'application/json',
        type: 'DELETE',
        url: url,
        data: JSON.stringify(configsToRetire),
        success: function() {
            // Append to configs
            $j('#save-retired-busy-image').hide();
            configsToRetire.forEach(function (config) {
                var selector = '#config-tr-' + config.uuid;
                $j(selector).remove();
            });

            //Hide the reason thing
            toggleRetireButton();
        },
        error: function(jqXHR, textStatus, error) {
            $j('#save-retired-busy-image').hide();
            console.error('Status:', textStatus, 'Error:', error);
        }
    });
}

$j(document).ready(function() {
    $j('#configuration-table').DataTable({
        searching: false,
        ordering: false,
        select: true
    });

    $j('#unsaved-config-container').hide();

    $j('#form-search-button').click(function() {
        var toSearch = $j('input[name=search]').val();
        doubleDataEntryfetchForms(pageContext, toSearch);
    });

    $j('textarea[name="retireReason"]').keyup(function() {
        if($j(this).val().trim().length > 0) {
            $j('#save-retired').prop('disabled',false);
        }
        else {
            $j('#save-retired').prop('disabled',true);
        }
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
            var unsavedTableBody = $j('#unsaved-configuration-table > tbody');
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

            tableRow.html(html).append(lastCol).appendTo(unsavedTableBody);

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