$(
    function () {
        var myMap = null;
        var orders = {
            maps:[]
        };
        var saleMapVue = new Vue(
            {
                el:".container",
                data:orders,
                mounted:function () {
                    myMap = echarts.init(document.getElementById('mapDIV'));
                    myMap.showLoading();
                    this.get();
                },
                methods:{
                    get:function () {
                        axios.get(getPath()+"/admin/sales/map").then(
                            function (value) {
                                saleMapVue.maps = value.data;
                                Vue.nextTick(function () {
                                    saleMapVue.map();
                                });
                            }
                        );
                    },
                    map:function() {
                        var optionMap = {
                            title: {
                                text: '订单量',
                                subtext: '中国各省份订单量',
                                x:'center',
                                y: 'top',
                                textAlign: 'center',
                            },
                            backgroundColor: '#ebebeb',
                            borderColor: '#66FF00',
                            borderWidth: 1,
                            // 标题内边距，单位px，默认各方向内边距为5，接受数组分别设定上右下左边距，同css，见下图
                            padding: [20,40,20,40],
                            itemGap: 20,
                            // 主标题文本样式

                            tooltip : {
                                trigger: 'item'
                            },

                            //左侧小导航图标
                            visualMap: {
                                show : true,
                                x: 'left',
                                y: 'center',
                                splitList: [
                                    {start: 15},
                                    {start: 10, end: 15},
                                    {start: 5, end: 10},
                                    {start: 3, end: 5},
                                    {start: 1, end: 3},
                                    {end: 1}
                                ],
                                color: ['#e0022b', '#e0311f', '#e06113','#e09107', '#cbab08', '#a3e00b']
                            },

                            //配置属性
                            series: [{
                                name: '订单量',
                                type: 'map',
                                mapType: 'china',
                                zoom: 1.3,
                                itemStyle: {
                                    //normal 是图形在默认状态下的样式；
                                    normal: {
                                        show: true,
                                        areaColor:"#CECECE",
                                        borderColor:"#FCFCFC",
                                        borderWidth:"1"
                                    },
                                    //emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                                    emphasis: {
                                        show: true,
                                        areaColor:"#C8A5DF"
                                    }
                                },
                                label: {
                                    normal: {
                                        show: true  //省份名称
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                data:saleMapVue.maps //数据
                            }]
                        };
//初始化echarts实例
//使用制定的配置项和数据显示图表
                        myMap.setOption(optionMap);
                        myMap.hideLoading();
                    }
                }
            }
        );
    }
);