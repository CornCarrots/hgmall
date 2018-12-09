$(
    function () {
        var scores={
            uri:"/admin/scores",
            pages:[],
            key:'',
            scores:[],
            addMember:{id:0,name:'',min:0,max:0,score:0,icon:''},
            editMember:{id:0,name:'',min:0,max:0,score:0,icon:''}
        };
        var scoreVue=new Vue(
            {
                el:".container",
                data:scores,
                mounted:function () {
                    this.list(0);
                },
                methods: {
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if (value.data.content.length > 0) {
                                    scoreVue.pages = value.data;
                                    scoreVue.scores = value.data.content;
                                    $(".back_user_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_user_list_table").hide();
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
                    jump: function (page) {
                        jump(page, scoreVue);
                    },
                    jumpByNumber: function (start) {
                        jumpByNumber(start, scoreVue);
                    },
                    addButton:function()
                    {
                        $("#addScoreModel").modal("show");
                    },
                    deleteAllButton:function(){
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除积分信息',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='scoreCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+scoreVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');                                                  }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    scoreVue.list(0);
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
                        var url = getPath()+"/admin/scores/search?key="+this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    scoreVue.scores = value.data;
                                    $(".back_user_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_user_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
                    },
                    addMember_:function () {
                        if(!$("#addForm").validationEngine("validate")) return false;

                        var uri = getPath() + this.uri;
                        var bean = this.addMember;
                        axios.post(uri,bean).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '添加积分信息成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    scoreVue.list(0);
                                                    scoreVue.addMember={id:0,name:'',min:0,max:0,score:0,icon:''};
                                                    $("#addScoreModel").modal("hide");
                                                }
                                            }
                                        }
                                    }
                                );

                            }
                        );
                    },
                    deleteMember_:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除积分信息',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+scoreVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');
                                        }
                                        else{
                                            $.alert('成功删除!');
                                            scoreVue.list(0);
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
                    editMember_:function (id) {
                        var url = getPath()+this.uri+"/"+id;
                        axios.get(url).then(function(value){
                            scoreVue.editMember=value.data;
                        });
                        $("#editScoreModel").modal("show");
                    },
                    updateMember_:function (id) {
                        if(!$("#updateForm").validationEngine("validate")) return false;

                        var url = getPath()+this.uri+"/"+id;
                        axios.put(url,this.editMember).then(function(value){
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改积分信息成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                scoreVue.editMember={id:0,name:'',min:0,max:0,score:0,icon:''};
                                                scoreVue.list(0);
                                                $("#editScoreModel").modal("hide");
                                            }
                                        }
                                    }
                                }
                            );

                        });
                    },
                    power:function (id) {
                        location.href=getPath()+"/admin/user/power?mid="+id;
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