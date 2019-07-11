
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

<!-- page content -->
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Payment Collection</h3>
			</div>

			<div class="title_right">
				<div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
					<div class="input-group">
						<input type="text" class="form-control" placeholder="Search for...">
							<span class="input-group-btn">
								<button class="btn btn-default" type="button">Go!</button>
							</span>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>

		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>Point of Sales</h2>
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
						<div align="right">
							<p>
								<button
									onClick="location.href='/admin/createPos'"
									class="btn btn-success">Create New POS</button>
						</div>
						<table id="postable"
							class="table table-striped table-bordered dt-responsive nowrap"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>ID</th>
									<th>Username</th>
									<th>Address No</th>
									<th>City</th>
									<th>Postal Code</th>
									<th>Action</th>
								</tr>
							</thead>
						</table>
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
 		$("#postable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/posData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${member.username}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							},{
								"data" : "username"
							}, {
								"data" : "address"
							}, {
								"data" : "city"
							}, {
								"data" : "postalCode"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<a href='detailPos?id=" + data + "&username=${member.username}'>Detail</a> &nbsp;&nbsp;/&nbsp;&nbsp; <a href='editPos?id=" + data + "&username=${member.username}'>Edit</a> &nbsp;&nbsp;/&nbsp;&nbsp; <a href='deletePos?id=" + data + "&username=${member.username}' onclick='return ConfirmDelete();'>Delete</a>";
               					}
							}]
					});
	</script> 
	
	<script>
	    function ConfirmDelete()
	    {
	      var x = confirm("Are you sure you want to delete ?");
	      if (x)
	          return true;
	      else
	        return false;
	    }
	</script>    
	</body>
</html>