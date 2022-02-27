
// Retrieve menu from the backend.
$(document).ready(function () {
    var action = "/tabledata";
    header = {
        'X-CSRF-TOKEN': $('#csr-token').val(),
        '${_csrf.parameterName}': $('#csr-token').val()
    };
    //Server.edit(header, action, empDesId,setDataToEdit);
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: action,
        /*data : id,*/
        dataType: 'json',
        headers: header,
        success: function (result) {
            $('#showRangeId').empty();
            var showRange = result.lowerRange + "-" + result.upperRange + "( of " + result.totalRoleMatching + ")";
            $("#showRangeId").val(showRange);
            var digit_length_after_decimal = 0
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                var score_element = two_decimal_digit(clique_score,0)
                var digit_after_decimal = score_element.split('.')[1];
                if(digit_length_after_decimal < digit_after_decimal.length )
                    digit_length_after_decimal = digit_after_decimal.length;
                //alert(digit_length_after_decimal)
            });
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                var score_element = two_decimal_digit(clique_score,digit_length_after_decimal)
                //console.log(score_element)
                var menuList = '<tr><td class="menutext">\n' +
                    '<a href="#" >' +
                    index.split('_RM_')[1] + '</td><td class="menutext"> ' + score_element + ' </td><td class="menutext">' + index.split('_RM_')[0] +
                    /*   '                                    <i class="right fas fa-angle-left"></i>\n' +*/
                    '</a>' +
                    '</td></tr>';
                //console.log(menuList)

                $('#tableId').append(menuList);
            });

        },
        error: function (e) {
            alert("Error!" + e)
        }
    });
