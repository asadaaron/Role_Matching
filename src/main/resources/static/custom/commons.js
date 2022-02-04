var Server = {
        save:function (headerValue,data,url,formId,caption,gridId)
        {
      jQuery.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: url,
        async: true,
        data : JSON.stringify(data),
        //dataType: "json",
        headers:headerValue,
        success: function (result) {
            //$('#'+formId).find('#'+gridId).trigger('reloadGrid');
            //$('#'+gridId).trigger('reloadGrid');
            Server.getMessage(1,result,caption);
            Server.resetForm(formId);
        },
        error: function (result) {
           // document.getElementById("#"+formId).reset();
            //console.log("response in error");
            Server.getMessage(2, result, caption);
           // $('#'+gridId).trigger('reloadGrid');
            Server.resetForm(formId);

        }
    });
},
    delete: function(header,url,data,formId, content, caption,gridId){
        //console.log(formId+" "+gridId)
        $.confirm({
            title: 'Confirm!',
            content: content,
            buttons: {
                confirm: function () {
                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : url,
                        data : JSON.stringify(data),
                        dataType : 'json',
                        headers:header,
                        success : function(result) {
                            Server.getMessage(1,result,caption);
                            $('#'+gridId).trigger( 'reloadGrid' );
                            Server.resetForm(formId);
                        },
                        error : function(result) {

                            Server.getMessage(2,result,caption);
                            $('#'+formId).find('#'+gridId).trigger( 'reloadGrid' );
                            Server.resetForm(formId);
                        }
                    });

                },
                cancel: function () {
                }
            }
        });
    },
    getMessage: function(type,msg,header){
        var color = '';
        if(type==1) {
            var color = 'bg-green';
        }else if(type==2){
            var color = 'bg-red';
        }else if(type==3){
            var color = 'bg-azure';
        }
        $.jGrowl(""+ msg +"", {
            header: header,
            sticky: false,
            position: 'bottom-right',
            theme: color
        });

    },
    resetForm:function(formIdReset){
        $('#'+formIdReset).trigger("reset");
        $('#id').val('');
        $('#deleteButton').hide();
        $('#jqGrid').trigger( 'reloadGrid' );
        $('#updateButton').hide();
        $('#saveButton').show();

    },
    validator:function(formId){
        return $('#'+formId).validate();

    },
    edit:function(header,url,id,callback){
        console.log(id);
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : url+id,
            data : id,
            dataType : 'json',
            headers:header,
            success : function(result) {
                callback(result);
                $('#deleteButton').show();
                //$('#btnsave').val("Update");
            },
            error : function(e) {
                alert("Error!" +e)
            }
        });

    },

    list:function(header,url,colModel,formId,caption,urlmethod,gridId){

        //Start JqGrid
        $('#'+gridId).jqGrid({
        //$("#jqGrid").jqGrid({
            url: url,
            mtype: urlmethod,
            styleUI : 'Bootstrap',
            postData: header,
            contentType : "application/json",
            datatype: "json",
            colModel:colModel ,
            pager: "#jqGridPager",
            rowNum:10,
            rownumbers: true,
            rowList:[10,20,30,50,100,500],
            viewrecords: true,
            sortorder: "asc",
            caption: caption,
            //autoWidth: true,
            width:1000,
            height:200,

            altRows:true,
            shrinkToFit: true,
            scrollOffset: 0,
            onSelectRow: function() {
                var myGrid = $('#'+gridId),
                    selectedRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
                    cellValue = myGrid.jqGrid ('getCell', selectedRowId, 'id');
                //console.log(selectedRowId)
                edit(cellValue);
            }
        });
        $('#'+formId).find('#'+gridId).jqGrid("setLabel", "rn", "SL.");
        $(window).bind('resize', function() {
            // resize the datagrid to fit the page properly:
            $('div.grid-resize').each(function() {
                $(this).width('auto');
                $(this).find('table').width('100%');
            });
        });

    }

}