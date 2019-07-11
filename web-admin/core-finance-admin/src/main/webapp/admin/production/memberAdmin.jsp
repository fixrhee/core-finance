
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

<!-- page content -->
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Member</h3>
			</div>

			<div class="title_right">
				<div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
					<div class="input-group">
					<br /> <br />
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>Search Member</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li>
								<a class="collapse-link">
									<i class="fa fa-chevron-up"></i>
								</a>
							</li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"
									role="button" aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li>
										<a href="#">Settings</a>
									</li>
								</ul>
							</li>
							<li>
								<a class="close-link">
									<i class="fa fa-close"></i>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>

					<div class="x_content">
						<form id="searchMember" data-parsley-validate class="form-horizontal form-label-left"
							action="/admin/detailMember" method="GET" modelAttribute="searchmember">
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="username">
									Username :
								</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="username" name="username" required="required"
										class="form-control col-md-7 col-xs-12" />
								</div>
							</div>
			  			
						<div class="ln_solid"></div>
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button class="btn btn-primary" type="reset">Reset</button>
									<button type="submit" id="sbm" class="btn btn-success">Search</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>Manage Member</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li>
								<a class="collapse-link">
									<i class="fa fa-chevron-up"></i>
								</a>
							</li>
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"
									role="button" aria-expanded="false">
									<i class="fa fa-wrench"></i>
								</a>
								<ul class="dropdown-menu" role="menu">
									<li>
										<a href="#">Settings</a>
									</li>
								</ul>
							</li>
							<li>
								<a class="close-link">
									<i class="fa fa-close"></i>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>

					<div class="x_content">
						<c:choose>
							    <c:when test="${member.groupName == 'ADMIN'}">
							        <div align="right">
										<p>
											<button
												onClick="location.href='/admin/createMember'"
												class="btn btn-success">Create New Member</button>
									</div>
									<table id="membertable"
										class="table table-striped table-bordered dt-responsive nowrap"
										cellspacing="0" width="100%">
										<thead>
											<tr>
												<th>ID</th>
												<th>Username</th>
												<th>Group</th>
												<th>Name</th>
												<th>Email</th>
												<th>Created Date</th>
												<th>Action</th>
											</tr>
										</thead>
									</table>
							    </c:when>
							    <c:otherwise>
										<table id="membertable"
											class="table table-striped table-bordered dt-responsive nowrap"
											cellspacing="0" width="100%">
											<thead>
												<tr>
													<th>ID</th>
													<th>Username</th>
													<th>Group</th>
													<th>Name</th>
													<th>Email</th>
													<th>Created Date</th>
													<th>Action</th>
												</tr>
											</thead>
										</table>
							    </c:otherwise>
							</c:choose>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
</div>
<!-- /page content -->

<%@include file="footer.jsp" %>


<c:if test="${not empty fn:trim(notification)}">
<script type="text/javascript">
	$(function(){
 	 new PNotify({
        title: '${title}',
        text: '${message}',
        type: '${notification}',
        styling: 'bootstrap3'
        });
	});
	</script>
</c:if>

 <script>
 		$("#membertable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/memberData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${member.username}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "username"
							}, {
								"data" : "group"
							}, {
								"data" : "name"
							}, {
								"data" : "email"
							}, {
								"data" : "createdDate"
							}, {
								"data" : "username",
								"render" : function ( data, type, row ) {
                   					 return "<a href='detailMember?username=" + data + "' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail</a>" + 
                   					 "<a href='editMember?username=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-pencil'></i> Edit</a>";
               					}
							}]
					});
	</script> 

	</body>
</html>