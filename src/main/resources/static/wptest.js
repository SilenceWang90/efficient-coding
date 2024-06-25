/** 请求头放入lang参数 **/
var testi18n = function () {
    $.ajax({
        url: "/test-i18n/getSpecificI18nInfo?specific-keys=greet",
        type: "GET",
        headers: {
            "lang": "en"
        },
        success: function (response) {
            alert("返回的信息为：" + response.greet);
        }
    })
};
