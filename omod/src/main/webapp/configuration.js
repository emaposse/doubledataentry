/**
 * Created by mhawilamhawila on 3/20/17.
 */

function doubleDataEntryfetchForms(searchText) {
    jQuery.getJSON('/ws/module/doubledataentry/configuration/forms', function(data) {
        console.log(data);
    });
}