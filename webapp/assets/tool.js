/**
 * Created by root on 15-10-29.
 */
// IE8下 console.log提示未定义错误
window.console = window.console || (function(){
        var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile
            = c.clear = c.exception = c.trace = c.assert = function(){};
        return c;
    })();