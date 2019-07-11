<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

<!-- page content -->
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Webservice</h3>
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
						<h2>
							</small>
								Webservice Detail
							</small>
						</h2>
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
										<a href="editWebservice?id=${id}">Edit</a>
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
						<table id="detailAccount" class="table table-striped">
							<tbody>
								<tr>
									<td width="150">ID </td>
									<td width="150"> : </td>
									<td align="right">${id}</td>
								</tr>
								<tr>
									<td width="150">Username </td>
									<td width="150"> : </td>
									<td align="right">${username}</td>
								</tr>
								<tr>
									<td width="150">Password </td>
									<td width="150"> : </td>
									<td align="right">**********</td>
								</tr>
								<tr>
									<td width="150">Webservice Name </td>
									<td width="150"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="150">Hash </td>
									<td width="150"> : </td>
									<td align="right">${hash}</td>
								</tr>
								<tr>
									<td width="150">Enabled </td>
									<td width="150"> : </td>
									<td align="right">${enabled}</td>
								</tr>
								<tr>
									<td width="150">PIN Enabled </td>
									<td width="150"> : </td>
									<td align="right">${secure}</td>
								</tr>
							</tbody>
						</table>
						<hr />
					</div>
				</div>
			</div>

	          <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Webservice Permission</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                 
                   <div class="x_content">
             	     <div align="right">
             			<p><button onClick="location.href='/admin/createWebservicePermission?id=${id}'" class="btn btn-success">Add New Permission</button>
             		</div>
             	   <table id="permissiontable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Group Name</th>
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
 		$("#permissiontable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/webservicePermissionData",
       					 "data" : function ( d ) {
 		               	  	d.id = "${id}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "permissionID"
							}, {
								"data" : "group.name"
							}, {
								"data" : "permissionID",
								"render" : function ( data, type, row ) {
                   					 return "<a href='deleteWebservicePermission?id=" + data + "' onclick='return ConfirmDelete();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i> Delete</a>";
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