//format the click score at two decimal value
    function two_decimal_digit(value,digit_length_after_decimal) {
        var non_zero_counter = 0;
        var BreakException = {};
        var before_decimal = value.split('.')[0];
        var after_decimal = value.split('.')[1];
        var backup_digit = '';
        var display_digit = '';
        try {
            after_decimal.split('').forEach((x, i) => {
                //alert(x)
                if (x != '0') {
                    //alert('non zero condition')
                    if (non_zero_counter == 2) throw BreakException;
                    else {
                        //alert('non zero counter not 2')
                        display_digit += x;
                        backup_digit = x;
                        non_zero_counter += 1;
                    }
                } else {
                    if (non_zero_counter == 2) throw BreakException;
                    else {
                        display_digit += x;
                        if (backup_digit != 0) throw BreakException;
                    }
                }
            });
        } catch (e) {
            if (e !== BreakException) throw e;
        }
        //alert('out of loop')
        var digit_difference = digit_length_after_decimal - display_digit.split('').length
        for (let i = 0; i < digit_difference; i++) {
            display_digit += '0';
        }
        display_digit = before_decimal + '.' + display_digit
        //console.log(display_digit)
        return display_digit
    }


});
// advance search functionality
$(".sa_search").click(function () {
    var searchstring = $("#searchInputId").val() == '' ? 'NAN' : $("#searchInputId").val();
    var size_max = $("#size_max").val() == '' ? 10000 : $("#size_max").val();
    var size_min = $("#size_min").val() == '' ? 0 : $("#size_min").val();
    var score_min = $("#score_min").val() == '' ? 0 : $("#score_min").val();
    var score_max = $("#score_max").val() == '' ? 10000 : $("#score_max").val();
    var is_all = $("#allroles").is(':checked') ? true : false;
    var atleast_one = $("#atleastonerole").is(':checked') ? true : false;
    var property_or_role_id = $("#proper_role_id").val() == '' ? 'NAN' : $("#proper_role_id").val();
    var contentId = $("#contentId").val()
    var percentage = $("#percentage").val()
    var all_mismatch = $("#all_mismatch").is(':checked') ? true : false;
    // For the role matching id search
    var percen_role = $("#per_role_id").val()== '' ? 100 : $("#per_role_id").val();
    var percentage_role_check = $("#per_role_id_match").is(':checked') ? true : false;
    if(percentage_role_check == true){
        if(percen_role== ''){
            alert("Percentage of role matching is missing")
            return
        }

    }
    if(contentId != '') {
        if(percentage == ''){
            alert("Percentage is missing")
            return;
        }

    }
    if(contentId== '' && !all_mismatch)
        var action = "/searchrolematching/" + size_max + "/" + size_min + "/" + score_min + "/" + score_max + "/" + is_all + "/" + searchstring + "/" + property_or_role_id+"/" + percen_role+ "/" + atleast_one;
    else if(all_mismatch )
        var action = "/searchmismatchrolematching/" + all_mismatch;
    else
        var action = "/searchrolematching/" + contentId+"/"+percentage;
    header = {
        'X-CSRF-TOKEN': $('#csr-token').val(),
        '${_csrf.parameterName}': $('#csr-token').val()
    };

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: action,
        data: searchstring,
        dataType: 'json',
        headers: header,
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('#side_loader').removeClass('hidden')
        },
        success: function (result) {
            $('#tableId > tbody').empty();
            $('#showRangeId').empty();
            $('#advance_search_param_id').empty();
            $('#advance_search_param_id').append(contentId);
            advance_params_show( );
            //console.log(result)
            var showRange = result.lowerRange + "-" + result.upperRange + "( of " + result.totalRoleMatching + ")";
            $("#showRangeId").val(showRange);
            var digit_length_after_decimal = 0
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                var score_element = two_decimal_digit(clique_score,0)
                var digit_after_decimal = score_element.split('.')[1];
                if(digit_length_after_decimal < digit_after_decimal.length )
                    digit_length_after_decimal = digit_after_decimal.length;
                //alert(digit_length_after_decimal)
            });
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                console.log(clique_score)
                var score_element = two_decimal_digit(clique_score,digit_length_after_decimal)
                var menuList = '<tr><td class="roleId menutext"">\n' +
                    '<a href="#" >' +
                    index.split('_RM_')[1] + '</td><td class="menutext"> ' + score_element + ' </td><td class="menutext">' + index.split('_RM_')[0] +
                    /*   '                                    <i class="right fas fa-angle-left"></i>\n' +*/
                    '</a>' +
                    '</td></tr>';
                console.log(menuList)
                $('#tableId').append(menuList);

            });
            advance_params_empty();
            $("#myModal").modal('hide');

        },
        complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            $('#side_loader').addClass('hidden')
        },
        error: function (e) {
            alert("Error!" + e)
        }
    });
});
// after clicking the search button, the modal will empty by this function
function advance_params_empty(){
    $("#score_min").val('');
    $("#score_max").val('');
    $("#size_min").val('');
    $("#size_max").val('');
    $("#proper_role_id").val('');
    $("#all_mismatch").val('')
    $("#all_role_matching_id").val('')
    //$("#atleastonerole").
    $("#contentId").val('');
    $("#percentage").val('');
    $("#per_role_id").val('');
    $("#per_role_id_match").val('');
}
// to show the searched parameter in the left hand side menu bar
function advance_params_show(){
    $('#advance_search_params').empty();
     var score_min = $("#score_min").val();
     var score_max = $("#score_max").val();
     var size_min = $("#size_min").val();
     var size_max =  $("#size_max").val();
     var role_prop = $("#proper_role_id").val();
     var allroles =  $("#allroles").is(':checked') ? true : false;
     var atleast_one = $("#atleastonerole").is(':checked') ? true : false;
     var content = $("#contentId").val();
     var percentage = $("#percentage").val();
    var percen_role = $("#per_role_id").val()== '' ? 100 : $("#per_role_id").val();
    var percentage_role_check = $("#per_role_id_match").is(':checked') ? true : false;
    if(score_min != '' || score_max != '')
        $('#advance_search_params').append('<label class="advance_search_params" >Score &nbsp;-> Min:&nbsp;&nbsp;&nbsp; '+score_min+  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Max:&nbsp;&nbsp;&nbsp; '+score_max+  '</label><br>')
    if(size_min != '' || size_max != '')
        $('#advance_search_params').append('<label class="advance_search_params" >Size &nbsp;&nbsp;&nbsp -> Min:&nbsp;&nbsp;&nbsp; '+size_min+  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Max:&nbsp;&nbsp;&nbsp; '+size_max+  '</label><br>')
    if(role_prop != '')
        $('#advance_search_params').append('<label class="advance_search_params" >Role/Property:&nbsp;&nbsp;&nbsp; '+role_prop+  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><br>')
    if(role_prop != '' && allroles == true)
        $('#advance_search_params').append('<label class="advance_search_params" >All roles:&nbsp;&nbsp;&nbsp; '+allroles+  '</label><br>')
    if(role_prop != '' &&  atleast_one == true)
        $('#advance_search_params').append('<label class="advance_search_params" >Atleast one:&nbsp;&nbsp;&nbsp;'+ atleast_one +'</label><br>')
    if(role_prop != '' &&  percentage_role_check == true)
        $('#advance_search_params').append('<label class="advance_search_params" >Percentage of roles:&nbsp;&nbsp;&nbsp;'+ percen_role +'</label><br>')
    if(content != '')
        $('#advance_search_params').append('<label class="advance_search_params" >Content:&nbsp;&nbsp;&nbsp; '+content+  '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Percentage:&nbsp;&nbsp;&nbsp; '+percentage+  '</label><br>')



    }

// show the previous and next role matching id in the left side menu bar
$(".nav-pre-next-btn").click(function () {
    console.log(this.id);
    var nav_btn = this.id;
    var getRange = $("#showRangeId").val();
    const splitbyinterval = getRange.split("-");
    const splitbybracket = splitbyinterval[1].split("(");
    var range = [splitbyinterval[0], splitbybracket[0]]
    var params = 0;
    console.log(range[1])
    if (range.length != 0) {
        var lowerRangeRole = range[0]
        var upperRangeRole = range[1]
        console.log(upperRangeRole)
        if (nav_btn == 'nextBtnId') {
            params = upperRangeRole
            //lower_bound = upperRangeRole
            //upper_bound = lower_bound + 100
        } else {
            if (range[0] < 100) {
                alert("Data not available for the range")
                return
            }
            params = lowerRangeRole - 100
            //upper_bound = lower_bound
            //lower_bound = upper_bound - 100

        }

    }

    var action = "/rangewisemenu/" + params;
    header = {
        'X-CSRF-TOKEN': $('#csr-token').val(),
        '${_csrf.parameterName}': $('#csr-token').val()
    };

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: action,
        /* data : searchstring,*/
        dataType: 'json',
        headers: header,
        success: function (result) {
            //var showRange = result.lowerRange + "-" + result.upperRange  ;
            var showRange = result.lowerRange + "-" + result.upperRange + "( of" + result.totalRoleMatching + ")";
            $('#showRangeId').empty();
            $("#showRangeId").val(showRange);
            //$('#dynamicMenuId').empty();
            $('#tableId > tbody').empty();
            var digit_length_after_decimal = 0
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                var score_element = two_decimal_digit(clique_score,0)
                var digit_after_decimal = score_element.split('.')[1];
                if(digit_length_after_decimal < digit_after_decimal.length )
                    digit_length_after_decimal = digit_after_decimal.length;
                //alert(digit_length_after_decimal)
            });
            $.each(result.roleMatchingRangeWise, function (index, element) {
                var clique_score = element + '';
                var score_element = two_decimal_digit(clique_score,digit_length_after_decimal)
                //set the row in the left side table
                var menuList = '<tr><td class="roleId menutext"">\n' +
                    '<a href="#" >' +
                    index.split('_RM_')[1] + '</td><td class="menutext"> ' + score_element + ' </td><td class="menutext">' + index.split('_RM_')[0] +
                    /*   '                                    <i class="right fas fa-angle-left"></i>\n' +*/
                    '</a>' +
                    '</td></tr>';
                $('#tableId').append(menuList);

            });
        },
        error: function (e) {
            alert("Error!" + e)
        }
    });
});
let pointing_array = [] // array of mismatch column list
let current_status = 0; // current status of the column which is focused
let offset = 0; // base location of the pointer
//Use a delegated event.  Every click on an "a" element within the ##navigation_bar will trigger this
// This function will load data into the rolematching table and automatically focus the last column of the table.
// It will also set the color of the content of the column
$('#dynamicMenuId').on('click', 'a', function () {
    // Get li containing the link clicked
    var menuName = $(this).text(); //$(this).closest('li').html();
    console.log(menuName.split('(')[0].trim());
    var action = "/tabledatafrorolematchin/" + menuName.split('(')[0].trim();
    //var table_header = '<thead><th>' + 'Role ID' + '</th>\n'
    var table_header = '<thead><th style="background-color: #aeb9c5; color: black">' + 'Role ID' + '</th>\n'
    var table_data = '<tbody>\n'
    var mapcounter = 0;
    header = {
        'X-CSRF-TOKEN': $('#csr-token').val(),
        '${_csrf.parameterName}': $('#csr-token').val()
    };

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: action,
        /* data : searchstring,*/
        dataType: 'json',
        headers: header,
        beforeSend: function () { // Before we send the request, remove the .hidden class from the spinner and default to inline-block.
            $('#loader').removeClass('hidden')
        },
        success: function (result) {
            $('#roleMatchingTblId').empty();
            $('#selectedrolematching').empty();
            //console.log(result);
            $('#selectedrolematching').append('(' + menuName.split('(')[0].trim() + ')')
            pointing_array = []

            $.each(result, function (key, value) {

                var tableDataList = value
                //table_data += '<a href="'+key+'"><tr><td>Wikipedia </td></a>\n'
                var key_split = key.split('____');
                //table_data += '<tr><td><a href="' + key_split[0] + '"target="_blank">' + key_split[1] + '</a></td>\n'
                table_data += '<tr><td><a href="' + key_split[0] + '"target="_blank" style="color: black">' + key_split[1] + '</a></td>\n'


                if (mapcounter == 0) {
                    var pointing_counter = 0;
                    for (var li of tableDataList) {
                        pointing_counter++;
                        if(li.timeStamp.includes("!") ){
                            table_header += '<th class="'+pointing_counter+'" style="color: red" >' + li.timeStamp + '</th>\n'
                            pointing_array.push(pointing_counter)
                        }
                        else
                            table_header += '<th >' + li.timeStamp + '</th>\n'
                    }
                    pointing_counter +=1
                    pointing_array.push(pointing_counter)
                    console.log(pointing_array)
                    table_header += '<th class="'+pointing_counter+'" > Current Date (07-09-2019) </th>\n'
                    table_header += '</thead>'
                    //console.log(table_header)

                    mapcounter = 1;
                }
                var backup_data = ''
                for (var li of tableDataList) {
                    table_data += '<td>' + li.value + '</td>\n'

                    if(li.value !== '_'){
                        //console.log("Inside If")
                        backup_data = li.value
                    }
                }
                table_data += '<td>' + backup_data + '</td>\n'
                table_data += '</tr>\n'
                console.log("finished 1st item")
            });
            $('#roleMatchingTblId').append(table_header)
            $('#roleMatchingTblId').append(table_data)
            // showing border for the first column having different data
            var current_border = pointing_array[pointing_array.length-1]+1//pointing_array[0]+1
            //$('table tr td').attr("style", "border-left: 0px solid #000;border-right: 0px solid #000;");
            if(pointing_array.length>0) {
                $('#roleMatchingTblId > tbody > tr td:nth-child(' + current_border + ')').attr("style", "border-left: 2px solid #800000;border-right: 2px solid #800000;");
                //table data coloring
                var table = document.getElementById("roleMatchingTblId");
                //var row = table.rows[current_border]
                const content_color_map = new Map();
                var colors = ['black', 'green', 'blue', 'DarkRed', 'Chocolate', 'DarkBlue', 'DarkCyan','DarkSlateGrey']; // color for the content of the table
                for (var i = 0, row; row = table.rows[i]; i++) {
                    //iterate through rows
                    //rows would be accessed using the "row" variable assigned in the for loop
                    var col = row.cells[current_border-1]//row.cells[pointing_array[0]]
                    if (i == 1 && (col.innerText != '_' || col.innerText != 'undefined')) {
                        content_color_map.set(col.innerText, colors[0])
                        col.style.color = colors[0]
                        colors.splice(0, 1)

                        //col.css("color", "#F00");
                    }
                    if (i != 1 && !content_color_map.has(col.innerText) && (col.innerText != '_' || col.innerText != 'undefined')) {
                        content_color_map.set(col.innerText, colors[0])
                        col.style.color = colors[0]
                        colors.splice(0, 1)
                    } else if (content_color_map.has(col.innerText) && (col.innerText != '_' || col.innerText != 'undefined')) {
                        col.style.color = content_color_map.get(col.innerText)
                    }

                }
            }
            //table data coloring
            // scroll the horizontal bar to the right
            current_status = pointing_array.length - 1
            offset = document.getElementById('roleMatchingTblId').offsetWidth-1000
            console.log(offset)
            $(".table-responsive").animate(
                {
                    scrollLeft: offset+"px"
                },
                "slow"
            );

        },
        complete: function () { // Set our complete callback, adding the .hidden class and hiding the spinner.
            $('#loader').addClass('hidden')
        },
        error: function (e) {
            alert("No Data Found!")
        }
    });
});
// format the clique at two decimal point
function two_decimal_digit(value,digit_length_after_decimal) {
    if (!value.includes("."))
        value += '.0'
    var non_zero_counter = 0;
    var BreakException = {};
    var before_decimal = value.split('.')[0];
    var after_decimal = value.split('.')[1];
    var backup_digit = '';
    var display_digit = '';
    try {
        after_decimal.split('').forEach((x, i) => {
            //alert(x)
            if (x != '0') {
                //alert('non zero condition')
                if (non_zero_counter == 2) throw BreakException;
                else {
                    //alert('non zero counter not 2')
                    display_digit += x;
                    backup_digit = x;
                    non_zero_counter += 1;
                }
            } else {
                if (non_zero_counter == 2) throw BreakException;
                else {
                    display_digit += x;
                    if (backup_digit != 0) throw BreakException;
                }
            }
        });
    } catch (e) {
        if (e !== BreakException) throw e;
    }
    //alert('out of loop')
    var digit_difference = digit_length_after_decimal - display_digit.split('').length
    for (let i = 0; i < digit_difference; i++) {
        display_digit += '0';
    }
    display_digit = before_decimal + '.' + display_digit
    //console.log(display_digit)
    return display_digit
}
// navigate the horizontal scroll bar to the previous mismatch column
$("#previous_tbl_nav").click(function () {
    var current_pre_data
    if(typeof pointing_array[current_status-1] === 'undefined') {
        //alert("Not Available")
        return
    }
    else {
        current_status -=1
        var current_border = pointing_array[current_status] + 1;
        //var role_matching_table = document.getElementById("roleMatchingTblId");
        $('#roleMatchingTblId > tbody > tr td').attr("style", "border-left: 0px solid #000;border-right: 0px solid #000;");
        $('#roleMatchingTblId > tbody > tr td:nth-child(' + current_border + ')').attr("style", "border-left: 2px solid #800000;border-right: 2px solid #800000;");
        var table = document.getElementById("roleMatchingTblId");
        //var row = table.rows[current_border]
        const content_color_map = new Map();
        var colors = ['red', 'green', 'blue','DarkRed','Chocolate', 'DarkBlue', 'DarkCyan','DarkSlateGrey'];
        for (var i = 0, row; row = table.rows[i]; i++) {
            //iterate through rows
            //rows would be accessed using the "row" variable assigned in the for loop
            var col = row.cells[pointing_array[current_status]]
            if(i == 1 && (col.innerText != '-' || col.innerText != 'undefined')){
                content_color_map.set(col.innerText, colors[0])
                col.style.color = colors[0]
                colors.splice(0,1)

                //col.css("color", "#F00");
            }
            if(i!= 1 && !content_color_map.has(col.innerText) && (col.innerText != '-' || col.innerText != 'undefined')){
                content_color_map.set(col.innerText, colors[0])
                col.style.color = colors[0]
                colors.splice(0,1)
            }else if (content_color_map.has(col.innerText) && (col.innerText != '-' || col.innerText != 'undefined')){
                col.style.color = content_color_map.get(col.innerText)
            }

        }
    }

    offset = offset - 700 + $('.'+pointing_array[current_status]).position().left;
    //offset = offset + $('.'+pointing_array[current_status]).position().left;
    console.log($('.'+pointing_array[current_status]).position().left)
    $(".table-responsive").animate(
        {
            scrollLeft: offset+"px"
        },
        "slow"
    );

});

