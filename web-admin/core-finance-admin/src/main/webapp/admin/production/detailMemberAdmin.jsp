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
										<a href="/admin/editMember?username=${username}">Edit</a>
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
							<h2>
								Access Administration
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
							<form id="unblokpin" data-parsley-validate class="form-horizontal form-label-left"
								action="/admin/accessMember" method="POST" modelAttribute="accessmember">
								<div class="form-group">
									<input id="msisdn" name="msisdn" value="${username}"
										class="date-picker form-control col-md-7 col-xs-12" type="hidden" />
									<input id="email" name="email" value="${email}"
										class="date-picker form-control col-md-7 col-xs-12" type="hidden" />
									<input id="username" name="username" value="${member.username}" class="date-picker 
										form-control col-md-7 col-xs-12" type="hidden">
									<!--input id="sessionID" name="sessionID" value="${sessionID}"
										class="date-picker form-control col-md-7 col-xs-12" type="hidden" /-->
								</div>
								<div class="form-group">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">
												Unblock PIN
											</label>
											<div class="col-md-3 col-sm-3 col-xs-12">
												<c:choose>
												    <c:when test="${isBlocked == true}">
												        <button id="unblockPin" name="access" value="unblockPin" type="submit" class="btn btn-primary">Unblock PIN</button>
												    </c:when>
												    <c:otherwise>
												    	<button id="unblockPin" disabled name="access" value="unblockPin" type="submit" class="btn btn-primary">Unblock PIN</button>
												    </c:otherwise>
												</c:choose>
											</div>
										</div>
										<br>
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">
												Reset PIN
											</label>
											<div class="col-md-3 col-sm-3 col-xs-12">
												<button id="resetPin" name="access" value="resetPin" type="submit" class="btn btn-primary">Reset PIN</button>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">
												Change PIN
											</label>
											<div class="col-md-3 col-sm-3 col-xs-12">
												<button id="changePin" name="access" value="changePin" type="submit" class="btn btn-primary" >Change PIN</button>
											</div>
										</div>
										<br>
										<div class="form-group">
											<label class="control-label col-md-3 col-sm-3 col-xs-12"
												for="first-name">
												Change Group
											</label>
											<div class="col-md-3 col-sm-3 col-xs-12">
												<button id="changeGroup" name="access" value="changeGroup" type="submit" class="btn btn-primary">Change Group</button>
											</div>
										</div>
									</div>
								</div>
							</form>
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
                   					 return "<a href='transactionHistory?username=${username}&GroupID=${groupID}&AccountID=" + data+"' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail</a>";
               					 }
							}]
					});
	</script> 
	</body>
</html>
