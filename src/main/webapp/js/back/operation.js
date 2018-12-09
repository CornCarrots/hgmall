$(
    function () {
        var operations = {
            uri:"/admin/system/operations",
            pages:[],
            operations:[],
            operation:{},
            key:''
        };
        var operationVue = new Vue(
            {
                el:".container",
                data:operations,
                mounted:function () {
                    this.list(0);
                },
                methods:{
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    operationVue.pages = value.data;
                                    operationVue.operations = value.data.content;
                                    $(".back_operation_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else
                                {
                                    $(".back_operation_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(
                                    function () {
                                        $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                        $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

                                        checkListener();
                                    }
                                );
                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,operationVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,operationVue);
                    },
                    addButton:function()
                    {
                        operationVue.operation={};
                        $("#addOperationModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除操作权限',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='moduleCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+operationVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    operationVue.list(0);
                                                }
                                            });
                                        }
                                    );
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });

                    },
                    search:function()
                    {
                        if(!checkEmpty(this.key,'关键词'))
                        {
                            return;
                        }
                        if(this.key.length>=10)
                        {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var url = getPath()+"/admin/system/operations/search?key="+operationVue.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    operationVue.operations = value.data;
                                    $(".back_operation_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_operation_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
                    },
                    addOperation_:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        var uri = getPath() + this.uri;
                        var bean = this.operation;
                        axios.post(uri,bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加操作权限成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    operationVue.operation={};
                                                    operationVue.list(0);
                                                    $("#addOperationModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteOperation_:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除操作权限',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    var url = getPath()+operationVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');
                                        }
                                        else{
                                            $.alert('成功删除!');
                                            operationVue.list(0);
                                        }
                                    });
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });
                    },
                    editOperation_:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function(value){
                            operationVue.operation=value.data;
                        });
                        $("#editOperationModel").modal("show");
                    },
                    updateOperation_:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.operation).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改操作权限成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                operationVue.operation={};
                                                operationVue.list(0);
                                                $("#editOperationModel").modal("hide");
                                            }
                                        }
                                    }
                                }
                            );

                        });

                    }
                }
            }
        );
        function checkListener(){
            $("#checkAllTH input").click(
                function () {
                    checkAll();
                }
            );
            $(".checkOne input").click(
                function () {
                    checkOne();
                }
            );
        }
    }
);