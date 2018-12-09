$(
    function () {
        var bean = {
            uri:"/foreReviewSuccess",
            item:{order:{createDate:new Date()},product:{firstProductImage:{id:0}}},
            reviews:[]
        };
        var reviewVue = new Vue(
            {
                el:".main",
                data:bean,
                mounted:function () {
                    this.get();
                },
                filters:{
                  anonyNameFilter:function (name) {
                      if(null==name)
                          return null;

                      if(name.length<=1)
                          return "*";

                      if(name.length==2)
                          return name.substring(0,1) +"*";
                      var arr = name.split('');
                      for (var i = 1; i < arr.length-1; i++) {
                          arr[i] = '*';
                      }
                      return arr.join('');
                  }
                },
                methods:{
                    get:function () {
                        var id = getUrlParms("oid");
                        var param = "?timeStamp=" + new Date().getTime();
                        var url = getPath() + this.uri + "/" + id + param;
                        axios.get(url).then(
                            function (value) {
                                reviewVue.item = value.data.item;
                                reviewVue.reviews = value.data.reviews;
                            }
                        );
                    },
                    getImage: function (name, id) {
                        if (name == null || id == 0)
                            return;
                        else {
                            var url = getPath() + "/image/" + name + "/" + id + ".jpg";
                            return url;
                        }
                    }
                }
            }
        );
    }
);