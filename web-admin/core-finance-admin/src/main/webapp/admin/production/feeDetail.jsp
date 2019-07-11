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
								Fee Detail
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
										<a href="#">Edit</a>
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
									<td width="250">ID </td>
									<td width="250"> : </td>
									<td align="right">${feeID}</td>
								</tr>
								<tr>
									<td width="250">Name </td>
									<td width="250"> : </td>
									<td align="right">${name}</td>
								</tr>
								<tr>
									<td width="250">Description </td>
									<td width="250"> : </td>
									<td align="right">${description}</td>
								</tr>
								<tr>
									<td width="250">From Member </td>
									<td width="250"> : </td>
									<td align="right">${fromMemberName}</td>
								</tr>
								<tr>
									<td width="250">From Account </td>
									<td width="250"> : </td>
									<td align="right">${fromAccountName}</td>
								</tr>
								<tr>
									<td width="250">To Member </td>
									<td width="250"> : </td>
									<td align="right">${toMemberName}</td>
								</tr>
								<tr>
									<td width="250">To Account </td>
									<td width="250"> : </td>
									<td align="right">${toAccountName}</td>
								</tr>
									<tr>
									<td width="250">Start Date </td>
									<td width="250"> : </td>
									<td align="right">${startDate}</td>
								</tr>
								<tr>
									<td width="250">End Date </td>
									<td width="250"> : </td>
									<td align="right">${endDate}</td>
								</tr>
								<tr>
									<td width="250">Minimum Amount </td>
									<td width="250"> : </td>
									<td align="right">${initialRangeAmount}</td>
								</tr>
								<tr>
									<td width="250">Maximum Amount </td>
									<td width="250"> : </td>
									<td align="right">${maximumRangeAmount}</td>
								</tr>
								<tr>
									<td width="250">Deduct Amount </td>
									<td width="250"> : </td>
									<td align="right">${fn:toUpperCase(deductAmount)}</td>
								</tr>
								<tr>
									<td width="250">Fixed Amount </td>
									<td width="250"> : </td>
									<td align="right">${fixedAmount}</td>
								</tr>
								<tr>
									<td width="250">Percentage </td>
									<td width="250"> : </td>
									<td align="right">${percentageValue}</td>
								</tr>
								<tr>
									<td width="250">Filter By Source Member </td>
									<td width="250"> : </td>
									<td align="right">${fn:toUpperCase(filterSource)}</td>
								</tr>
								<tr>
									<td width="250">Filter By Destination Member </td>
									<td width="250"> : </td>
									<td align="right">${fn:toUpperCase(filterDestination)}</td>
								</tr>								
								<tr>
									<td width="250">Priority </td>
									<td width="250"> : </td>
									<td align="right">${fn:toUpperCase(priority)}</td>
								</tr>
								<tr>
									<td width="250">Enabled </td>
									<td width="250"> : </td>
									<td align="right">${fn:toUpperCase(enabled)}</td>
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
                    <h2>Member Filtering</h2>
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
             			<p><button onClick="location.href='/admin/createMemberFiltering?feeID=${feeID}'" class="btn btn-success">Add New Member Filter</button>
             		</div>
             	   <table id="memberFilterTable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Name</th>
						  <th>Source/Destination</th>
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
                    <h2>Broker Fee Configuration</h2>
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
             			<p><button onClick="location.href='/admin/createBroker?feeID=${feeID}'" class="btn btn-success">Add New Broker</button>
             		</div>
             	   <table id="brokerTable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Broker Name</th>
                          <th>Deduct All Fee</th>
						  <th>Fixed Amount</th>
						 <th>Percentage</th>
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
 		$("#brokerTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/brokerData",
       					 "data" : function ( d ) {
 		               	  	d.id = "${feeID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "name"
							}, {
								"data" : "deductAllFee"
							}, {
								"data" : "fixedAmount"
							}, {
								"data" : "percentageValue"
							}, {
								"data" : "enabled"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return"<a href='brokerDetail?id=" + data + "' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail</a>" +
                   					 "<a href='editBroker?id=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-pencil'></i> Edit</a>";
               					 }
							}]
					});
	</script>
	
	<script>
 		$("#memberFilterTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/feeMemberFilterData",
       					 "data" : function ( d ) {
 		               	  	d.feeID = "${feeID}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "name"
							}, {
								"data" : "destination",
								"render" : function ( data, type, row ) {
								if (data == true) { return "DESTINATION" } else { return "SOURCE" };
								}
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return"<a href='deleteMemberFilteringForm?id=" + data + "' onclick='return ConfirmDelete();' class='btn btn-danger btn-xs'><i class='fa fa-trash-o'></i> Delete</a>";
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
