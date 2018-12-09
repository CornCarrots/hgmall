$(function () {
    var home = {
        users_num: 0,
        stores_num: 0,
        orders_num: 0,
        orders_sum: 0,
        orders_pay: 0,
        orders_delivery: 0,
        orders_confirm: 0,
        orders_success: 0,
        orders_fail: 0,
        products_success: 0,
        products_fail: 0,
        products_all: 0,
        category_num: 0,
        review_num: 0,
        messages: []
    };
    var homeVue = new Vue(
        {
            el: ".container",
            data: home,
            mounted: function () {
                this.get();
            },
            methods: {
                get: function () {
                    var url = getPath() + "/admin/home";
                    axios.get(url).then(function (value) {
                        homeVue.users_num = value.data.users_num;
                        homeVue.stores_num = value.data.stores_num;
                        homeVue.orders_num = value.data.orders_num;
                        homeVue.orders_sum = value.data.orders_sum;
                        homeVue.orders_pay = value.data.orders_pay;
                        homeVue.orders_delivery = value.data.orders_delivery;
                        homeVue.orders_confirm = value.data.orders_confirm;
                        homeVue.orders_success = value.data.orders_success;
                        homeVue.orders_fail = value.data.orders_fail;
                        homeVue.products_success = value.data.products_success;
                        homeVue.products_fail = value.data.products_fail;
                        homeVue.products_all = value.data.products_all;
                        homeVue.category_num = value.data.category_num;
                        homeVue.review_num = value.data.review_num;
                        homeVue.messages = value.data.messages;
                    });
                }
            }
        }
    );
});