<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

<!-- page content -->
<div class="right_col" role="main">
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>Transfer Type</h3>
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
								Transfer Type Detail
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
										<a href="editTransferType?id=${transferTypeID}">Edit</a>
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
						<table id="detailTransferType" class="table table-striped">
							<tbody>
								<tr>
									<td width="150">ID </td>
									<td width="150"> : </td>
									<td align="right">${transferTypeID}</td>
								</tr>
								<tr>
									<td width="150">Name </td>
									<td width="150"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="150">Description </td>
									<td width="150"> : </td>
									<td align="right">${description}</td>
								</tr>
								<tr>
									<td width="150">From Account </td>
									<td width="150"> : </td>
									<td align="right">${fromAccount}</td>
								</tr>
								<tr>
									<td width="150">To Account </td>
									<td width="150"> : </td>
									<td align="right">${toAccount}</td>
								</tr>
								<tr>
									<td width="150">Minimum Amount </td>
									<td width="150"> : </td>
									<td align="right">${minAmount}</td>
								</tr>
								<tr>
									<td width="150">Maximum Amount </td>
									<td width="150"> : </td>
									<td align="right">${maxAmount}</td>
								</tr>
								<tr>
									<td width="150">OTP Threshold </td>
									<td width="150"> : </td>
									<td align="right">${otpThreshold}</td>
								</tr>
								<tr>
									<td width="150">Maximum Transaction </td>
									<td width="150"> : </td>
									<td align="right">${maxTransaction}</td>
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
                    <h2>Transfer Type Permission</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                 
                   <div class="x_content">
             	     <div align="right">
             			<p><button onClick="location.href='/admin/createTransferTypePermission?id=${transferTypeID}'" class="btn btn-success">Add New Permission</button>
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
               
               <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Fee Configuration</h2>
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
             			<p><button onClick="location.href='/admin/createFee?transferTypeID=${transferTypeID}'" class="btn btn-success">Add New Fee</button>
             		</div>
             	   <table id="feetable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Fee Name</th>
                          <th>Fixed Amount</th>
						 <th>Percentage</th>
                          <th>Priority</th>
                          <th>Enabled</th>
                          <th>Action</th>
                        </tr>
                      </thead>                      
                    </table>
                  </div>
                </div>
               </div>
               
               <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Transfer Notification</h2>
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
             			<p><button onClick="location.href='/admin/createTransferNotification?id=${transferTypeID}'" class="btn btn-success">Add New Notification</button>
             		</div>
             	   <table id="notificationtable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Notification Name</th>
                          <th>Destination Module</th>
                          <th>Notification Type</th>
                          <th>Enabled</th>
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
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/transferTypePermissionData",
       					 "data" : function ( d ) {
 		               	  	d.id = "${transferTypeID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "permissionID"
							}, {
								"data" : "group.name"
							}, {
								"data" : "permissionID",
								"render" : function ( data, type, row ) {
                   					 return"<a href='deleteTransferTypePermission?id=" + data + "' onclick='return ConfirmDelete();'  class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i> Delete </a>";
               					 }
							}]
					});
	</script>
	
	<script>
 		$("#feetable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/feeData",
       					 "data" : function ( d ) {
 		               	  	d.transferTypeID = "${transferTypeID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "name"
							}, {
								"data" : "fixedAmount"
							}, {
								"data" : "percentageValue"
							}, {
								"data" : "priority"
							}, {
								"data" : "enabled"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return"<a href='feeDetail?id=" + data + "' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail </a>" +
                   					 "<a href='editFee?id=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-pencil'></i> Edit </a>";
               					 }
							}]
					});
	</script>
	
	<script>
 		$("#notificationtable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/transferNotificationData",
       					 "data" : function ( d ) {
 		               	  	d.transferTypeID = "${transferTypeID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "notificationName"
							}, {
								"data" : "destinationModule"
							}, {
								"data" : "notificationType"
							}, {
								"data" : "enabled"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return"<a href='editTransferNotification?id=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-pencil'></i> Edit</a>" +
                   					 "<a href='deleteTransferNotificationForm?id=" + data + "' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i> Delete</a>";
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
