$(
    function () {
        var stores = {
            uri: "/admin/stores/info",
            store: {
                id: 0,
                name: '',
                cid: 0,
                type: '',
                applyDate: '',
                addDate: '',
                applyName: '',
                mobile: '',
                telephone: '',
                identification: '',
                email: '',
                authentication_credit: 0,
                authentication_id: 0,
                authentication_phone: 0,
                sumary: '',
                status: 0,
                category: {id: 0, name: '', sid: 0, type: ''}
            },
            categories: [],

        };
        var storeVue = new Vue(
            {
                el: ".container",
                data: stores,
                mounted: function () {
                    // $("#select").empty();
                    this.get();

                },
                methods: {
                    get: function () {
                        var select = $("#select");
                        var url = getPath() + this.uri;
                        axios.get(url).then(function (value) {
                            storeVue.store = value.data;
                            url = getPath() + "/admin/categories/parent";
                            axios.get(url).then(function (value) {
                                storeVue.categories = value.data;
                                Vue.nextTick(function () {
                                    select.selectpicker('refresh');
                                    select.selectpicker('render');
                                });
                            });
                        });
                    },
                    updateStore: function () {
                        var select = $("#select").val();
                        storeVue.store.cid = select;
                        var url = getPath() + this.uri;
                        axios.put(url, storeVue.store).then(function (value) {
                            $.alert(
                                {
                                    title: '恭喜你!',
                                    content: '修改店铺信息成功',
                                    theme:'modern',
                                    icon: 'fa fa-smile-o',
                                    buttons: {
                                        ok: {
                                            action: function () {
                                                location.reload();
                                            }
                                        }
                                    }
                                }
                            );
                        })
                    },
                    reset: function () {
                        storeVue.get();
                    }
                }
            }
        );
    }
);