$(function () {
    var bean = {
        uri: "/foreMember",
        carousels: [],
        powers: [],
        normal:[],
        power:{},
        width:0
    };
    var memberVue = new Vue(
        {
            el: ".main",
            data: bean,
            mounted: function () {
                this.get();
            },
            methods: {
                get: function () {
                    var url = getPath() + this.uri+"?timeStamp="+new Date().getTime();
                    axios.get(url).then(
                        function (value) {
                            memberVue.carousels = value.data.carousels;
                            memberVue.normal = value.data.normal;
                            var arr = new Array();
                            $(value.data.powers).each(function (i, data) {
                                arr.push(data);
                                if ((i + 1) % 4 == 0 || i == value.data.powers.length - 1) {
                                    memberVue.powers.push(arr);
                                    arr = new Array();
                                }
                            });
                        }
                    );
                },
                getCarousel: function (id) {
                    var url = getPath() + "/image/carousel_member/" + id + ".jpg";
                    return url;
                },
                getPower: function (id) {
                    var param = window.btoa("pid=" + id+"&timeStamp="+new Date().getTime());
                    var url = getPath() + "/power?"+param;
                    return url;
                },
                toPower: function (id) {
                    location.href = this.getPower(id);
                },
                myPower: function (i) {
                    memberVue.power=memberVue.normal[i];
                    console.log(memberVue.power)
                    $("#getPowerModel").modal("show");
                },
                upgrade:function () {
                    $.dialog(
                        {
                            title:false,
                            content:'   <div align="center">\n' +
                                '             <h5>新用户首次开通赠送30积分</h5>\n' +
                                '            <h5>用户每次充值，赠送2积分</h5>\n' +
                                '            <h5>用户每次下单，赠送1积分，多买多送</h5>\n' +
                                '        </div>',
                            theme:'material'
                        }
                    );
                }
            }
        }
    );
});