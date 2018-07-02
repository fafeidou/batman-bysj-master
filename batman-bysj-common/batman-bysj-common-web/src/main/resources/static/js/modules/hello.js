var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            userName: null
        },
        showList: true,
        title: null,
        user: {}
    },
    created: function () {
        $(function () {
            $("#jqGrid").jqGrid({
                url: baseURL + '/app/user/getUserPage',
                datatype: "json",
                mtype: 'POST',
                ajaxGridOptions: {
                    contentType: "application/json",
                },
                colModel: [
                    { label: 'id', name: 'id', width: 60, key: true ,sortable: true,sortname:'id',sortorder: 'asc'},
                    { label: '用户名', name: 'userName', width: 60 },
                    { label: '地址', name: 'address', width: 100 }
                ],
                viewrecords: true,
                height: 385,
                rowNum: 10,
                rowList : [10,30,50],
                rownumbers: true,
                rownumWidth: 25,
                autowidth:true,
                multiselect: true,
                pager: "#jqGridPager",
                jsonReader : {
                    root:  function (obj) {
                        return obj.data.record;
                    },
                    page: function (obj) {
                        return obj.data.record;
                    },
                    total: function (obj) {
                        return obj.data.totalPage;
                    },
                    records: function (obj) {
                        return obj.data.total;
                    }
                },
                serializeGridData: function (postData)
                {
                    var data = {};
                    data.userName = vm.q.userName;
                    data.pageSize = $('#jqGrid').getGridParam('rowNum');
                    data.pageNum = $('#jqGrid').getGridParam('page');
                    data.sortName = $('#jqGrid').getGridParam('sortname');
                    data.sortOrder = $('#jqGrid').getGridParam('sortorder');
                    data.orders = [{"column":"id","direction":"desc"}];
                    return JSON.stringify(data);
                },
                gridComplete:function(){
                    //隐藏grid底部滚动条
                    $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                }
            });
        });
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.user = {};
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.showList = false;
            $.get(baseURL + "app/user/info/?id="+id, function(r){
                vm.title = "修改";
                vm.user = r.data;
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.user.id == null ? "/app/user/save" : "/app/user/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(index){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "app/user/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        pause: function (event) {
            var jobIds = getSelectedRows();
            if(jobIds == null){
                return ;
            }

            confirm('确定要暂停选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/schedule/pause",
                    contentType: "application/json",
                    data: JSON.stringify(jobIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        resume: function (event) {
            var jobIds = getSelectedRows();
            if(jobIds == null){
                return ;
            }

            confirm('确定要恢复选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/schedule/resume",
                    contentType: "application/json",
                    data: JSON.stringify(jobIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        runOnce: function (event) {
            var jobIds = getSelectedRows();
            if(jobIds == null){
                return ;
            }

            confirm('确定要立即执行选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "sys/schedule/run",
                    contentType: "application/json",
                    data: JSON.stringify(jobIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'userName': vm.q.userName},
            }).trigger("reloadGrid");
        }
    }
});