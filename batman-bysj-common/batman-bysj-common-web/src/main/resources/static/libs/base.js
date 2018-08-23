//function alert(text) {
////    layer.closeAll();//关闭所有 
////   ;
//    layer.alert(text, {
//        skin: 'layui-layer-molv'
//        ,closeBtn: 0
//        ,anim: 4 //动画类型
//      });
//}

//重写confirm式样框
window.confirm = function(msg, callback){
	
	 layui.use('layer', function(){
	    layer = layui.layer;
	    layer.confirm(msg, {btn: ['确定','取消']},
	    	    function(){//确定事件
	    	       
	    	        if(typeof(callback) === "function"){
	    	            callback("ok");
	    	           alert(1);
	    	        }
	    	    });
	 });
	    
   
}

function wait() {
	 layui.use('layer', function(){
		    layer = layui.layer;
		    layer.load();
		 });
    

}
/** 控制全局界面元素JS** */
//禁止IE缓存AJAX 影响包括了 查询的列表 不会从IE缓存中读取list
$.ajaxSetup({
 cache : false
});
//////////////////////////////////////////////////////////////声明继承类
/////////////////////////////////////////////////////////////
var AJAX_VAR;// AJAX 中断器 变量
/** 取消/中断AJAX* */
function ajax_cancel() {
 try {
     AJAX_VAR.abort();
 } catch (e) {
 }
}
/** 获取公共AJAX* */
function getAjax() {
 return AJAX_VAR;
}
/** 强行更换AJAX* */
function setAjax(ajax) {
 ajax_cancel();
 AJAX_VAR = ajax;
}
/** *** */
/** 封装加载页面Ajax请求* */
function DivLoadingAjax(divid, url, parm) {
 DivLoadingAjax_Inter(divid, url, parm, true);
}
/** 可以中断前面请求的ajax 实在是中断导致系统请求不正常请设置false* */
function DivLoadingAjax_Inter(divid, url, parm, iscan) {
 createloadingpic(divid);
 if (!iscan) {
     $.ajax({
         url : url,
         data : parm,
         success : function(data) {
             $("#" + divid).html(data);
         }
     });
 } else {
     ajax_cancel();
     AJAX_VAR = $.ajax({
         url : url,
         data : parm,
         success : function(data) {
             $("#" + divid).html(data);
         }
     });
 }
 // closeloadingpic(divid);
}
/** *还要执行其他函数的div 自定义callback函数** */
function DivLoadingAjaxWithFunc(divid, url, parm, ohterfunctionname) {
 DivLoadingAjaxWithFunc_Inteter(divid, url, parm, ohterfunctionname, false);
 // closeloadingpic(divid);
}
/** *还要执行其他函数的div 自定义callback函数 实在是中断导致系统请求不正常请设置false* ** */
function DivLoadingAjaxWithFunc_Inteter(divid, url, parm, ohterfunctionname, iscan) {
 createloadingpic(divid);
 if (!iscan) {
     $.ajax({
         url : url,
         data : parm,
         success : function(data) {
             var option = data;
             testFn(ohterfunctionname, option);
         }
     });
 } else {
     ajax_cancel();
     AJAX_VAR = $.ajax({
         url : url,
         data : parm,
         success : function(data) {
             var option = data;
             testFn(ohterfunctionname, option);
         }
     });
 }
 // closeloadingpic(divid);
}
/** 加载json 通常用在button 上* */
function JsonAjax(url, parm, dataType, button_id, ohterfunctionname) {
 JsonAjax_Inter(url, parm, dataType, button_id, ohterfunctionname, "正在执行");
}
/** 加载json 通常用在button 上* */
function JsonAjax_Inter(url, parm, dataType, button_id, ohterfunctionname, buttontext) {
 var loadtext = '正在执行';
 if (buttontext) {
     loadtext = buttontext;
 }
 var buttonhtml = $("#" + button_id).html();
 var buttontext = "";
 if (buttonhtml == null) {
     buttontext = $("#" + button_id).val();
 } else {
     buttontext = buttonhtml;
 }
 $("#" + button_id).val(loadtext);
 $("#" + button_id).html(loadtext);
 $("#" + button_id).attr('disabled', 'disable');
 // otherfunc = callback;
 $.ajax({
     url : url,
     type : "post",
     data : parm,
     dataType : dataType,
     success : function(data) {
         // deal(msg);
         // alert(data.msg);
         var option = data;
         testFn(ohterfunctionname, option);
         $("#" + button_id).val(buttontext);
         $("#" + button_id).html(buttontext);
         $("#" + button_id).attr('disabled', false);
     },
 });
}
/*******************************************************************************
* 用于ajaxform 表单提交
* @param formid
*            提交formid 函数
* @param parm
*            其他参数 格式{name:$("#id").val(),age:$("#age.val)}
* @param dataType
*            一般填写json
* @param button_id
*            提交按钮ID
* @param ohterfunctionname
*            重写回调函数的方法名
* @param buttontext
*            等待中的文字 ,不填 则使用正在执行
******************************************************************************/
function ajaxform(formid, url, parm, dataType, button_id, ohterfunctionname, buttontext) {
 var loadtext = '正在执行';
 if (buttontext) {
     loadtext = buttontext;
 }
 var buttonhtml = $("#" + button_id).html();
 var buttontext = "";
 if (buttonhtml == null) {
     buttontext = $("#" + button_id).val();
 } else {
     buttontext = buttonhtml;
 }
 $("#" + button_id).val(loadtext);
 $("#" + button_id).html(loadtext);
 $("#" + button_id).attr('disabled', 'disable');
 // otherfunc = callback;
 $("#" + formid).ajaxSubmit({
     type : 'post',
     url : url,
     data : parm,
     dataType : dataType,
     success : function(data) {
         var option = data;
         testFn(ohterfunctionname, option);
         $("#" + button_id).val(buttontext);
         $("#" + button_id).html(buttontext);
         $("#" + button_id).attr('disabled', false);
     },
 });
}
function testFn(callback, option) {// 自定义回调方法
 callback.call(this, option);// call()方法的使用
}
/** 加载等待中图片* */
function createloadingpic(div) {
 //$("#" + div).html('<center><img src="/newimages3/system/loading.gif" width="50px" height="50px" />加载中...</center>');
wait();
}
/** 关闭等待中图片 * */
function closewait() {
	 layui.use('layer', function(){
		    layer = layui.layer;
		    layer.closeAll('loading');
		 });
 
   
}
//var a=document.body.innerHTML;
//document.body.innerHTML=a.replace(/\ufeff/g,'');
