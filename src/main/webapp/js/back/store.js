$(function () {
    var stores = {
        uri: "/admin/stores",
        pages: {},
        stores: [],
        key: '',
        store: {
            id: 0, name: '', cid: 0, type: '', applyDate: '', addDate: '',
            applyName: '', mobile: '', telephone: '', identification: '',
            email: '', authentication_credit: 0, authentication_id: 0, authentication_phone: 0, summary_html: '', status: 0,
            category: {id: 0, name: '', sid: 0, type: ''}
        }
    };
    var storeVue = new Vue(
        {
            el: ".container",
            data: stores,
            mounted: function () {
                this.list(0);
            },
            filters: {
                statusFilter: function (status) {
                    if (status == 0)
                        return '通过';
                    if (status == 1)
                        return '未通过';
                    if (status == 2)
                        return '待审核';
                },
                typeFilter: function (type) {
                    if (type == 'type_private')
                        return '个人店铺';
                    if (type == 'type_business')
                        return '商业店铺'
                }
            },
            methods: {
                list: function (start) {
                    var url = getPath() + this.uri + "?start=" + start;
                    axios.get(url).then(
                        function (value) {
                            storeVue.pages = value.data;
                            storeVue.stores = value.data.content;
                            Vue.nextTick(
                                function () {
                                    checkListener();
                                }
                            );
                        }
                    )
                },
                jump: function (page) {
                    jump(page, storeVue);
                },
                jumpByNumber: function (start) {
                    jumpByNumber(start, storeVue);
                },
                getStore: function (id) {
                    var url = getPath() + this.uri + "/" + id;
                    axios.get(url).then(function (value) {
                        console.log(value.data);
                        storeVue.store = value.data;
                    });
                    $("#storeInfoModel").modal("show");
                },
                getImage: function (id) {
                    if (id == 0||storeVue.store.categories.length<=0)
                        return;
                     id = storeVue.store.categories[0].id;
                    var url = getPath() + "/image/store/" + id + ".jpg";
                    return url;
                },
                editStore: function (status, id) {

                    if (status == 3) {
                        $.confirm({
                            title: '确定吗？',
                            content: '您正在删除店铺',
                            theme:'modern',
                            icon: 'fa fa-question',
                            buttons: {
                                '确认': function () {
                                    this.store.status = status;
                                    var url = getPath() + storeVue.uri + "/" + id;
                                    axios.put(url, this.store).then(function (value) {
                                        $.alert('成功删除!');
                                        storeVue.store.status = '';
                                        storeVue.list(0);
                                    })
                                },
                                '取消': {
                                    action: function () {
                                        $.alert('已取消!');
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        this.store.status = status;
                        var url = getPath() + this.uri + "/" + id;
                        axios.put(url, this.store).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改店铺成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    confirm: function(){
                                        storeVue.store.status = '';
                                        storeVue.list(0);
                                    }
                                }
                            );

                        })
                    }

                },
                deleteAllButton: function () {
                    $.confirm({
                        title: '确定吗？',
                        content: '您正在删除店铺',
                        theme:'modern',
                        icon: 'fa fa-question',
                        buttons: {
                            '确认': function () {
                                $("input[name='storeCheckBox']:checked").each(
                                    function () {
                                        var input = $(this);
                                        storeVue.store.status = 3;
                                        var url = getPath() + storeVue.uri + "/" + input.val();
                                        axios.put(url, storeVue.store).then(function (value) {
                                            $.alert('成功删除!');
                                            storeVue.store.status = '';
                                            input.prop("checked", false);
                                            storeVue.list(0);
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
                search: function () {
                    if (!checkEmpty(this.key, '关键词')) {
                        return;
                    }
                    if (this.key.length >= 10) {
                        alert("关键词长度不能大于十，请重新搜索")
                        return;
                    }
                    var url = getPath() + "/admin/store/search?key=" + this.key;
                    location.href = url;
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
});