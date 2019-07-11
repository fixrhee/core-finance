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
                    <br /> <br /> </div>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-24">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Member Details</small></h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings 1</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  
                  <div class="x_content">
                    <table id="trxdetailMember" class="table table-striped">
                      <tbody>
                      	<tr>
                          <td width="150">ID</td>
                          <td width="150"> : </td>
                          <td align="right">${id}</td>
                        </tr>
                      	<tr>
                          <td width="150">Status Approval</td>
                          <td width="150"> : </td>
                          <td align="right">${statusApproval}</td>
                        </tr>
                        <tr>
                          <td width="150">Requested Date</td>
                          <td width="150"> : </td>
                          <td align="right">${requestedDate}</td>
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
                          <td width="150">ID Card Nomor</td>
                          <td width="150"> : </td>
                          <td align="right">${idCard}</td>
                        </tr>
                        <tr>
                          <td width="150">Mother Maiden Name</td>
                          <td width="150"> : </td>
                          <td align="right">${motherName}</td>
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
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
        <!-- /page content -->
   
<%@include file="footer.jsp" %>

<script>
	function reloadTable(){
		var table = $('#trxtable').DataTable();
		table.ajax.reload( null, false );
	}
</script>
	
<script>
 		$("#trxdetailMember")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/detailMemberData",
       					 "data" : function ( d ) {
 		               	  	
 			           		}
 			          }
					});
	</script>
	</body>
</html>
