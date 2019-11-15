<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet"
	href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
<link rel="stylesheet" href="${APP_PATH}/css/main.css">
<link rel="stylesheet" href="${APP_PATH}/css/pagination.css">
<style>
.tree li {
	list-style-type: none;
	cursor: pointer;
}

table tbody tr:nth-child(odd) {
	background: #F4F4F4;
}

table tbody td:nth-child(even) {
	color: #C00;
}
</style>
</head>

<body>

	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<div>
					<a class="navbar-brand" style="font-size: 32px;" href="#">众筹平台
						- 广告维护</a>
				</div>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li style="padding-top: 8px;"><%@include
							file="/WEB-INF/jsp/common/userinfo.jsp"%>
					</li>
					<li style="margin-left: 10px; padding-top: 8px;">
						<button type="button" class="btn btn-default btn-danger">
							<span class="glyphicon glyphicon-question-sign"></span> 帮助
						</button>
					</li>
				</ul>
				<form class="navbar-form navbar-right">
					<input type="text" class="form-control" placeholder="Search...">
				</form>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<div class="tree">
					<%@include file="/WEB-INF/jsp/common/left.jsp" %>
				</div>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-th"></i> 数据列表
						</h3>
					</div>
					<div class="panel-body">
						<form class="form-inline" id="form" style="float: left;">
							<div class="form-group has-feedback">
								<div class="input-group">
									<div class="input-group-addon">查询条件</div>
									<input id="queryText" class="form-control has-success" type="text"
										placeholder="请输入查询条件">
								</div>
							</div>
							<button type="button" class="btn btn-warning" onclick="queryAdvert()">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
						</form>
						<button id="batchDelete" type="button" class="btn btn-danger"
							style="float: right; margin-left: 10px;">
							<i class="glyphicon glyphicon-remove"></i> 删除
						</button>
						<button type="button" class="btn btn-primary"
							style="float: right;" onclick="window.location.href='${APP_PATH}/advert/add.htm'">
							<i class="glyphicon glyphicon-plus"></i> 新增
						</button>
						<br>
						<hr style="clear: both;">
						<div class="table-responsive">
							<table class="table  table-bordered">
								<thead>
									<tr>
										<th width="30">#</th>
										<th width="30"><input type="checkbox"></th>
										<th>名称</th>
										<th>地址</th>
										<th>状态</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
								<tfoot>
								<div id="fenye" class="box-footer">

								</div>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
	<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH}/script/docs.min.js"></script>
	<script src="${APP_PATH}/jquery/layer/layer.js"></script>
	<script src="${APP_PATH}/jquery/pagination/jquery.pagination.js"></script>
	<script src="${APP_PATH}/script/showLeft.js"></script>
	<script type="text/javascript">
            $(function () {
			    $(".list-group-item").click(function(){
				    if ( $(this).find("ul") ) {
						$(this).toggleClass("tree-closed");
						if ( $(this).hasClass("tree-closed") ) {
							$("ul", this).hide("fast");
						} else {
							$("ul", this).show("fast");
						}
					}
				});
				queryPage(1, 10);
				var path = "${requestScope['javax.servlet.forward.request_uri']}";
				showLeft(path);

            });

			//定义在前面是为了给后面拿,下拉框选择的每页的大小
			var sePageSize;

			function selectPagesize() {
				//获取下拉框的值
				sePageSize = $("#changePageSize").val();

				//向服务器发送请求，改变每页显示条数
				queryPage(1, sePageSize);

			};

			//选择第几页
			function selectPage(page, pagesize) {
				queryPage(page, pagesize);

			}

			//为后面查询添加json数据方便，一般直接就写在ajax的data里了。
			var jsonObj = {
				"page": 1,
				"pagesize": 10
			};

			var loadingIndex = -1;


			//展示页面或展示查询后的页面
			function queryPage(page, pagesize) {
				jsonObj.page = page;
				jsonObj.pagesize = pagesize;
				$.ajax({
					type: "post",
					data: jsonObj,
					url: "${APP_PATH}/advert/showAdverts.do",
					beforeSend: function () {
						loadingIndex = layer.load(2, {time: 10 * 1000});
						return true;
					},
					success: function (result) {
						layer.close(loadingIndex);
						if (result.success) {
							var pageInfo = result.pageInfo;
							var adverts = result.pageInfo.list;
							var content = "";


							//i是从0开始的
							$.each(adverts, function (i, advert) {
								content += '<tr>';
								content += '	<td>' + (i + 1) + '</td>';
								content += '	<td><input type="checkbox" id="' + advert.id +'" ></td>';
								content += '	<td>' + advert.name + '</td>';
								content += '	<td>' + advert.url + '</td>';
								if(advert.status=='0'){
									content+="	<td>草稿</td>";
								}else if(advert.status=='1'){
									content+="	<td>未审核</td>";
								}else if(advert.status=='2'){
									content+="	<td>审核完成</td>";
								}else if(advert.status=='3'){
									content+="	<td>发布</td>";
								}
								content += '	<td>';
								content += '		<button type="button" onclick="window.location=\'${APP_PATH}/advert/assignPermission.htm?id=' + advert.id + '\'" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';
								content += '		<button type="button" onclick="window.location=\'${APP_PATH}/advert/toUpdate.htm?id=' + advert.id + '\'" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>';
								content += '		<button type="button" onclick="deleteAdvert(' + advert.id + ')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
								content += '	</td>';
								content += '</tr>';
							});
							$("tbody").html(content);

							var fenyeContent = "";
							fenyeContent += '<div class="pull-left">';
							fenyeContent += '	<div class="form-group form-inline">';
							fenyeContent += '		总共' + pageInfo.pages + '页，共' + pageInfo.total + '条数据。 每页';
							fenyeContent += '		<select class="form-control" id="changePageSize" onchange="selectPagesize()">';
							fenyeContent += '			<option value="1">1</option>';
							fenyeContent += '			<option value="2">2</option>';
							fenyeContent += '			<option value="3">3</option>';
							fenyeContent += '			<option value="4">4</option>';
							fenyeContent += '			<option value="5">5</option>';
							fenyeContent += '			<option value="6">6</option>';
							fenyeContent += '			<option value="7">7</option>';
							fenyeContent += '			<option value="8">8</option>';
							fenyeContent += '			<option value="9">9</option>';
							fenyeContent += '			<option value="10" selected>10</option>';
							fenyeContent += '		</select> 条';
							fenyeContent += '	</div>';
							fenyeContent += '</div>';
							fenyeContent += '<div class="box-tools pull-right">';
							fenyeContent += '<ul class="pagination">';
							fenyeContent += '		<li>';
							fenyeContent += '			<a href="#" onclick="selectPage(1,' + sePageSize + ')" aria-label="Previous">首页</a>';
							fenyeContent += '		</li>';
							fenyeContent += '		<li><a href="#" onclick="selectPage(' + (pageInfo.pageNum - 1) + ',' + sePageSize + ')">上一页</a></li>';
							for (var i = 1; i <= pageInfo.pages; i++) {
								fenyeContent += '			<li';
								if (pageInfo.pageNum == i) {
									fenyeContent += '	class="active"';
								}
								fenyeContent += '			><a href="#" onclick="selectPage(' + i + ',' + sePageSize + ')">' + i + '</a></li>';
							}
							fenyeContent += '		<li><a href="#" onclick="selectPage(' + (pageInfo.pageNum + 1) + ',' + sePageSize + ')">下一页</a></li>';
							fenyeContent += '		<li>';
							fenyeContent += '			<a href="#" onclick="selectPage(' + (pageInfo.pages) + ',' + sePageSize + ')" aria-label="Next">尾页</a>';
							fenyeContent += '		</li>';
							fenyeContent += '	</ul>';
							fenyeContent += '</div>';
							$("#fenye").html(fenyeContent);

							$("#changePageSize").children('option').each(function () {
								if ($(this).val() == sePageSize) {
									$(this).prop('selected', true);
								}
							});

						} else {
							layer.msg(result.message, {time: 1000, icon: 5, shift: 6});
						}
					},
					error: function () {
						layer.msg("加载数据失败!！", {time: 1000, icon: 5, shift: 6});
					}
				});
			};

			$("#queryBtn").on("click", function () {
				var queryText = $("#queryText").val();
				if (queryText == null || $.trim(queryText) == "") {
					return fasle;
				}
				jsonObj.queryText = queryText;
				queryPage(1, 10);
			});

			//删除单个
			function deleteAdvert(id) {
				layer.confirm("确认要删除用户吗?", {icon: 3, title: '提示'}, function (cindex) {
					layer.close(cindex);
					$.ajax({
						type: "post",
						data: {
							"id": id
						},
						url: "${APP_PATH}/advert/doDelete.do",
						beforeSend: function () {
							return true;
						},
						success: function (result) {
							if (result.success) {
								window.location = "${APP_PATH}/advert/index.htm"
							} else {
								layer.msg("删除用户失败!！", {time: 1000, icon: 5, shift: 6});
							}
						},
						error: function () {
							layer.msg("删除失败!！", {time: 1000, icon: 5, shift: 6});
						}
					});
				}, function (clinex) {
					layer.close(clinex);
				});
			};





			//全选事件
			$("#allSelected").on("click",function () {
				var checkedValue = $(this).prop("checked");
				$("tbody input").prop("checked", checkedValue);
			});


			$("tbody").on("click","input",function () {
				var numOfAll = $('tbody input').length;
				var numOfSelect = $('tbody input:checked').length;


				$('#allSelected').prop('checked',numOfAll == numOfSelect);
			});



			//批量删除按钮
			$("#deleteBatchBtn").on("click", function () {

				var selectedRows = $('tbody input:checked');
				if(selectedRows.length==0){
					layer.msg("至少选择一个用户进行删除!请选择用户!", {time:1000, icon:5, shift:6});
					return false ;
				}


				var strIds = [];
				for (var i = 0; i < selectedRows.length; i++) {
					strIds.push(selectedRows[i].id);
				}
				var ids = strIds.join(",");



				layer.confirm("确定要删除吗?", {icon: 3, title: '提示'}, function (cindex) {
					layer.close(cindex);
					$.ajax({
						type: "post",
						data: {
							"ids": ids
						},
						url: "${APP_PATH}/advert/doDeleteBatch.do",
						beforeSend: function () {
							return true;
						},
						success: function (result) {
							if (result.success) {
								window.location = "${APP_PATH}/advert/index.htm";
							} else {
								layer.msg("删除用户失败", {time: 1000, icon: 5, shift: 6})
							}
						},
						error: function () {
							layer.msg("删除失败", {time: 1000, icon: 5, shift: 6})
						}
					})
				}, function (cindex) {
					layer.close(cindex);
				})
			});


            

        </script>

</body>

</html>