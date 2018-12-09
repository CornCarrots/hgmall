$(
    function () {
        var modules = {
            uri:"/admin/system/modules",
            pages:[],
            modules:[],
            module:{},
            key:'',
            allModules:[]
        };
        var moduleVue = new Vue(
            {
                el:".container",
                data:modules,
                mounted:function () {


                    this.list(0);
                },
                methods:{
                    list: function (start) {
                        $("#addSelect").fancySelect();
                        $("#editSelect").fancySelect();
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                moduleVue.allModules = value.data.modules;
                                if(value.data.page.content.length>0)
                                {
                                    moduleVue.pages = value.data.page;
                                    moduleVue.modules = value.data.page.content;
                                    $(".back_module_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                }
                                else
                                {
                                    $(".back_module_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                $("#addSelect").html('');
                                Vue.nextTick(function () {
                                    var html = '<option value=\"0\" selected>无</option>';
                                    $(moduleVue.allModules).each(
                                        function (i, data) {
                                            html+='<option value=\"'+data.id+'\">'+data.desc+'</option>';
                                        }
                                    );
                                    $("#addSelect").html(html);
                                    $("#addSelect").trigger('update.fs');
                                    $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

                                    checkListener();
                                });
                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,moduleVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,moduleVue);
                    },
                    addButton:function()
                    {
                        moduleVue.module={};

                        $("#addModuleModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除模块',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    $("input[name='moduleCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+moduleVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    moduleVue.list(0);
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
                        var url = getPath()+"/admin/system/modules/search?key="+moduleVue.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    moduleVue.modules = value.data;
                                    $(".back_module_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_module_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
                    },
                    addModule_:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        var uri = getPath() + this.uri;
                        var bean = this.module;
                        axios.post(uri,bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    moduleVue.module={};
                                                    moduleVue.list(0);
                                                    $("#addModuleModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteModule_:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除模块',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {

                                    var url = getPath()+moduleVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');
                                        }
                                        else{
                                            $.alert('成功删除!');
                                            moduleVue.list(0);
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
                    editModule_:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function(value){
                            moduleVue.module=value.data;
                            $("#editSelect").html('');
                            Vue.nextTick(function () {
                                var html = '<option value=\"0\">无</option>';
                                $(moduleVue.allModules).each(
                                    function (i, data) {
                                        if(data.id!=moduleVue.module.id)
                                        {
                                            if(data.id==moduleVue.module.pid)
                                                html+='<option value=\"'+data.id+'\" selected>'+data.desc+'</option>';
                                            else
                                                html+='<option value=\"'+data.id+'\">'+data.desc+'</option>';
                                        }

                                    }
                                );
                                $("#editSelect").html(html);
                                $("#editSelect").trigger('update.fs');
                            });
                        });
                        $("#editModuleModel").modal("show");
                    },
                    updateModule_:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.module).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改模块成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                moduleVue.module={};
                                                moduleVue.list(0);
                                                $("#editModuleModel").modal("hide");
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