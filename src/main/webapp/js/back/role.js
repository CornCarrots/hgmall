$(
    function () {
        var roles={
            uri:"/admin/roles",
            pages:[],
            key:'',
            roles:[],
            role:{}
        };
        var roleVue=new Vue(
            {
                el:".container",
                data:roles,
                mounted:function () {
                    this.list(0);
                },
                methods: {
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if (value.data.content.length > 0) {
                                    roleVue.pages = value.data;
                                    roleVue.roles = value.data.content;
                                    $(".back_role_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_role_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(function () {
                                    $("#addForm").validationEngine({promptPosition:'centerRight', showArrow:true});
                                    $("#updateForm").validationEngine({promptPosition:'centerRight', showArrow:true});

                                    checkListener();
                                });
                            }
                        )
                    },
                    jump: function (page) {
                        jump(page, roleVue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, roleVue);
                    },
                    addButton:function()
                    {
                        $("#addRoleModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除角色',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='roleCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+roleVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    roleVue.list(0);
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
                        var url = getPath()+"/admin/roles/search?key="+this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    roleVue.roles = value.data;
                                    $(".back_role_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_role_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
                    },
                    addRole:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        var uri = getPath() + this.uri;
                        var bean = this.role;
                        axios.post(uri,bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加角色成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    roleVue.list(0);
                                                    roleVue.role={};
                                                    $("#addRoleModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteRole:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除角色',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+roleVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');                                          }
                                        else{
                                            $.alert('成功删除!');
                                            roleVue.list(0);
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
                    editRole:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function(value){
                            roleVue.role=value.data;
                        });
                        $("#editRoleModel").modal("show");
                    },
                    updateRole:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.role).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改角色成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                roleVue.role={};
                                                roleVue.list(0);
                                                $("#editRoleModel").modal("hide");
                                            }
                                        }
                                    }
                                }
                            );

                        });

                    },
                    permission:function (id) {
                        location.href=getPath()+"/admin/manager/permission?rid="+id;
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