// navigate the horizontal scroll bar to the next mismatch column
$("#next_tbl_nav").click(function() {
    if(typeof pointing_array[current_status+1] === 'undefined') {
       /*alert("Not Available")*/
       return
    }
    else {
        current_status +=1
        var current_border = pointing_array[current_status] + 1;
        $('#roleMatchingTblId > tbody > tr  td').attr("style", "border-left: 0px solid #000;border-right: 0px solid #000;");
        $('#roleMatchingTblId > tbody > tr td:nth-child(' + current_border + ')').attr("style", "border-left: 2px solid #800000;border-right: 2px solid #800000;");
        var table = document.getElementById("roleMatchingTblId");
        //var row = table.rows[current_border]
        const content_color_map = new Map();
        var colors = ['red', 'green', 'blue','DarkRed','Chocolate', 'DarkBlue', 'DarkCyan','DarkSlateGrey'];
        for (var i = 0, row; row = table.rows[i]; i++) {
            //iterate through rows
            //rows would be accessed using the "row" variable assigned in the for loop
            var col = row.cells[pointing_array[current_status]]
            if(i == 1 && (col.innerText != '-' || col.innerText != 'undefined')){
                content_color_map.set(col.innerText, colors[0])
                col.style.color = colors[0]
                colors.splice(0,1)

                //col.css("color", "#F00");
            }
            if(i!= 1 && !content_color_map.has(col.innerText) && (col.innerText != '-' || col.innerText != 'undefined')){
                content_color_map.set(col.innerText, colors[0])
                col.style.color = colors[0]
                colors.splice(0,1)
            }else if (content_color_map.has(col.innerText) && (col.innerText != '-' || col.innerText != 'undefined')){
                col.style.color = content_color_map.get(col.innerText)
            }

       }
        //console.log(content_color_map)



    }
    if(pointing_array.length-1 == current_status){
        offset =  offset - 800 + $('.'+pointing_array[current_status]).position().left;
    }else{
        offset =  offset - 700 + $('.'+pointing_array[current_status]).position().left;
    }

    console.log($('.'+pointing_array[current_status]).position().left)
    $(".table-responsive").animate(
        {
            scrollLeft: offset+"px"
        },
        "slow"
    );
});
// show the percentage of min role matching input field
$("#per_role_id_match").click(function() {
    $("#role_per_div_id").removeClass('hidden')
});
$("#allroles").click(function() {
    $("#role_per_div_id").addClass('hidden')
});
$("#atleastonerole").click(function() {
    $("#role_per_div_id").addClass('hidden')
});
// show the note about the content in the content search
$( ".myDIV" ).hover(
    function() {
        $("#info-id").removeClass("hidden");
    }, function() {
        $("#info-id").addClass("hidden");
    }
);
// show the note about the content in the content search
$( "#content-icon-id" ).hover(
    function() {
        $("#info-id-content").removeClass("hidden");
    }, function() {
        $("#info-id-content").addClass("hidden");
    }
);
// select table data and show in the portion
/*var role_matching_table_id = document.getElementById("roleMatchingTblId");

role_matching_table_id.addEventListener("click", function(e) {
    if (e.target && e.target.nodeName == "TD") {
        var currentRow=$(this).closest("tr");
        var col1=currentRow.find("td:eq(0)").text();
        console.log($(e.target).closest("td").html())
        //console.log($(e.target).find("td:eq(0)").text())
        //console.log($(this).innerHTML())
        //alert(col1)
        var first_td = $(e.target).closest("tr") // Finds the closest <tr>
            .find("td:first").eq(0) // Finds the first <td>
            .text();
        let $cell = $(e.target).closest("td");
        let txt = $cell.text();
        let label = $cell.closest('table').find('td').eq(4).text() //$cell.closest('tr').find('.label').text();
        let header = $cell.closest('table').find('th').eq($cell.index()).text();
        $("#date_id").val(header)
        $("#role_id_display").val(first_td)
        $("#current_value_id").val(txt)
        console.log(txt + ':' + first_td + ':' + header);
    }
});*/


