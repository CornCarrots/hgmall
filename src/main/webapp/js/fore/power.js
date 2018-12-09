$(function () {
    var bean = {
        uri:"/forePower",
        power:{},
        member:{}
    };
    var powerVue = new Vue({
        el:".main",
        data:bean,
        mounted:function () {
            this.get();
        },
        methods:{
            get:function () {
                var id = getUrlParms("pid");
                var url = getPath()+this.uri+"/"+id+"?timeStamp="+new Date().getTime();
                axios.get(url).then(
                    function (value) {
                        powerVue.power = value.data.power;
                        powerVue.member = value.data.member;
                    }
                );
            },
            exchange:function () {
                var id = getUrlParms("pid");
                var url = getPath()+this.uri+"/"+id+"?timeStamp="+new Date().getTime();
                axios.post(url).then(
                    function (value) {
                        if(value.data=='ok')
                        {
                            $.alert(
                                {
                                    title:'恭喜你!',
                                    content:'兑换成功',
                                    theme:'modern',
                                    icon:'fa fa-smile-o'
                                }
                            );
                        }
                        else
                        {
                            $.alert(
                                {
                                    title:'抱歉!',
                                    content:'您的积分或会员等级不满足条件',
                                    theme:'modern',
                                    icon:'fa fa-close'
                                }
                            );
                        }

                    }
                );
            }
        }
    });
});