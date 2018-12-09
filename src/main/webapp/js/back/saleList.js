$(
    function () {
        var myChart = null;
        var orders = {
            order:{},
            orders_pay:[],
            orders_delivery:[],
            orders_confirm:[],
            orders:[]
        };
        var saleListVue = new Vue(
            {
                el:".container",
                data:orders,
                mounted:function () {
                    myChart = echarts.init(document.getElementById('chartDIV'));
                    myChart.showLoading();
                    this.get();
                },
                methods:{
                    get:function () {

                        axios.get(getPath()+"/admin/sales/info").then(
                            function (value) {
                                saleListVue.order = value.data.order;
                                saleListVue.orders_pay = value.data.orders_pay;
                                saleListVue.orders_delivery = value.data.orders_delivery;
                                saleListVue.orders_confirm = value.data.orders_confirm;
                                saleListVue.orders = value.data.orders;

                                Vue.nextTick(function () {
                                    saleListVue.chart();
                                })

                            }
                        );
                    },
                    chart:function () {
// 指定图表的配置项和数据
                        var option = {
                            title: {
                                text: '月购买订单交易记录'
                            },
                            tooltip: {},
                            legend: {
                                orient: 'vertical',//垂直布局
                                x: 'right', //水平方向居左显示
                                y: 'center', //垂直方向居中显示
                                data: ['总订单','待付款', '待发货', '待收货'],
                            },
                            grid: {
                                left: '1%',//距离div左边的距离
                                right: '14%',//距离div右边的距离
                                bottom: '3%',//距离下面
                                containLabel: true
                            },
                            xAxis: {
                                data: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
                            },
                            yAxis: {
                                // data:[2000,400,600,800,1000,1200]
                            },
                            series: [
                                {
                                    name: '总订单',
                                    type: 'bar',
                                    data: saleListVue.orders,
                                    // data: [5, 20, 36, 10, 10, 20, 5, 20, 36, 10, 10, 20],
                                    color:'#2ec7c9'
                                },
                                {
                                    name: '待付款',
                                    type: 'bar',
                                    data: saleListVue.orders_pay,
                                    color:'#b6a2de'
                                },
                                {
                                    name: '待发货',
                                    type: 'bar',
                                    data: saleListVue.orders_delivery,
                                    color: "#5ab1ef"
                                },
                                {
                                    name: '待收货',
                                    type: 'bar',
                                    data: saleListVue.orders_confirm,
                                    color:"#ffb980"
                                }
                            ]
                        };

// 使用刚指定的配置项和数据显示图表。
                        myChart.setOption(option);
                        myChart.hideLoading();
                    }
                }
            }
        );

    }
);