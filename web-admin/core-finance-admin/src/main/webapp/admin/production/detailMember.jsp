<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

<!-- page content -->
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Manage Member</h3>
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
							Member Details
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
						<table id="trxdetailMember" class="table table-striped">
							<tbody>
								<tr>
									<td width="150">GroupID </td>
									<td width="150"> : </td>
									<td align="right">${groupID}</td>
								</tr>
								<tr>
									<td width="150">Group</td>
									<td width="150"> : </td>
									<td align="right">${groupName}</td>
								</tr>
								<tr>
									<td width="150">Username</td>
									<td width="150"> : </td>
									<td align="right">${username}</td>
								</tr>
								<tr>
									<td width="150">Name</td>
									<td width="150"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="150">Mobile No</td>
									<td width="150"> : </td>
									<td align="right">${msisdn}</td>
								</tr>
								<tr>
									<td width="150">Email</td>
									<td width="150"> : </td>
									<td align="right">${email}</td>
								</tr>
								<tr>
									<td width="150">Created Date</td>
									<td width="150"> : </td>
									<td align="right">${createdDate}</td>
								</tr>
								<tr>
									<td width="150">Address</td>
									<td width="150"> : </td>
									<td align="right">${address}</td>
								</tr>
								<tr>
									<td width="150">ID Card No</td>
									<td width="150"> : </td>
									<td align="right">${idCard}</td>
								</tr>
								<tr>
									<td width="150">Mother Maiden Name</td>
									<td width="150"> : </td>
									<td align="right">${motherName}</td>
								</tr>
								<tr>
									<td width="150">Work</td>
									<td width="150"> : </td>
									<td align="right">${work}</td>
								</tr>
								<tr>
									<td width="150">Sex</td>
									<td width="150"> : </td>
									<td align="right">${sex}</td>
								</tr>
								<tr>
									<td width="150">Nationality</td>
									<td width="150"> : </td>
									<td align="right">${nationality}</td>
								</tr>
								<tr>
									<td width="150">Place of Birth</td>
									<td width="150"> : </td>
									<td align="right">${pob}</td>
								</tr>
								<tr>
									<td width="150">Date of Birth</td>
									<td width="150"> : </td>
									<td align="right">${dob}</td>
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
							<h2>List Account Member</h2>
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
							<table id="accounttable"
								class="table table-striped table-bordered dt-responsive nowrap"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th>ID</th>
										<th>Name</th>
										<th>Description</th>
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
 		$("#accounttable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/accountData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${username}";
 		               	  	d.groupID = "${groupID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "name"
							}, {
								"data" : "description"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<a href='transactionHistory?username=${username}&GroupID=${groupID}&AccountID=" + data + "' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail</a>";
               					 }
							}]
					});
	</script> 

	</body>
</html>
