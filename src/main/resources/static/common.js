// 设置全局ajax的配置
$.ajaxSetup({
    beforeSend: function (xhr) {
        xhr.setRequestHeader('my-lang', navigator.language);
    }
});