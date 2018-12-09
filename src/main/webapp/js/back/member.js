$(
    function () {
        var members = {
            uri:"/admin/members",
            pages:[],
            key:'',
            members:[]
        };
        var memberVue = new Vue(
            {
                el:".container",
                data:members,
                mounted:function () {
                    this.list(0);
                },
                filters:{
                    statusFilter:function (value) {
                        if(value==0)
                            return '已审核';
                        if(value==1)
                            return '已屏蔽';
                    }
                },
                methods:{
                    list: function (start) {
                        var url = getPath() + this.uri + "?start=" + start;
                        axios.get(url).then(
                            function (value) {
                                if(value.data.content.length>0)
                                {
                                    memberVue.pages = value.data;
                                    memberVue.members = value.data.content;
                                    $(".back_user_list_table").show();
                                    $(".notfound_list").hide();
                                    $(".pageDiv").show();
                                    $(".notfound_search").hide();

                                }
                                else
                                {
                                    $(".back_user_list_table").hide();
                                    $(".notfound_list").show();
                                    $(".pageDiv").hide();
                                    $(".notfound_search").hide();

                                }

                            }
                        )
                    },
                    jump:function (page) {
                        jump(page,memberVue);
                    },
                    jumpByNumber:function (start) {
                        jumpByNumber(start,memberVue);
                    },
                    checkUser:function (id) {
                        var url = getPath() + "/admin/users/check/" + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '通过成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    memberVue.list(0);
                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    shieldUser:function (id) {
                        var url = getPath() + "/admin/users/shield/"  + id;
                        axios.put(url).then(
                            function (value) {
                                $.alert(
                                    {
                                        title: '恭喜你!',
                                        content: '屏蔽成功',
                                        theme:'modern',
                                        icon: 'fa fa-smile-o',
                                        buttons: {
                                            ok: {
                                                action: function () {
                                                    memberVue.list(0);

                                                }
                                            }
                                        }
                                    }
                                );
                            }
                        );
                    },
                    search:function () {
                        if (!checkEmpty(this.key, '关键词')) {
                            return;
                        }
                        if (this.key.length >= 10) {
                            alert("关键词长度不能大于十，请重新搜索")
                            return;
                        }
                        var key = this.key;
                        var url = getPath()+"/admin/members/search?key="+key;
                        axios.post(url).then(
                            function (value) {
                                $(".pageDiv").hide();
                                if (value.data.length > 0) {
                                    memberVue.members = value.data;
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
                    }
                }
            }
        );
    }
);