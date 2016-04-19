//document.write('<script src="/assets/modernizr/modernizr.js"></script>');
//document.write('<script src="/assets/underscore/underscore.js"></script>');
//document.write('<script src="/assets/jquery-migrate/jquery-migrate.min.js"></script>');
//document.write('<script src="/assets/jquery-ui-bootstrap/assets/js/jquery-ui-1.10.0.custom.min.js"></script>');
//document.write('<script src="/assets/jquery.validation/dist/jquery.validate.js"></script>');
//document.write('<script src="/assets/jquery.validation/src/localization/messages_zh.js"></script>');
//document.write('<script src="/assets/jquery.uniform/jquery.uniform.js"></script>');
//document.write('<script src="/assets/Placeholders.js/placeholders.jquery.min.js"></script>');
//document.write('<script src="/assets/select2/select2.js"></script>');
//document.write('<script src="/assets/noty/js/noty/jquery.noty.js"></script>');
//document.write('<script src="/assets/noty/js/noty/layouts/top.js"></script>');
//document.write('<script src="/assets/noty/js/noty/themes/default.js"></script>');
//document.write('<script src="/assets/app/jquery-ujs.js"></script>');
//document.write('<script src="/assets/app/application.js"></script>');
//document.write('<script src="/assets/fileUpload/ajaxfileupload.js"></script>');
//document.write('<script src="/assets/fileUpload/upload.js"></script>');
//document.write('<script src="/assets/zTree_v3/js/jquery.ztree.core-3.5.js"></script>');
document.write('<script src="/assets/daterangepicker/daterangepicker.js"></script>');
document.write('<script src="/assets/loading/loading.js"></script>');

/**
 * 刷新验证码图片
 * @param imgObj
 */
function refreshCode(imgObj){
    if (!imgObj) {
        imgObj = document.getElementById("validationCodeImg");
    }
    var index = imgObj.src.indexOf("?");
    if(index != -1) {
        var url = imgObj.src.substring(0,index + 1);
        imgObj.src = url + Math.random();
    } else {
        imgObj.src = imgObj.src + "?" + Math.random();
    }
}