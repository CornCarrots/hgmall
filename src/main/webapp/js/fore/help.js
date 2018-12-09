$(
    function () {
        var bean = {
            uri:"/foreHelp",
            categories:[],
            articles:[],
            index :0,
            category:{}
        };
        var helpVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function () {
                    this.get();
                },
                methods:{
                    get:function () {
                        var url = getPath()+this.uri+"?timeStamp="+new Date().getTime();
                        axios.get(url).then(function (value) {
                            helpVue.categories = value.data.categories;
                            helpVue.getTitle(0);
                        });
                    },
                    getTitle:function (i) {
                        $(".questionDiv").show();
                        $(".questionDiv>a").show();
                        $(".questionDiv>p").show();
                        $(".questionDiv .artContent").hide();
                        helpVue.index = i;
                        helpVue.category = helpVue.categories[i];
                        helpVue.articles = helpVue.categories[i].articles;
                    },
                    getArticle:function (e) {
                        $(".questionDiv").hide();
                        $(e.target).parent().show();
                        $(e.target).next().next().show();
                        $(e.target).hide();
                        $(e.target).next().hide();
                    }
                }
            }
        );
    }
);