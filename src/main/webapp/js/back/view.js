$(
    function () {
        var messages = {
            uri:"/admin/messages",
            pages:[],
            key:'',
            messages:[],
            message:{id:0,uid:0,createDate:null,text:'',reply:'',replyDate:null,status:0,type:'',sid:0}
        };
        var messageVue = new Vue(
            {
                el:".container",
                data:messages,
                mounted:function () {
                    this.list(0);
                },
                filters:{
                    statusFilter:function (value) {
                        if(value==0)
                            return '已读';
                        if(value==1)
                            return '未读';
                    },
                    typeFilter:function (value) {
                        if(value=='type_complaint')
                            return '投诉';
                        if(value=='type_suggestion')
                            return '建议';
                    }
                },
                methods:{
                    list: function (start) {
                        var url = getPath() + "/admin/views" + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    messageVue.pages = value.data;
                                    messageVue.messages = value.data.content;
                                    $(".back_message_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else
                                {
                                    $(".back_message_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();
                                }
                                Vue.nextTick(
                                    function () {
                                        checkListener();
                                    }
                                );
                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,messageVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,messageVue);
                    },
                    checkButton:function (id) {
                        var url = getPath() + messageVue.uri+"/check/" + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '标记已读成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    messageVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    unCheckButton:function (id) {
                        var url = getPath() + messageVue.uri+"/unCheck/" + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '标记未读成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    messageVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    replyButton:function (id) {
                        var url = getPath() + messageVue.uri+"/" + id;
                        axios.get(url).then(function (value) {
                            messageVue.message = value.data;
                            if(messageVue.message.reply ==null)
                                messageVue.message.reply='';
                            $("#messageReplyModel").modal("show");
                        });

                    },
                    updateButton:function(id)
                    {
                        var url = getPath() + messageVue.uri+"/" + id;
                        axios.put(url,messageVue.message).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '回复成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                messageVue.list(0);
                                            }
                                        }
                                    }
                                }
                            );
                        });
                        $("#messageReplyModel").modal("hide");
                    },
                    detailButton:function(id){
                        var url = getPath() + messageVue.uri+"/" + id;
                        axios.get(url).then(function (value) {
                            messageVue.message = value.data;
                            if(messageVue.message.reply ==null)
                                messageVue.message.reply='';
                            $("#messageInfoModel").modal("show");
                        });

                    },
                    deleteButton:function (id) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除反馈',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    var url = getPath()+messageVue.uri+"/"+id;
                                    axios.delete(url).then(function(value){
                                        if(0!=value.data.length){
                                            $.alert('系统异常，请重试!');                                          }
                                        else{
                                            $.alert('成功删除!');
                                            messageVue.list(0);
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
                    deleteAllButton:function () {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除反馈',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    $("input[name='messageCheckbox']:checked").each(
                                        function () {
                                            var input = $(this);
                                            var url = getPath()+messageVue.uri+"/"+input.val();
                                            axios.delete(url).then(function(value){
                                                if(0!=value.data.length){
                                                    $.alert('系统异常，请重试!');
                                                }
                                                else{
                                                    $.alert('成功删除!');
                                                    input.prop("checked",false);
                                                    messageVue.list(0);
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
                    search:function () {
                        if(!checkEmpty(this.key,'关键词'))
                        {
                            return;
                        }
                        if(this.key.length>=10)
                        {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var url = getPath() +"/admin/views/search?key=" + this.key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    messageVue.messages = value.data;
                                    $(".back_message_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".notfound_search").hide();
                                }
                                else {
                                    $(".back_message_list_table").hide();
                                    $(".notfound_search").show();
                                    $(".notfound_list").hide();
                                }
                            }
                        );
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