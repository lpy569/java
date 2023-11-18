function getLoginStatus() {
        $.ajax({
            type: 'get',
            url: 'login',
            success: function(body) {
                // 已经登录
                console.log('已经登录了');
            },

            error: function() {
                // 页面跳转
                location.assign('login.html');

            }

        })
    }